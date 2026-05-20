package dev.alexkzk.doselect.ordergateway;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentOrderProcessor {

    private final long maxBurstCapacity = 100;
    private final long tokensPerSecond = 50;
    private final long nanoSecondsPerToken = 1_000_000_000L / tokensPerSecond;

    private final AtomicLong availableTokens = new AtomicLong(maxBurstCapacity);
    private final AtomicLong lastRefillTime = new AtomicLong(System.nanoTime());
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Processes an incoming order if rate limits allow.
     *
     * @param sku      The item identity string
     * @param quantity The quantity requested
     * @return true if the order was processed, false if rate-limited (shed)
     * @throws InvalidOrderException if data validations fail
     */
    public boolean processOrder(String sku, int quantity) throws InvalidOrderException {
        if (sku == null || sku.isEmpty()) {
            throw new InvalidOrderException("SKU must not be empty");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Quantity must be positive, got: " + quantity);
        }

        // Refill and consume are both performed under the lock so they are one atomic unit.
        // The lock is acquired per-order but held only for nanoseconds (pure arithmetic),
        // so it does not become a throughput bottleneck under concurrent load.
        lock.lock();
        try {
            long now = System.nanoTime();
            long elapsed = now - lastRefillTime.get();
            if (elapsed > 0) {
                long newTokens = elapsed / nanoSecondsPerToken;
                if (newTokens > 0) {
                    long refilled = Math.min(availableTokens.get() + newTokens, maxBurstCapacity);
                    availableTokens.set(refilled);
                    // Advance clock only by minted tokens to preserve sub-token remainder.
                    lastRefillTime.addAndGet(newTokens * nanoSecondsPerToken);
                }
            }
            if (availableTokens.get() <= 0) {
                return false;
            }
            availableTokens.decrementAndGet();
            return true;
        } finally {
            lock.unlock();
        }
    }
}