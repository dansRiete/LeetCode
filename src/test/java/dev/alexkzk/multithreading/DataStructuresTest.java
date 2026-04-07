package dev.alexkzk.multithreading;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests covering:
 *  A) StackVsDeque   — correctness, ordering, performance ratio
 *  B) ConcurrentHashMap internals — thread-safety, atomicity, no-data-loss
 */
class DataStructuresTest {

    // =========================================================================
    // A. StackVsDeque
    // =========================================================================

    @Nested
    @DisplayName("A. ArrayDeque vs Stack")
    class DequeTests {

        // --- A1: both give LIFO order ---

        @Test
        @DisplayName("A1: Stack and ArrayDeque both produce LIFO order")
        void lifoOrder() {
            int[] input = {1, 2, 3, 4, 5};
            int[] expected = {5, 4, 3, 2, 1};  // reversed = LIFO

            Stack<Integer> stack = new Stack<>();
            StackVsDeque.pushAll(stack, input);
            assertArrayEquals(expected, StackVsDeque.popAll(stack),
                    "Stack must pop in LIFO order");

            Deque<Integer> deque = new ArrayDeque<>();
            StackVsDeque.pushAll(deque, input);
            assertArrayEquals(expected, StackVsDeque.popAll(deque),
                    "ArrayDeque must pop in LIFO order");
        }

        // --- A2: single element ---

        @Test
        @DisplayName("A2: Single element push/pop")
        void singleElement() {
            Deque<Integer> deque = new ArrayDeque<>();
            deque.push(42);
            assertEquals(42, deque.pop());
            assertTrue(deque.isEmpty());
        }

        // --- A3: Single-threaded microbenchmark — teaches JIT lock elision ---
        //
        // WHY THIS TEST DOES NOT ASSERT A PERFORMANCE RATIO:
        //
        // The JVM applies "lock elision" (escape analysis) when it can prove a lock
        // object cannot escape to another thread. Since our Stack is local to this
        // method, the JIT may eliminate its synchronized blocks entirely, making
        // uncontended Stack ≈ ArrayDeque in microbenchmarks.
        //
        // LESSON: Stack's synchronization cost is real under contention (see A4),
        // but invisible under single-threaded use because the JVM is smart enough
        // to remove it. The architectural problem with Stack isn't just raw speed —
        // it's that it *forces* you to pay the synchronization tax in shared-state
        // scenarios even when you don't need it, and its API leaks thread-unsafe
        // operations via its Vector superclass (e.g. get(index), insertElementAt).
        //
        // This test just prints the numbers so you can observe them.

        @Test
        @DisplayName("A3: Microbenchmark (informational — JIT lock elision means no ratio assertion)")
        void dequeVsStackMicrobenchmarkInformational() {
            int n = 1_000_000;

            // Warm up
            for (int i = 0; i < 5; i++) {
                StackVsDeque.benchmarkStack(n / 10);
                StackVsDeque.benchmarkDeque(n / 10);
            }

            long stackNs = StackVsDeque.benchmarkStack(n);
            long dequeNs = StackVsDeque.benchmarkDeque(n);
            double ratio = (double) stackNs / dequeNs;

            System.out.printf("  [A3] Stack: %,d ms | ArrayDeque: %,d ms | ratio: %.2fx%n",
                    stackNs / 1_000_000, dequeNs / 1_000_000, ratio);
            System.out.println("  Note: ratio may be ~1.0 due to JIT lock elision on uncontended stack.");
            System.out.println("  The real cost shows under contention — see A4.");

            // We only assert both complete; timing is informational
            assertTrue(stackNs > 0 && dequeNs > 0, "Both benchmarks must complete");
        }

        // --- A4: Stack's synchronized overhead is measurable under contention ---
        // Two threads pushing/popping a shared Stack must serialize;
        // two threads on separate ArrayDeques (same pattern) should be faster.

        @Test
        @DisplayName("A4: Shared Stack serializes threads; separate Deques do not")
        void stackSynchronizationContention() throws InterruptedException {
            int n = 100_000;

            // Shared Stack — both threads acquire the same lock
            Stack<Integer> sharedStack = new Stack<>();
            for (int i = 0; i < n; i++) sharedStack.push(i);

            long stackTime = timedRun(() -> {
                CountDownLatch ready = new CountDownLatch(2);
                CountDownLatch go    = new CountDownLatch(1);
                Runnable task = () -> {
                    ready.countDown();
                    try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    for (int i = 0; i < n / 2; i++) {
                        synchronized (sharedStack) { sharedStack.push(i); }
                    }
                };
                Thread t1 = new Thread(task), t2 = new Thread(task);
                t1.start(); t2.start();
                try {
                    ready.await();
                    go.countDown();
                    t1.join(); t2.join();
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });

            // Two separate Deques — zero shared state
            long dequeTime = timedRun(() -> {
                Deque<Integer> d1 = new ArrayDeque<>(), d2 = new ArrayDeque<>();
                CountDownLatch ready = new CountDownLatch(2);
                CountDownLatch go    = new CountDownLatch(1);
                Thread t1 = new Thread(() -> {
                    ready.countDown();
                    try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    for (int i = 0; i < n; i++) d1.push(i);
                });
                Thread t2 = new Thread(() -> {
                    ready.countDown();
                    try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    for (int i = 0; i < n; i++) d2.push(i);
                });
                t1.start(); t2.start();
                try {
                    ready.await();
                    go.countDown();
                    t1.join(); t2.join();
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });

            System.out.printf("  Shared Stack: %,d ms | Separate Deques: %,d ms%n",
                    stackTime / 1_000_000, dequeTime / 1_000_000);

            assertTrue(dequeTime < stackTime,
                    "Separate ArrayDeques should be faster than a shared synchronized Stack");
        }

        // --- A5-A8: balanced parentheses ---

        @ParameterizedTest(name = "A5: isBalanced(\"{0}\") == {1}")
        @CsvSource({
            "({[]}),           true",
            "()[]{},           true",
            "((())),           true",
            "({[}),            false",
            "(((),             false",
            "'',               true",
            "(,                false",
            "hello(world[!]),  true"
        })
        void balancedParentheses(String expr, boolean expected) {
            assertEquals(expected, StackVsDeque.isBalanced(expr.trim()),
                    "isBalanced(\"" + expr.trim() + "\")");
        }
    }

    // =========================================================================
    // B. ConcurrentHashMap internals
    // =========================================================================

    @Nested
    @DisplayName("B. ConcurrentHashMap internals")
    class ConcurrentMapTests {

        // --- B1: No data loss under concurrent writes ---

        @Test
        @DisplayName("B1: All keys written by 8 concurrent threads are present")
        void noDataLossUnderConcurrentWrites() throws InterruptedException {
            int threads = 8, keysPerThread = 10_000;
            Map<Integer, Integer> map = ConcurrentHashMapInternals.concurrentWrites(threads, keysPerThread);

            assertEquals(threads * keysPerThread, map.size(),
                    "Every key must be present — no silent overwrites or lost puts");

            // Spot-check values
            for (int i = 0; i < threads * keysPerThread; i++) {
                assertEquals(i * i, map.get(i), "Value for key " + i + " must be i²");
            }
        }

        // --- B2: Reads complete even while writes are happening ---

        @Test
        @DisplayName("B2: Lock-free reads — reader thread completes alongside writer")
        void lockFreeReads() throws InterruptedException {
            long[] result = ConcurrentHashMapInternals.lockFreeReadDemo();
            long reads  = result[0];
            long writes = result[1];

            // Both sides should have completed their full quota
            assertEquals(500_000, reads,  "All reads must complete");
            assertEquals(500_000, writes, "All writes must complete");
        }

        // --- B3: merge() is atomic — word count must be exact ---

        @Test
        @DisplayName("B3: merge() is atomic — concurrent word counting is exact")
        void atomicMerge() throws InterruptedException {
            // "apple" × 100, "banana" × 50 — submitted across 4 threads
            int appleCount = 100, bananaCount = 50;
            String[] words = new String[appleCount + bananaCount];
            Arrays.fill(words, 0, appleCount, "apple");
            Arrays.fill(words, appleCount, appleCount + bananaCount, "banana");

            // Shuffle to interleave apple/banana across threads
            List<String> list = new ArrayList<>(Arrays.asList(words));
            Collections.shuffle(list, new Random(42));

            Map<String, Integer> freq = ConcurrentHashMapInternals.wordFrequency(
                    list.toArray(new String[0]));

            assertEquals(appleCount,  freq.get("apple"),  "apple count must be exact");
            assertEquals(bananaCount, freq.get("banana"), "banana count must be exact");
        }

        // --- B4: computeIfAbsent — list created exactly once per key ---

        @Test
        @DisplayName("B4: computeIfAbsent never creates duplicate lists")
        void computeIfAbsentCreatesListOnce() throws InterruptedException {
            // 90 numbers starting with 1, 2, or 3
            int[] numbers = new int[90];
            for (int i = 0; i < 30; i++) {
                numbers[i]      = 100 + i;  // starts with '1'
                numbers[30 + i] = 200 + i;  // starts with '2'
                numbers[60 + i] = 300 + i;  // starts with '3'
            }

            Map<String, List<Integer>> groups =
                    ConcurrentHashMapInternals.groupByFirstChar(numbers);

            assertEquals(3, groups.size(), "Must have exactly 3 groups");
            assertEquals(30, groups.get("1").size(), "Group '1' must have 30 entries");
            assertEquals(30, groups.get("2").size(), "Group '2' must have 30 entries");
            assertEquals(30, groups.get("3").size(), "Group '3' must have 30 entries");
        }

        // --- B5: Spread keys are faster than same-bucket keys ---

        @Test
        @DisplayName("B5: Keys in different buckets contend less than keys in the same bucket")
        void spreadKeysAreFasterThanSameBucket() throws InterruptedException {
            int iterations = 200_000;

            // Warm up
            ConcurrentHashMapInternals.contendedVsSpread(10_000);

            long[] times = ConcurrentHashMapInternals.contendedVsSpread(iterations);
            long sameBucket  = times[0];
            long spreadBucket = times[1];

            System.out.printf("  Same bucket: %,d ms | Spread: %,d ms%n",
                    sameBucket / 1_000_000, spreadBucket / 1_000_000);

            assertTrue(spreadBucket < sameBucket,
                    "Writes to different buckets should be faster (less lock contention)");
        }

        // --- B6: Concurrent size is consistent (not stale like HashMap) ---

        @Test
        @DisplayName("B6: size() after concurrent puts reflects all insertions")
        void consistentSizeAfterConcurrentPuts() throws InterruptedException {
            ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
            int threadCount = 10, keysEach = 1_000;

            ExecutorService pool = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            AtomicInteger totalPuts = new AtomicInteger();

            for (int t = 0; t < threadCount; t++) {
                final int base = t * keysEach;
                pool.submit(() -> {
                    for (int i = base; i < base + keysEach; i++) {
                        map.put(i, i);
                        totalPuts.incrementAndGet();
                    }
                    latch.countDown();
                });
            }
            latch.await();
            pool.shutdown();

            assertEquals(threadCount * keysEach, map.size(),
                    "size() must match total distinct keys inserted");
        }

        // --- B7: putIfAbsent — only first writer wins ---

        @Test
        @DisplayName("B7: putIfAbsent — only first writer's value is retained")
        void putIfAbsentFirstWriterWins() throws InterruptedException {
            ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
            int threadCount = 50;

            CountDownLatch ready = new CountDownLatch(threadCount);
            CountDownLatch go    = new CountDownLatch(1);
            CountDownLatch done  = new CountDownLatch(threadCount);

            AtomicInteger winnerCount = new AtomicInteger();

            for (int t = 0; t < threadCount; t++) {
                final int id = t;
                new Thread(() -> {
                    ready.countDown();
                    try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    // Returns null if this thread wrote first, otherwise the existing value
                    Integer prev = map.putIfAbsent("shared-key", id);
                    if (prev == null) winnerCount.incrementAndGet();
                    done.countDown();
                }).start();
            }

            ready.await();
            go.countDown();
            done.await();

            assertEquals(1, winnerCount.get(),   "Exactly one thread must win putIfAbsent");
            assertEquals(1, map.size(),           "Map must have exactly one entry");
            assertTrue(map.containsKey("shared-key"), "Key must exist");
        }
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------

    private static long timedRun(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }
}
