package dev.alexkzk.resilience;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * FALLBACK PATTERN
 * ────────────────
 * When a primary operation fails, try progressively cheaper/degraded alternatives
 * rather than propagating the error to the caller.
 *
 * LAYERED FALLBACK CHAIN
 * ──────────────────────
 *
 *   ┌─────────────────────────────────────────────────────────────┐
 *   │  Primary call                                               │
 *   │     → fails?                                                │
 *   │        → Fallback 1 (e.g. secondary service)               │
 *   │             → fails?                                        │
 *   │                → Fallback 2 (e.g. local cache)             │
 *   │                     → fails?                               │
 *   │                        → Fallback 3 (e.g. static default)  │
 *   └─────────────────────────────────────────────────────────────┘
 *
 * COMMON FALLBACK STRATEGIES
 * ──────────────────────────
 *   1. Static default    — return a safe constant (empty list, 0, "UNKNOWN")
 *   2. Cached value      — return the last known-good result
 *   3. Degraded service  — call a simpler/cheaper endpoint
 *   4. Fail fast         — re-throw immediately (when partial results are worse than none)
 *
 * Uses Callable<T> (not Supplier<T>) so that each layer can throw checked exceptions.
 * This is critical for composing with CircuitBreaker.execute() which throws Exception.
 *
 *   String price = Fallback.<String>tryFirst(() -> cb.execute(() -> liveApi.getPrice("AAPL")))
 *       .on(CircuitBreaker.CircuitOpenException.class, () -> cache.getPrice("AAPL"))
 *       .then(() -> "N/A")
 *       .execute();
 */
public class Fallback<T> {

    /**
     * One entry in the fallback chain.
     * predicate: which exception types this entry handles (null = always try)
     * callable:  what to call when predicate matches
     */
    private record Entry<T>(Predicate<Exception> predicate, Callable<T> callable) {}

    private final List<Entry<T>> chain = new ArrayList<>();

    private Fallback() {}

    // -----------------------------------------------------------------------
    // Fluent builder API
    // -----------------------------------------------------------------------

    /** Start a new fallback chain with a primary callable. */
    public static <T> Fallback<T> tryFirst(Callable<T> primary) {
        Fallback<T> fb = new Fallback<>();
        fb.chain.add(new Entry<>(null, primary));
        return fb;
    }

    /** Append a fallback that runs on ANY exception from the previous step. */
    public Fallback<T> then(Callable<T> fallback) {
        chain.add(new Entry<>(e -> true, fallback));
        return this;
    }

    /**
     * Append a fallback that runs only when a specific exception type is thrown.
     * Other exception types fall through to later entries.
     */
    public <E extends Exception> Fallback<T> on(Class<E> exceptionType, Callable<T> fallback) {
        chain.add(new Entry<>(e -> exceptionType.isInstance(e), fallback));
        return this;
    }

    /** Append a fallback with a custom predicate (e.g. check exception message). */
    public Fallback<T> onIf(Predicate<Exception> condition, Callable<T> fallback) {
        chain.add(new Entry<>(condition, fallback));
        return this;
    }

    // -----------------------------------------------------------------------
    // Execute
    // -----------------------------------------------------------------------

    /**
     * Run the chain from the beginning.
     * Each step is tried; on exception, the next matching entry is tried.
     * If no entry succeeds, the last exception is re-thrown.
     */
    public T execute() throws Exception {
        Exception lastException = null;

        for (Entry<T> entry : chain) {
            // First entry has null predicate — always try it.
            // Subsequent entries check their predicate against the last exception.
            if (lastException == null || entry.predicate().test(lastException)) {
                try {
                    return entry.callable().call();
                } catch (Exception e) {
                    lastException = e;
                }
            }
        }

        if (lastException != null) throw lastException;
        throw new IllegalStateException("Fallback chain is empty");
    }

    /**
     * Like execute() but wraps the result in Optional and never throws.
     * Returns Optional.empty() on total failure.
     */
    public Optional<T> executeOptional() {
        try {
            return Optional.ofNullable(execute());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // -----------------------------------------------------------------------
    // Cache-backed fallback helper
    // -----------------------------------------------------------------------

    /**
     * Wraps a supplier so that on success the value is cached, and on failure
     * the last cached value is returned (if one exists).
     *
     * This is the "stale-cache" strategy: better to serve slightly old data
     * than no data at all.
     *
     *   var priceSupplier = Fallback.withCache(() -> liveApi.getPrice("AAPL"));
     *   String price = priceSupplier.get();  // always returns *something*
     */
    public static <T> Supplier<T> withCache(Supplier<T> primary) {
        @SuppressWarnings("unchecked")
        T[] cached = (T[]) new Object[1];

        return () -> {
            try {
                T value = primary.get();
                cached[0] = value;
                return value;
            } catch (Exception e) {
                if (cached[0] != null) return cached[0];
                throw e;
            }
        };
    }
}
