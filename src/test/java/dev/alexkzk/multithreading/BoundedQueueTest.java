package dev.alexkzk.multithreading;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BoundedQueueTest {

    // -----------------------------------------------------------------------
    // Basic correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("put then take returns the same item")
    void putThenTake() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);
        q.put("hello");
        assertEquals("hello", q.take());
    }

    @Test
    @DisplayName("take returns items in FIFO order")
    void fifoOrder() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);
        q.put("A");
        q.put("B");
        q.put("C");
        assertEquals("A", q.take());
        assertEquals("B", q.take());
        assertEquals("C", q.take());
    }

    @Test
    @DisplayName("size tracks correctly after puts and takes")
    void sizeTracking() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);
        assertEquals(0, q.size());
        q.put("A");
        q.put("B");
        assertEquals(2, q.size());
        q.take();
        assertEquals(1, q.size());
    }

    // -----------------------------------------------------------------------
    // Blocking behaviour
    // -----------------------------------------------------------------------

    /**
     * Producer blocks when queue is full.
     *
     * We fill the queue to capacity, then start a producer thread that tries
     * to put one more item. It must block (not proceed) until a consumer
     * takes an item and frees a slot.
     */
    @Test
    @DisplayName("producer blocks when queue is full, unblocks when consumer takes")
    void producerBlocksWhenFull() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(2);
        q.put("A");
        q.put("B");   // queue now full

        CountDownLatch producerStarted = new CountDownLatch(1);
        CountDownLatch producerDone    = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            producerStarted.countDown();
            try {
                q.put("C");       // must block here — queue is full
                producerDone.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        producerStarted.await();

        // Give producer time to reach wait() inside put()
        Thread.sleep(50);

        // Producer must still be blocked — it has not counted down producerDone
        assertEquals(1, producerDone.getCount(), "Producer must be blocked while queue is full");
        assertTrue(q.isFull());

        // Consumer takes one item — frees a slot — producer unblocks
        q.take();
        assertTrue(producerDone.await(1, TimeUnit.SECONDS), "Producer must unblock after take()");
        assertEquals(2, q.size());   // "B" + "C"
    }

    /**
     * Consumer blocks when queue is empty.
     *
     * We start a consumer on an empty queue. It must block until a producer
     * puts an item.
     */
    @Test
    @DisplayName("consumer blocks when queue is empty, unblocks when producer puts")
    void consumerBlocksWhenEmpty() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);

        CountDownLatch consumerStarted = new CountDownLatch(1);
        List<String> received = new ArrayList<>();

        Thread consumer = new Thread(() -> {
            consumerStarted.countDown();
            try {
                received.add(q.take());   // must block — queue empty
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        consumerStarted.await();

        Thread.sleep(50);
        assertTrue(received.isEmpty(), "Consumer must be blocked while queue is empty");

        // Producer adds an item — consumer unblocks
        q.put("hello");
        consumer.join(1000);
        assertEquals(List.of("hello"), received);
    }

    // -----------------------------------------------------------------------
    // Concurrent producers and consumers
    // -----------------------------------------------------------------------

    /**
     * 4 producers × 25 items = 100 items total.
     * 4 consumers each take 25 items.
     * All 100 items must be received, no item lost, no deadlock.
     */
    @Test
    @DisplayName("100 items flow correctly through 4 producers and 4 consumers")
    void concurrentProducersAndConsumers() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);   // small limit forces frequent blocking
        int producerCount = 4;
        int itemsEach     = 25;

        CopyOnWriteArrayList<String> received = new CopyOnWriteArrayList<>();
        CountDownLatch allDone = new CountDownLatch(producerCount + producerCount);

        // Producers
        for (int p = 0; p < producerCount; p++) {
            final int id = p;
            new Thread(() -> {
                for (int i = 0; i < itemsEach; i++) {
                    try { q.put("p" + id + "-" + i); }
                    catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                allDone.countDown();
            }).start();
        }

        // Consumers
        for (int c = 0; c < producerCount; c++) {
            new Thread(() -> {
                for (int i = 0; i < itemsEach; i++) {
                    try { received.add(q.take()); }
                    catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                allDone.countDown();
            }).start();
        }

        assertTrue(allDone.await(5, TimeUnit.SECONDS), "Must finish within 5 seconds — no deadlock");
        assertEquals(producerCount * itemsEach, received.size(), "All items must be received");
        assertEquals(0, q.size(), "Queue must be empty when all consumers are done");
    }

    // -----------------------------------------------------------------------
    // Monitor — same lock on put() and take()
    // -----------------------------------------------------------------------

    /**
     * Proves both methods share the same monitor.
     *
     * While a thread is inside put() (holding the monitor), a second thread
     * trying to call take() must be blocked — not running concurrently.
     * We use a flag to detect whether take() started while put() was running.
     */
    @Test
    @DisplayName("put() and take() share the same monitor — cannot run concurrently")
    void sameMonitorBlocksConcurrentAccess() throws InterruptedException {
        BoundedQueue q = new BoundedQueue(5);
        AtomicInteger concurrentAccess = new AtomicInteger(0);

        // Subclass to inject observation points
        BoundedQueue instrumented = new BoundedQueue(5) {
            @Override
            public synchronized void put(String item) throws InterruptedException {
                concurrentAccess.incrementAndGet();          // entered put()
                Thread.sleep(100);                           // hold the monitor for 100ms
                super.put(item);
                concurrentAccess.decrementAndGet();
            }

            @Override
            public synchronized String take() throws InterruptedException {
                // If concurrentAccess > 0, put() is still holding the monitor.
                // This line can only be reached if the monitor was released.
                assertEquals(0, concurrentAccess.get(),
                        "take() must not run while put() holds the monitor");
                return super.take();
            }
        };

        instrumented.put("seed");   // pre-populate so take() doesn't block on empty

        Thread putter = new Thread(() -> {
            try { instrumented.put("new-item"); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread taker = new Thread(() -> {
            try { instrumented.take(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        putter.start();
        Thread.sleep(10);    // let putter acquire the monitor first
        taker.start();

        putter.join(2000);
        taker.join(2000);
    }
}
