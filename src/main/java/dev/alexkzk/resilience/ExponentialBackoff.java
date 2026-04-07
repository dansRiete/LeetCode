package dev.alexkzk.resilience;

import java.util.Random;
import java.util.function.Supplier;

/**
 * EXPONENTIAL BACKOFF
 * ───────────────────
 * When a remote call fails, waiting a fixed delay before retrying causes two
 * problems:
 *   1. Under load, many clients retry at the same moment → thundering herd.
 *   2. Short fixed delay → floods a struggling service; long delay → wastes time.
 *
 * Exponential backoff solves (2): each retry waits 2× longer than the last.
 * Full jitter solves (1): each wait is a random fraction of the computed ceiling.
 *
 * DELAY FORMULA
 * ─────────────
 *   ceiling  = min(maxDelayMs, baseDelayMs × 2^attempt)
 *   actual   = random(0, ceiling)          ← "full jitter" variant
 *
 * Timeline for base=100ms, max=3000ms, 5 attempts:
 *
 *   attempt 0  ceiling=  100ms   actual ∈ [0, 100]
 *   attempt 1  ceiling=  200ms   actual ∈ [0, 200]
 *   attempt 2  ceiling=  400ms   actual ∈ [0, 400]
 *   attempt 3  ceiling=  800ms   actual ∈ [0, 800]
 *   attempt 4  ceiling= 1600ms   actual ∈ [0,1600]
 *   attempt 5  ceiling= 3000ms   actual ∈ [0,3000]  ← capped at max
 *
 * Without jitter all callers share the same ceiling → they still spike together.
 * With full jitter they spread uniformly across [0, ceiling] → smooth load.
 */
public class ExponentialBackoff {

    private final long   baseDelayMs;
    private final long   maxDelayMs;
    private final int    maxAttempts;
    private final Random random;

    // Injecting Random lets tests pass a seeded instance for determinism.
    public ExponentialBackoff(long baseDelayMs, long maxDelayMs, int maxAttempts, Random random) {
        if (baseDelayMs <= 0) throw new IllegalArgumentException("baseDelayMs must be > 0");
        if (maxDelayMs < baseDelayMs) throw new IllegalArgumentException("maxDelayMs must be >= baseDelayMs");
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts must be >= 1");
        this.baseDelayMs = baseDelayMs;
        this.maxDelayMs  = maxDelayMs;
        this.maxAttempts = maxAttempts;
        this.random      = random;
    }

    public ExponentialBackoff(long baseDelayMs, long maxDelayMs, int maxAttempts) {
        this(baseDelayMs, maxDelayMs, maxAttempts, new Random());
    }

    // -----------------------------------------------------------------------
    // Core: compute delay for a given attempt index (0-based)
    // -----------------------------------------------------------------------

    /**
     * Returns the delay ceiling for attempt {@code n} (before jitter).
     * Safe against overflow: shifts cap at 62 bits.
     */
    public long ceilingMs(int attempt) {
        // 2^attempt can overflow int/long; cap the shift at 62 to stay safe.
        long shifted = (attempt < 62)
                ? baseDelayMs * (1L << attempt)   // baseDelay × 2^attempt
                : Long.MAX_VALUE;
        return Math.min(maxDelayMs, shifted);
    }

    /**
     * Returns the actual delay with full jitter applied.
     * Result is in [0, ceilingMs(attempt)].
     */
    public long delayMs(int attempt) {
        long ceiling = ceilingMs(attempt);
        return (long) (random.nextDouble() * ceiling);
    }

    // -----------------------------------------------------------------------
    // Execute: run a Supplier with automatic retry + backoff
    // -----------------------------------------------------------------------

    /**
     * Executes {@code action} up to maxAttempts times.
     * Sleeps between attempts using the computed backoff delay.
     *
     * @throws RetryExhaustedException wrapping the last exception if all attempts fail.
     */
    public <T> T execute(Supplier<T> action) {
        Exception lastException = null;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                return action.get();
            } catch (Exception e) {
                lastException = e;

                // Don't sleep after the final attempt
                if (attempt < maxAttempts - 1) {
                    long delay = delayMs(attempt);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RetryExhaustedException("Interrupted during backoff", ie);
                    }
                }
            }
        }

        throw new RetryExhaustedException(
                "All " + maxAttempts + " attempts failed", lastException);
    }

    public int getMaxAttempts() { return maxAttempts; }
    public long getBaseDelayMs() { return baseDelayMs; }
    public long getMaxDelayMs()  { return maxDelayMs; }

    // -----------------------------------------------------------------------
    // Checked exception that carries attempt count and root cause
    // -----------------------------------------------------------------------

    // RuntimeException so it can propagate through Supplier/Callable lambdas
    // without polluting every call site with a checked throws clause.
    public static class RetryExhaustedException extends RuntimeException {
        public RetryExhaustedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
