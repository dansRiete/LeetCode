package dev.alexkzk.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates how ConcurrentHashMap handles concurrency internally (Java 8+).
 *
 * LOCKING STRATEGY OVERVIEW
 * ─────────────────────────
 * 1. Reads      → lock-free. Bucket array slots and Node fields are volatile.
 *                 A reader never blocks, even during a concurrent write.
 *
 * 2. Empty-slot  → CAS (compareAndSet). If a bucket is null, the first writer
 *    write          atomically swaps null → new Node. No lock acquired.
 *
 * 3. Non-empty  → synchronized(bucketHead). Only the first node of the bucket
 *    bucket        acts as the monitor. Two threads writing to *different*
 *    write         buckets never contend.
 *
 * 4. Resize      → cooperative. Threads calling put() during a resize help
 *                  transfer buckets. Each thread claims a stripe of buckets
 *                  via CAS on an internal `transferIndex` counter.
 *
 * 5. Treeification → when a bucket chain exceeds 8 nodes it becomes a
 *                    red-black tree. The tree root becomes the lock object.
 *
 * Visual bucket layout (16-slot table):
 *
 *   [0]  →  Node(k=0) → null          ← CAS write if null
 *   [1]  →  Node(k=1) → Node(k=17)    ← synchronized(Node(k=1)) for writes
 *   [2]  →  null
 *   ...
 *   [15] →  TreeBin(root)             ← synchronized(TreeBin) if treeified
 *
 * Two threads writing to bucket[0] and bucket[1] simultaneously → no contention.
 */
public class ConcurrentHashMapInternals {

    // -----------------------------------------------------------------------
    // 1. Concurrent writes from many threads — prove no data is lost
    // -----------------------------------------------------------------------

    public static Map<Integer, Integer> concurrentWrites(int threadCount, int keysPerThread)
            throws InterruptedException {

        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int base = t * keysPerThread;
            pool.submit(() -> {
                for (int i = base; i < base + keysPerThread; i++) {
                    map.put(i, i * i);
                }
                latch.countDown();
            });
        }

        latch.await();
        pool.shutdown();
        return map;
    }

    // -----------------------------------------------------------------------
    // 2. Lock-free reads — readers never block writers and vice versa
    //
    //    We write 1 million times on one thread while reading 1 million times
    //    on another. Both complete without either blocking the other.
    // -----------------------------------------------------------------------

    public static long[] lockFreeReadDemo() throws InterruptedException {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        // Pre-populate so readers have something to find
        for (int i = 0; i < 1000; i++) map.put(i, i);

        AtomicInteger readCount  = new AtomicInteger();
        AtomicInteger writeCount = new AtomicInteger();
        CountDownLatch done = new CountDownLatch(2);

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 500_000; i++) {
                map.put(i % 1000, i);
                writeCount.incrementAndGet();
            }
            done.countDown();
        });

        Thread reader = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 500_000; i++) {
                Integer v = map.get(i % 1000);
                if (v != null) sum += v;
                readCount.incrementAndGet();
            }
            done.countDown();
        });

        long start = System.nanoTime();
        writer.start();
        reader.start();
        done.await();
        long elapsed = System.nanoTime() - start;

        // Both completed — neither was blocked
        return new long[]{ readCount.get(), writeCount.get(), elapsed };
    }

    // -----------------------------------------------------------------------
    // 3. Atomic compute operations — thread-safe increment without external sync
    //
    //    merge() and compute() are single atomic operations on the bucket lock.
    //    Using get() + put() separately would be a race condition.
    // -----------------------------------------------------------------------

    public static Map<String, Integer> wordFrequency(String[] words) throws InterruptedException {
        ConcurrentHashMap<String, Integer> freq = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(words.length);

        for (String word : words) {
            pool.submit(() -> {
                // merge: if absent → put 1, if present → apply (old, 1) → old + 1
                // Entire read-modify-write is under the bucket lock — no race.
                freq.merge(word, 1, Integer::sum);
                latch.countDown();
            });
        }

        latch.await();
        pool.shutdown();
        return freq;
    }

    // -----------------------------------------------------------------------
    // 4. Bucket contention demo — keys that hash to the same bucket DO contend
    //
    //    In a default 16-slot table, keys 0, 16, 32, 48 all land in bucket[0].
    //    Threads writing only those keys will serialize on one bucket lock.
    //    Keys spread across buckets run in true parallel.
    // -----------------------------------------------------------------------

    public static long[] contendedVsSpread(int iterations) throws InterruptedException {
        return new long[]{
            timeWrites(iterations, /* same bucket */ new int[]{0, 16, 32, 48}),
            timeWrites(iterations, /* different    */ new int[]{0,  1,  2,  3})
        };
    }

    private static long timeWrites(int iterations, int[] keys) throws InterruptedException {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>(16);
        CountDownLatch latch = new CountDownLatch(keys.length);
        ExecutorService pool = Executors.newFixedThreadPool(keys.length);

        long start = System.nanoTime();
        for (int key : keys) {
            pool.submit(() -> {
                for (int i = 0; i < iterations; i++) map.put(key, i);
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();
        return System.nanoTime() - start;
    }

    // -----------------------------------------------------------------------
    // 5. computeIfAbsent — safe lazy initialization, no double-initialization
    //
    //    Classic pitfall: HashMap.computeIfAbsent is NOT thread-safe.
    //    ConcurrentHashMap guarantees the function runs at most once per key.
    // -----------------------------------------------------------------------

    public static Map<String, List<Integer>> groupByFirstChar(int[] numbers)
            throws InterruptedException {

        ConcurrentHashMap<String, List<Integer>> groups = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(numbers.length);

        for (int n : numbers) {
            pool.submit(() -> {
                String key = String.valueOf(n).substring(0, 1);
                // computeIfAbsent: list creation happens under bucket lock — only once
                groups.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(n);
                latch.countDown();
            });
        }

        latch.await();
        pool.shutdown();
        return groups;
    }

    // -----------------------------------------------------------------------
    // Demo main
    // -----------------------------------------------------------------------

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1. Concurrent writes (8 threads × 10,000 keys) ===");
        Map<Integer, Integer> result = concurrentWrites(8, 10_000);
        System.out.printf("  Keys written: %,d / 80,000 expected%n", result.size());

        System.out.println("\n=== 2. Lock-free reads ===");
        long[] rw = lockFreeReadDemo();
        System.out.printf("  Reads: %,d  Writes: %,d  Elapsed: %,d ms%n",
                rw[0], rw[1], rw[2] / 1_000_000);

        System.out.println("\n=== 3. Word frequency (merge) ===");
        String[] words = {"apple","banana","apple","cherry","banana","apple"};
        System.out.println("  Frequencies: " + wordFrequency(words));

        System.out.println("\n=== 4. Contended bucket vs spread (100,000 iterations) ===");
        long[] times = contendedVsSpread(100_000);
        System.out.printf("  Same bucket  (keys 0,16,32,48): %,d ms%n", times[0] / 1_000_000);
        System.out.printf("  Spread buckets (keys 0,1,2,3) : %,d ms%n", times[1] / 1_000_000);

        System.out.println("\n=== 5. computeIfAbsent grouping ===");
        int[] numbers = {10,20,30,11,21,31,12,22,32};
        System.out.println("  Groups: " + groupByFirstChar(numbers));
    }
}
