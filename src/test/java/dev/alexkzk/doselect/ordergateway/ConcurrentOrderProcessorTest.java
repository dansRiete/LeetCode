package dev.alexkzk.doselect.ordergateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentOrderProcessorTest {

    private ConcurrentOrderProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ConcurrentOrderProcessor();
    }

    // --- Validation ---

    @Test
    void throwsOnNullSku() {
        assertThrows(InvalidOrderException.class, () -> processor.processOrder(null, 1));
    }

    @Test
    void throwsOnEmptySku() {
        assertThrows(InvalidOrderException.class, () -> processor.processOrder("", 1));
    }

    @Test
    void throwsOnZeroQuantity() {
        assertThrows(InvalidOrderException.class, () -> processor.processOrder("SKU-1", 0));
    }

    @Test
    void throwsOnNegativeQuantity() {
        assertThrows(InvalidOrderException.class, () -> processor.processOrder("SKU-1", -5));
    }

    // --- Basic rate limiting ---

    @Test
    void firstOrderIsAccepted() throws InvalidOrderException {
        assertTrue(processor.processOrder("SKU-A", 1));
    }

    @Test
    void exhaustsBurstCapacityAt100() throws InvalidOrderException {
        for (int i = 0; i < 100; i++) {
            assertTrue(processor.processOrder("SKU-X", 1), "order " + i + " should be accepted");
        }
        assertFalse(processor.processOrder("SKU-X", 1));
    }

    @Test
    void tokensReplenishOverTime() throws Exception {
        for (int i = 0; i < 100; i++) {
            processor.processOrder("SKU-R", 1);
        }
        assertFalse(processor.processOrder("SKU-R", 1), "bucket must be empty");

        // At 50 tokens/sec, 100 ms should yield ~5 new tokens.
        Thread.sleep(100);

        assertTrue(processor.processOrder("SKU-R", 2), "at least one token must have been replenished");
    }

    // --- Concurrency correctness ---

    @Test
    void tokenBalanceNeverDropsBelowZero() throws InterruptedException {
        int threadCount = 200;
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger accepted = new AtomicInteger();
        AtomicInteger shed = new AtomicInteger();

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    if (processor.processOrder("SKU-T", 1)) {
                        accepted.incrementAndGet();
                    } else {
                        shed.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Unexpected exception: " + e.getMessage());
                }
            });
            threads[i].start();
        }

        ready.await();
        start.countDown();
        for (Thread t : threads) t.join();

        // Exactly 100 orders should have been accepted (burst capacity) — no more.
        assertEquals(100, accepted.get(),
                "Accepted count must equal burst capacity; actual=" + accepted.get());
        assertEquals(threadCount - 100, shed.get());
    }

    @Test
    void noDeadlockOrExceptionUnder500ConcurrentThreads() throws InterruptedException {
        int threadCount = 500;
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        AtomicInteger errors = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    processor.processOrder("SKU-C", 1);
                } catch (InvalidOrderException e) {
                    errors.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
            t.setDaemon(true);
            t.start();
        }

        ready.await();
        start.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS), "All 500 threads must complete within 5 seconds (deadlock check)");
        assertEquals(0, errors.get(), "No InvalidOrderException should be thrown for valid input");
    }
}