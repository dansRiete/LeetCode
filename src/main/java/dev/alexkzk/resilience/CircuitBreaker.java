package dev.alexkzk.resilience;

import java.time.Clock;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CIRCUIT BREAKER
 * ───────────────
 * Named after the electrical component: when too much current flows (too many
 * failures), the breaker "trips" (opens) to protect the circuit. After a
 * cooldown it allows a single test current through (half-open probe); if the
 * circuit is repaired, it closes again.
 *
 * STATE MACHINE
 * ─────────────
 *
 *   ┌──────────────────────────────────────────────────────────────────┐
 *   │                                                                  │
 *   │   CLOSED ──(failures ≥ threshold)──► OPEN                       │
 *   │      ▲                                  │                        │
 *   │      │                          (timeout elapsed)                │
 *   │      │                                  ▼                        │
 *   │      └──(probe succeeds)────── HALF_OPEN                        │
 *   │                                    │                             │
 *   │                            (probe fails)                         │
 *   │                                    │                             │
 *   │                                    └──────────────► OPEN         │
 *   └──────────────────────────────────────────────────────────────────┘
 *
 * CLOSED    — Normal operation. Failures are counted. When the failure count
 *             reaches failureThreshold within the rolling window, trip to OPEN.
 *
 * OPEN      — All calls are rejected immediately with CircuitOpenException.
 *             No calls reach the downstream service. After openTimeoutMs elapses,
 *             transition to HALF_OPEN.
 *
 * HALF_OPEN — One probe call is allowed through. If it succeeds, reset failure
 *             count and close the circuit. If it fails, reopen immediately.
 *             Concurrent calls during HALF_OPEN are rejected (only one probe).
 *
 * PARAMETERS
 * ──────────
 *   failureThreshold  — consecutive failures needed to trip open        (e.g. 5)
 *   successThreshold  — consecutive successes in HALF_OPEN to close     (e.g. 1)
 *   openTimeoutMs     — how long to stay OPEN before trying HALF_OPEN   (e.g. 5000)
 *
 * THREAD SAFETY
 * ─────────────
 * State is held in AtomicReference<State> + AtomicInteger counters.
 * State transitions use compareAndSet to prevent races between threads.
 */
public class CircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int  failureThreshold;
    private final int  successThreshold;
    private final long openTimeoutMs;
    private final Clock clock;

    // Current state
    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);

    // Failure counter (resets on success or transition to CLOSED)
    private final AtomicInteger failureCount  = new AtomicInteger(0);
    // Success counter in HALF_OPEN (resets on state change)
    private final AtomicInteger successCount  = new AtomicInteger(0);
    // Timestamp when the circuit was opened (ms since epoch)
    private final AtomicLong    openedAt      = new AtomicLong(0);

    public CircuitBreaker(int failureThreshold, int successThreshold, long openTimeoutMs, Clock clock) {
        this.failureThreshold = failureThreshold;
        this.successThreshold = successThreshold;
        this.openTimeoutMs    = openTimeoutMs;
        this.clock            = clock;
    }

    public CircuitBreaker(int failureThreshold, int successThreshold, long openTimeoutMs) {
        this(failureThreshold, successThreshold, openTimeoutMs, Clock.systemUTC());
    }

    // -----------------------------------------------------------------------
    // Execute
    // -----------------------------------------------------------------------

    /**
     * Attempts to call {@code action} through the circuit breaker.
     *
     * @throws CircuitOpenException  if the circuit is OPEN (call not attempted)
     * @throws Exception             if the action itself throws (failure recorded)
     */
    // Callable allows the action to throw checked exceptions (unlike Supplier).
    public <T> T execute(Callable<T> action) throws Exception {
        checkAndTransition();

        State current = state.get();

        if (current == State.OPEN) {
            throw new CircuitOpenException("Circuit is OPEN — call rejected");
        }

        try {
            T result = action.call();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    // -----------------------------------------------------------------------
    // Internal state transitions
    // -----------------------------------------------------------------------

    /**
     * Called before each execution attempt.
     * If OPEN and timeout has elapsed, transitions to HALF_OPEN.
     */
    private void checkAndTransition() {
        if (state.get() == State.OPEN) {
            long elapsed = clock.millis() - openedAt.get();
            if (elapsed >= openTimeoutMs) {
                // Race: multiple threads may reach here simultaneously.
                // Only the one that wins the CAS transitions to HALF_OPEN.
                if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    successCount.set(0);
                }
            }
        }
    }

    private void onSuccess() {
        State current = state.get();
        if (current == State.HALF_OPEN) {
            int successes = successCount.incrementAndGet();
            if (successes >= successThreshold) {
                transitionToClosed();
            }
        } else if (current == State.CLOSED) {
            // Reset rolling failure count on any success
            failureCount.set(0);
        }
    }

    private void onFailure() {
        State current = state.get();
        if (current == State.HALF_OPEN) {
            // Probe failed — reopen immediately
            transitionToOpen();
        } else if (current == State.CLOSED) {
            int failures = failureCount.incrementAndGet();
            if (failures >= failureThreshold) {
                transitionToOpen();
            }
        }
    }

    private void transitionToOpen() {
        if (state.compareAndSet(State.CLOSED, State.OPEN)
                || state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
            openedAt.set(clock.millis());
            failureCount.set(0);
            successCount.set(0);
        }
    }

    private void transitionToClosed() {
        if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
            failureCount.set(0);
            successCount.set(0);
        }
    }

    // -----------------------------------------------------------------------
    // Inspection
    // -----------------------------------------------------------------------

    public State  getState()        { return state.get(); }
    public int    getFailureCount() { return failureCount.get(); }

    // -----------------------------------------------------------------------
    // Exceptions
    // -----------------------------------------------------------------------

    public static class CircuitOpenException extends RuntimeException {
        public CircuitOpenException(String message) { super(message); }
    }
}
