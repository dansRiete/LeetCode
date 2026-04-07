package dev.alexkzk.resilience;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for:
 *   A) ExponentialBackoff
 *   B) CircuitBreaker
 *   C) Fallback
 *   D) Combined: Fallback + CircuitBreaker + Backoff working together
 */
class ResiliencePatternsTest {

    // =========================================================================
    // A. ExponentialBackoff
    // =========================================================================

    @Nested
    @DisplayName("A. ExponentialBackoff")
    class BackoffTests {

        // --- A1: ceiling doubles each attempt, capped at max ---

        @ParameterizedTest(name = "A1: base=100 max=3000 attempt={0} → ceiling={1}ms")
        @CsvSource({
            "0,  100",
            "1,  200",
            "2,  400",
            "3,  800",
            "4, 1600",
            "5, 3000",   // 3200 would exceed max=3000, so capped
            "6, 3000",
            "99, 3000"
        })
        void ceilingDoublesEachAttempt(int attempt, long expectedCeiling) {
            ExponentialBackoff backoff = new ExponentialBackoff(100, 3000, 10);
            assertEquals(expectedCeiling, backoff.ceilingMs(attempt));
        }

        // --- A2: jittered delay is always in [0, ceiling] ---

        @Test
        @DisplayName("A2: delayMs is always in [0, ceiling] across 10,000 samples")
        void delayIsAlwaysWithinCeiling() {
            ExponentialBackoff backoff = new ExponentialBackoff(100, 3000, 10, new Random(42));
            for (int attempt = 0; attempt < 6; attempt++) {
                long ceiling = backoff.ceilingMs(attempt);
                for (int sample = 0; sample < 10_000; sample++) {
                    long delay = backoff.delayMs(attempt);
                    assertTrue(delay >= 0 && delay <= ceiling,
                            "attempt " + attempt + ": delay " + delay + " not in [0, " + ceiling + "]");
                }
            }
        }

        // --- A3: succeeds on first attempt (no retries needed) ---

        @Test
        @DisplayName("A3: Returns immediately on first success — no retries wasted")
        void succeedsOnFirstAttempt() {
            AtomicInteger calls = new AtomicInteger();
            ExponentialBackoff backoff = new ExponentialBackoff(1, 10, 5);

            String result = backoff.execute(() -> {
                calls.incrementAndGet();
                return "ok";
            });

            assertEquals("ok", result);
            assertEquals(1, calls.get(), "Must not retry after a success");
        }

        // --- A4: retries and succeeds on Nth attempt ---

        @Test
        @DisplayName("A4: Retries until success — result from winning attempt is returned")
        void retriesUntilSuccess() {
            AtomicInteger calls = new AtomicInteger();
            // Fail twice, then succeed on attempt 3
            ExponentialBackoff backoff = new ExponentialBackoff(1, 10, 5, new Random(0));

            String result = backoff.execute(() -> {
                int attempt = calls.incrementAndGet();
                if (attempt < 3) throw new RuntimeException("transient failure");
                return "recovered";
            });

            assertEquals("recovered", result);
            assertEquals(3, calls.get());
        }

        // --- A5: exhausts all attempts and throws RetryExhaustedException ---

        @Test
        @DisplayName("A5: Throws RetryExhaustedException after all attempts fail")
        void exhaustsAllAttempts() {
            AtomicInteger calls = new AtomicInteger();
            ExponentialBackoff backoff = new ExponentialBackoff(1, 10, 3, new Random(0));

            ExponentialBackoff.RetryExhaustedException ex = assertThrows(
                    ExponentialBackoff.RetryExhaustedException.class,
                    () -> backoff.execute(() -> {
                        calls.incrementAndGet();
                        throw new RuntimeException("always fails");
                    })
            );

            assertEquals(3, calls.get(), "Must attempt exactly maxAttempts times");
            assertNotNull(ex.getCause(), "Cause must be the last thrown exception");
            assertEquals("always fails", ex.getCause().getMessage());
        }

        // --- A6: jitter spreads delays — no two calls share the same delay ---

        @Test
        @DisplayName("A6: Jitter means delay values are not all identical (thundering herd prevention)")
        void jitterProducesVariedDelays() {
            // Seeded random — reproducible but varied
            ExponentialBackoff backoff = new ExponentialBackoff(100, 3000, 10, new Random(99));
            long attempt4Ceiling = backoff.ceilingMs(4);  // 1600ms

            List<Long> delays = new ArrayList<>();
            for (int i = 0; i < 50; i++) delays.add(backoff.delayMs(4));

            long distinct = delays.stream().distinct().count();
            // With full jitter over [0, 1600], 50 samples should almost never all be equal
            assertTrue(distinct > 10,
                    "Expected varied delays for thundering herd prevention, got " + distinct + " distinct values");
        }

        // --- A7: overflow safety — large attempt index doesn't throw ---

        @Test
        @DisplayName("A7: ceilingMs doesn't overflow on very large attempt index")
        void noOverflowOnLargeAttempt() {
            ExponentialBackoff backoff = new ExponentialBackoff(100, 30_000, 10);
            assertDoesNotThrow(() -> {
                long ceiling = backoff.ceilingMs(100);
                assertEquals(30_000, ceiling, "Must be capped at maxDelayMs");
            });
        }
    }

    // =========================================================================
    // B. CircuitBreaker
    // =========================================================================

    @Nested
    @DisplayName("B. CircuitBreaker")
    class CircuitBreakerTests {

        // Controllable clock so we don't actually sleep in tests
        private MutableClock clock;

        @BeforeEach
        void setUp() {
            clock = new MutableClock();
        }

        // --- B1: starts CLOSED ---

        @Test
        @DisplayName("B1: Circuit starts CLOSED and passes calls through")
        void startsClosed() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(3, 1, 5000, clock);
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());

            String result = cb.execute(() -> "hello");
            assertEquals("hello", result);
        }

        // --- B2: trips OPEN after failureThreshold consecutive failures ---

        @Test
        @DisplayName("B2: Trips OPEN after failureThreshold failures")
        void tripsOpenAfterThresholdFailures() {
            CircuitBreaker cb = new CircuitBreaker(3, 1, 5000, clock);

            // 2 failures — still CLOSED
            for (int i = 0; i < 2; i++) {
                assertThrows(RuntimeException.class,
                        () -> cb.execute(() -> { throw new RuntimeException("fail"); }));
            }
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());

            // 3rd failure — trips OPEN
            assertThrows(RuntimeException.class,
                    () -> cb.execute(() -> { throw new RuntimeException("fail"); }));
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());
        }

        // --- B3: OPEN rejects calls immediately (doesn't call the action) ---

        @Test
        @DisplayName("B3: OPEN circuit rejects calls without invoking the action")
        void openRejectsImmediately() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(2, 1, 5000, clock);
            AtomicInteger actionCalls = new AtomicInteger();

            // Trip the circuit
            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException("fail"); }); }
                catch (Exception ignored) {}
            }
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());

            // All further calls throw CircuitOpenException, action never invoked
            for (int i = 0; i < 5; i++) {
                assertThrows(CircuitBreaker.CircuitOpenException.class,
                        () -> cb.execute(() -> { actionCalls.incrementAndGet(); return "x"; }));
            }
            assertEquals(0, actionCalls.get(), "Action must not be called while circuit is OPEN");
        }

        // --- B4: transitions OPEN → HALF_OPEN after timeout ---

        @Test
        @DisplayName("B4: Transitions OPEN → HALF_OPEN after openTimeoutMs elapses")
        void transitionsToHalfOpenAfterTimeout() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(2, 1, 5000, clock);

            // Trip open
            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException(); }); }
                catch (Exception ignored) {}
            }
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());

            // Advance clock past timeout
            clock.advanceMs(5001);

            // Next call transitions to HALF_OPEN and the probe is executed
            AtomicInteger probeCount = new AtomicInteger();
            cb.execute(() -> { probeCount.incrementAndGet(); return "probe"; });

            // Should have closed after the successful probe
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
            assertEquals(1, probeCount.get(), "Exactly one probe call allowed in HALF_OPEN");
        }

        // --- B5: failed probe in HALF_OPEN reopens the circuit ---

        @Test
        @DisplayName("B5: Failed probe in HALF_OPEN reopens the circuit")
        void failedProbeReopens() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(2, 1, 5000, clock);

            // Trip open
            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException(); }); }
                catch (Exception ignored) {}
            }

            // Advance past timeout → HALF_OPEN
            clock.advanceMs(5001);

            // Probe fails
            assertThrows(RuntimeException.class,
                    () -> cb.execute(() -> { throw new RuntimeException("probe fail"); }));

            // Back to OPEN
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());
        }

        // --- B6: a single success resets the failure count ---

        @Test
        @DisplayName("B6: A success resets the failure count — must fail again from zero")
        void successResetsFailureCount() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(3, 1, 5000, clock);

            // 2 failures (one shy of tripping)
            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException(); }); }
                catch (Exception ignored) {}
            }
            assertEquals(2, cb.getFailureCount());

            // One success — resets counter
            cb.execute(() -> "ok");
            assertEquals(0, cb.getFailureCount());
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
        }

        // --- B7: OPEN circuit still rejects before timeout ---

        @Test
        @DisplayName("B7: Circuit stays OPEN until full timeout has elapsed")
        void staysOpenUntilTimeout() throws Exception {
            CircuitBreaker cb = new CircuitBreaker(2, 1, 5000, clock);

            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException(); }); }
                catch (Exception ignored) {}
            }

            // Advance but NOT past the full timeout
            clock.advanceMs(4999);
            assertThrows(CircuitBreaker.CircuitOpenException.class,
                    () -> cb.execute(() -> "too soon"));
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());
        }
    }

    // =========================================================================
    // C. Fallback
    // =========================================================================

    @Nested
    @DisplayName("C. Fallback")
    class FallbackTests {

        // --- C1: primary succeeds → fallbacks never called ---

        @Test
        @DisplayName("C1: Primary success — fallback suppliers are not invoked")
        void primarySuccessSkipsFallbacks() throws Exception {
            AtomicInteger fallbackCalls = new AtomicInteger();

            String result = Fallback.<String>tryFirst(() -> "primary")
                    .then(() -> { fallbackCalls.incrementAndGet(); return "fallback"; })
                    .execute();

            assertEquals("primary", result);
            assertEquals(0, fallbackCalls.get());
        }

        // --- C2: falls through to first fallback on primary failure ---

        @Test
        @DisplayName("C2: Primary fails → first fallback is returned")
        void fallsToFirstFallback() throws Exception {
            String result = Fallback.<String>tryFirst(() -> { throw new RuntimeException("down"); })
                    .then(() -> "secondary")
                    .then(() -> "cache")
                    .execute();

            assertEquals("secondary", result, "Must use first available fallback");
        }

        // --- C3: chains through multiple failing fallbacks ---

        @Test
        @DisplayName("C3: Chains through failing fallbacks until one succeeds")
        void chainsThroughMultipleFallbacks() throws Exception {
            AtomicInteger attempts = new AtomicInteger();

            String result = Fallback.<String>tryFirst(() -> { attempts.incrementAndGet(); throw new RuntimeException("primary"); })
                    .then(() -> { attempts.incrementAndGet(); throw new RuntimeException("secondary"); })
                    .then(() -> { attempts.incrementAndGet(); throw new RuntimeException("cache"); })
                    .then(() -> { attempts.incrementAndGet(); return "static-default"; })
                    .execute();

            assertEquals("static-default", result);
            assertEquals(4, attempts.get(), "All layers tried before default");
        }

        // --- C4: throws last exception when all fallbacks fail ---

        @Test
        @DisplayName("C4: Throws last exception when every fallback fails")
        void throwsWhenAllFail() {
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> Fallback.<String>tryFirst(() -> { throw new RuntimeException("layer1"); })
                            .then(() -> { throw new RuntimeException("layer2"); })
                            .then(() -> { throw new RuntimeException("layer3"); })
                            .execute());

            assertEquals("layer3", ex.getMessage(), "Must re-throw the last exception");
        }

        // --- C5: .on(ExceptionType) only triggers for matching exception ---

        @Test
        @DisplayName("C5: .on(ExceptionType) only triggers for the specified exception type")
        void onlyTriggersForMatchingException() throws Exception {
            // Fallback is only registered for IllegalStateException
            // Primary throws IllegalArgumentException → should NOT match → propagates
            assertThrows(IllegalArgumentException.class,
                    () -> Fallback.<String>tryFirst(() -> { throw new IllegalArgumentException("wrong type"); })
                            .on(IllegalStateException.class, () -> "wrong fallback")
                            .execute());

            // Primary throws IllegalStateException → should match
            String result = Fallback.<String>tryFirst(() -> { throw new IllegalStateException("right type"); })
                    .on(IllegalStateException.class, () -> "correct fallback")
                    .execute();

            assertEquals("correct fallback", result);
        }

        // --- C6: executeOptional returns empty on total failure ---

        @Test
        @DisplayName("C6: executeOptional returns empty Optional when all fallbacks fail")
        void executeOptionalReturnsEmptyOnFailure() {
            Optional<String> result = Fallback.<String>tryFirst(() -> { throw new RuntimeException(); })
                    .then(() -> { throw new RuntimeException(); })
                    .executeOptional();

            assertTrue(result.isEmpty(), "Must return Optional.empty() when all fail");
        }

        // --- C7: withCache returns stale value when primary fails ---

        @Test
        @DisplayName("C7: withCache serves stale cached value when primary is down")
        void cacheServesStaleDuringOutage() {
            AtomicInteger liveCallCount = new AtomicInteger();
            boolean[] primaryDown = {false};

            Supplier<String> resilient = Fallback.withCache(() -> {
                liveCallCount.incrementAndGet();
                if (primaryDown[0]) throw new RuntimeException("service down");
                return "live-price-100";
            });

            // First call — primary is up, cache gets populated
            assertEquals("live-price-100", resilient.get());
            assertEquals(1, liveCallCount.get());

            // Primary goes down
            primaryDown[0] = true;

            // Subsequent calls return cached value
            assertEquals("live-price-100", resilient.get(), "Must return stale cached value");
            assertEquals("live-price-100", resilient.get(), "Must keep returning cache");
            assertEquals(3, liveCallCount.get(), "Primary is still attempted each time");
        }

        // --- C8: withCache throws when primary fails and nothing is cached ---

        @Test
        @DisplayName("C8: withCache throws when primary fails and cache is empty")
        void cacheThrowsWhenNothingCached() {
            Supplier<String> resilient = Fallback.withCache(() -> {
                throw new RuntimeException("first call failed");
            });

            assertThrows(RuntimeException.class, resilient::get,
                    "Must propagate exception when cache is empty");
        }
    }

    // =========================================================================
    // D. Combined: Fallback + CircuitBreaker + Exponential Backoff
    // =========================================================================

    @Nested
    @DisplayName("D. Combined patterns")
    class CombinedTests {

        /**
         * REAL-WORLD SCENARIO: Price lookup service
         *
         * Architecture:
         *   ExponentialBackoff wraps the live API call → retries transient errors
         *   CircuitBreaker wraps the backoff call → trips open on sustained failure
         *   Fallback wraps the circuit breaker → serves cache when circuit is open
         *
         *   [Caller]
         *      → Fallback
         *          → CircuitBreaker
         *              → ExponentialBackoff
         *                  → Live API
         *          → (on CircuitOpenException) Cache
         *          → (on any other failure) Static default "N/A"
         */

        @Test
        @DisplayName("D1: Happy path — live API responds, no fallback needed")
        void happyPath() throws Exception {
            ExponentialBackoff backoff = new ExponentialBackoff(1, 10, 3);
            CircuitBreaker cb = new CircuitBreaker(5, 1, 1000);

            String price = Fallback.<String>tryFirst(() ->
                            cb.execute(() -> backoff.execute(() -> "105.00")))
                    .then(() -> "CACHED")
                    .then(() -> "N/A")
                    .execute();

            assertEquals("105.00", price);
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
        }

        @Test
        @DisplayName("D2: Transient error — backoff retries succeed before circuit trips")
        void transientErrorRetriedByBackoff() throws Exception {
            AtomicInteger apiCalls = new AtomicInteger();
            // Fail twice, succeed on 3rd
            ExponentialBackoff backoff = new ExponentialBackoff(1, 10, 5, new Random(0));
            CircuitBreaker cb = new CircuitBreaker(5, 1, 1000);

            String price = Fallback.<String>tryFirst(() ->
                            cb.execute(() -> backoff.execute(() -> {
                                int n = apiCalls.incrementAndGet();
                                if (n < 3) throw new RuntimeException("timeout");
                                return "103.50";
                            })))
                    .then(() -> "CACHED")
                    .execute();

            assertEquals("103.50", price);
            // Circuit saw one "call" that eventually succeeded — stays CLOSED
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
            assertEquals(3, apiCalls.get(), "API attempted 3 times via backoff");
        }

        @Test
        @DisplayName("D3: Sustained failure trips circuit — fallback serves cache")
        void sustainedFailureTripsToCachedFallback() throws Exception {
            MutableClock clock = new MutableClock();
            ExponentialBackoff backoff = new ExponentialBackoff(1, 5, 2, new Random(0));
            CircuitBreaker cb = new CircuitBreaker(3, 1, 5000, clock);

            // Simulate 3 fully exhausted backoff calls → trips the circuit
            for (int i = 0; i < 3; i++) {
                try {
                    cb.execute(() -> backoff.execute(() -> { throw new RuntimeException("down"); }));
                } catch (Exception ignored) {}
            }
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());

            // Now a normal request: circuit is open → Fallback catches CircuitOpenException → cache
            String price = Fallback.<String>tryFirst(() ->
                            cb.execute(() -> backoff.execute(() -> { throw new RuntimeException("still down"); })))
                    .on(CircuitBreaker.CircuitOpenException.class, () -> "CACHED-100.00")
                    .then(() -> "N/A")
                    .execute();

            assertEquals("CACHED-100.00", price, "Must return cached value when circuit is open");
        }

        @Test
        @DisplayName("D4: Circuit recovers — after timeout, probe succeeds and circuit closes")
        void circuitRecovery() throws Exception {
            MutableClock clock = new MutableClock();
            boolean[] serviceDown = {true};

            CircuitBreaker cb = new CircuitBreaker(2, 1, 5000, clock);

            // Trip the circuit
            for (int i = 0; i < 2; i++) {
                try { cb.execute(() -> { throw new RuntimeException(); }); }
                catch (Exception ignored) {}
            }
            assertEquals(CircuitBreaker.State.OPEN, cb.getState());

            // Service recovers
            serviceDown[0] = false;

            // Advance clock past timeout
            clock.advanceMs(5001);

            // Request with fallback — probe succeeds, circuit closes
            String result = Fallback.<String>tryFirst(() ->
                            cb.execute(() -> serviceDown[0] ? "fallback" : "live-data"))
                    .on(CircuitBreaker.CircuitOpenException.class, () -> "CACHED")
                    .execute();

            assertEquals("live-data", result, "Probe should succeed and return live data");
            assertEquals(CircuitBreaker.State.CLOSED, cb.getState(), "Circuit must close after successful probe");
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * A Clock whose time you can advance manually in tests.
     * This lets us test time-dependent behaviour (circuit timeouts) without sleeping.
     */
    static class MutableClock extends Clock {
        private long nowMs = Instant.now().toEpochMilli();

        public void advanceMs(long ms) { nowMs += ms; }

        @Override public ZoneId getZone()              { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId zone)   { return this; }
        @Override public Instant instant()             { return Instant.ofEpochMilli(nowMs); }
        @Override public long millis()                 { return nowMs; }
    }
}
