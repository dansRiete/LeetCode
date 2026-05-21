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
            throw new InvalidOrderException("SKU is null or empty");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Quantity must be greater than 0");
        }

        lock.lock();
        try {
            long elapsed = System.nanoTime() - lastRefillTime.get();
            long refill = elapsed / nanoSecondsPerToken;
            availableTokens.set(Math.min(availableTokens.get() + refill, maxBurstCapacity));
            lastRefillTime.addAndGet(refill * nanoSecondsPerToken);
            if (availableTokens.get() <= 0) {
                return false;
            } else {
                availableTokens.decrementAndGet();
            }
        } finally {
            lock.unlock();
        }

        return true;
    }
}