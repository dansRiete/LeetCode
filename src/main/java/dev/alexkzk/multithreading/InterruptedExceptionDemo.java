package dev.alexkzk.multithreading;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Demonstrates every scenario where InterruptedException is involved.
 *
 * SCENARIO 1 — Thread already blocked: interrupt() wakes it immediately.
 * SCENARIO 2 — Thread running:         interrupt() sets flag, exception fires at next block.
 * SCENARIO 3 — CPU-bound loop:         thread polls the flag manually via Thread.interrupted().
 * SCENARIO 4 — Flag swallowed (BAD):   silent catch loses the signal; thread keeps running.
 * SCENARIO 5 — Flag restored (GOOD):   Thread.currentThread().interrupt() re-sets the flag.
 * SCENARIO 6 — Backoff integration:    replicates ExponentialBackoff sleep interruption.
 */
public class InterruptedExceptionDemo {

    // -----------------------------------------------------------------------
    // SCENARIO 1: Thread is already blocked when interrupt() is called.
    //
    //   Timeline:
    //     t=0  workerThread enters Thread.sleep(10_000)  — parked, off CPU
    //     t=50 main calls workerThread.interrupt()
    //          JVM sees flag=true on a parked thread
    //          → unblocks immediately
    //          → throws InterruptedException inside sleep()
    //          → clears the flag
    // -----------------------------------------------------------------------
    public static String scenario1_alreadyBlocked() throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>();

        Thread worker = new Thread(() -> {
            System.out.println("  [worker] entering sleep(10_000) ...");
            try {
                Thread.sleep(10_000);                          // will never reach 10s
                outcome.set("completed-sleep");
            } catch (InterruptedException e) {
                // We were unblocked early
                System.out.println("  [worker] InterruptedException caught — woken up early");
                System.out.println("  [worker] flag after catch: " + Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();            // restore flag
                outcome.set("interrupted-while-sleeping");
            }
        });

        worker.start();
        Thread.sleep(50);                  // give worker time to reach sleep()
        System.out.println("  [main]   calling worker.interrupt()");
        worker.interrupt();
        worker.join();

        return outcome.get();
    }

    // -----------------------------------------------------------------------
    // SCENARIO 2: Thread is running when interrupt() is called.
    //
    //   The tricky part: we must guarantee interrupt() fires WHILE the thread
    //   is executing (not parked), and BEFORE it calls sleep().
    //   A simple sleep(50) in main is unreliable — the compute loop may finish
    //   before main wakes up, letting sleep(1) complete normally.
    //
    //   Solution: a two-flag handshake with no blocking calls in the worker.
    //     1. Worker sets workerRunning=true
    //     2. Main spins until it sees workerRunning, then calls interrupt()
    //        — flag is now set while worker is provably on CPU
    //     3. Main sets proceedToSleep=true
    //     4. Worker spins until it sees proceedToSleep, then calls sleep(1)
    //        — flag is already true → sleep() throws immediately
    //
    //   Timeline:
    //     [worker] running → workerRunning=true → spins on proceedToSleep
    //     [main]   sees workerRunning → interrupt() → proceedToSleep=true
    //     [worker] sees proceedToSleep → sleep(1) → InterruptedException
    // -----------------------------------------------------------------------
    public static String scenario2_runningThenBlocks() throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>();
        AtomicBoolean workerRunning  = new AtomicBoolean(false);
        AtomicBoolean proceedToSleep = new AtomicBoolean(false);

        Thread worker = new Thread(() -> {
            // Phase 1: signal that we are running (no blocking call yet)
            workerRunning.set(true);

            // Spin (pure CPU) — no blocking call, so interrupt() only sets the flag
            while (!proceedToSleep.get()) { /* busy wait */ }

            System.out.println("  [worker] flag before sleep: "
                    + Thread.currentThread().isInterrupted());   // true

            // Phase 2: first blocking call — flag already set → throws immediately
            try {
                Thread.sleep(1);
                outcome.set("sleep-completed");
            } catch (InterruptedException e) {
                System.out.println("  [worker] InterruptedException on sleep() — flag was pending");
                Thread.currentThread().interrupt();
                outcome.set("interrupted-at-sleep");
            }
        });

        worker.start();

        // Spin until worker is confirmed running, then interrupt before it sleeps
        while (!workerRunning.get()) { /* spin */ }
        System.out.println("  [main]   worker is running — calling interrupt()");
        worker.interrupt();
        proceedToSleep.set(true);   // now let worker proceed to its sleep() call

        worker.join();
        return outcome.get();
    }

    // -----------------------------------------------------------------------
    // SCENARIO 3: CPU-bound loop — thread must poll the flag manually.
    //
    //   There is no blocking call, so InterruptedException is never thrown.
    //   The thread checks Thread.interrupted() on each iteration to stay
    //   cancellable. Thread.interrupted() READS AND CLEARS the flag.
    // -----------------------------------------------------------------------
    public static int scenario3_cpuBoundLoop() throws InterruptedException {
        AtomicReference<Integer> iterationsDone = new AtomicReference<>(0);

        Thread worker = new Thread(() -> {
            int count = 0;
            // Thread.interrupted() checks the flag and clears it in one atomic step.
            // isInterrupted() checks without clearing — useful for inspection only.
            while (!Thread.interrupted()) {
                count++;
                // simulate work
                double x = Math.sqrt(count);
                if (x < 0) break; // never true — just prevents dead-code elimination
            }
            System.out.println("  [worker] loop exited after " + count + " iterations");
            iterationsDone.set(count);
        });

        worker.start();
        Thread.sleep(20);
        System.out.println("  [main]   interrupting CPU-bound worker");
        worker.interrupt();
        worker.join();

        return iterationsDone.get();
    }

    // -----------------------------------------------------------------------
    // SCENARIO 4 (BAD): Swallowing InterruptedException.
    //
    //   The catch block does nothing with the exception.
    //   The flag is already cleared by the time we catch it.
    //   The executor trying to shut down this thread will be stuck — it set
    //   the flag hoping the thread would stop, but the thread ate the signal.
    // -----------------------------------------------------------------------
    public static String scenario4_swallowedInterrupt() throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>("not-set");
        CountDownLatch workerReady  = new CountDownLatch(1);
        CountDownLatch continueWork = new CountDownLatch(1);

        Thread worker = new Thread(() -> {
            workerReady.countDown();
            try {
                Thread.sleep(10_000);          // will be interrupted
            } catch (InterruptedException e) {
                // BAD: flag is now false. Caller has no idea we were interrupted.
                System.out.println("  [worker] caught InterruptedException — but SWALLOWING it");
                System.out.println("  [worker] flag after swallow: " + Thread.currentThread().isInterrupted());
            }
            // Thread keeps running as if nothing happened
            outcome.set("kept-running-after-interrupt");
            continueWork.countDown();
        });

        worker.start();
        workerReady.await();
        worker.interrupt();
        continueWork.await(2, TimeUnit.SECONDS);
        worker.join(500);

        System.out.println("  [main]   worker still alive? " + worker.isAlive());
        return outcome.get();
    }

    // -----------------------------------------------------------------------
    // SCENARIO 5 (GOOD): Restoring the flag with Thread.currentThread().interrupt()
    //
    //   After catching InterruptedException, we restore the flag.
    //   Any caller up the stack that checks isInterrupted() will see it.
    //   The executor's shutdown mechanism works correctly.
    // -----------------------------------------------------------------------
    public static boolean scenario5_flagRestored() throws InterruptedException {
        AtomicBoolean flagVisibleToCalller = new AtomicBoolean(false);

        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                System.out.println("  [worker] caught InterruptedException");
                System.out.println("  [worker] flag immediately after catch: "
                        + Thread.currentThread().isInterrupted());   // false — cleared by JVM

                Thread.currentThread().interrupt();                  // restore it

                System.out.println("  [worker] flag after restore:           "
                        + Thread.currentThread().isInterrupted());   // true again
            }

            // Caller (or framework) can now observe the flag
            flagVisibleToCalller.set(Thread.currentThread().isInterrupted());
        });

        worker.start();
        Thread.sleep(50);
        worker.interrupt();
        worker.join();

        return flagVisibleToCalller.get();
    }

    // -----------------------------------------------------------------------
    // SCENARIO 6: Backoff sleep interrupted — replicates ExponentialBackoff.
    //
    //   A retry loop is sleeping between attempts.
    //   An ExecutorService shuts down and interrupts the worker thread mid-sleep.
    //   Without restoring the flag the executor hangs waiting for termination.
    //
    //   Flow:
    //     attempt 1 → fails → sleep(500) interrupted → RetryAbortedException thrown
    //     executor.awaitTermination() succeeds because flag was restored
    // -----------------------------------------------------------------------
    public static String scenario6_backoffInterrupted() throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            int attempt = 0;
            while (attempt < 5) {
                try {
                    // Simulate a failing remote call
                    System.out.println("  [worker] attempt " + attempt + " — calling remote service");
                    throw new RuntimeException("service unavailable");

                } catch (RuntimeException callFailed) {
                    long sleepMs = 100L * (1L << attempt);   // exponential: 100, 200, 400 ...
                    System.out.println("  [worker] attempt " + attempt + " failed, sleeping " + sleepMs + "ms");
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException ie) {
                        // Executor is shutting down — honour the signal
                        System.out.println("  [worker] sleep interrupted during backoff on attempt " + attempt);
                        Thread.currentThread().interrupt();  // restore so executor sees it
                        outcome.set("aborted-at-attempt-" + attempt);
                        return;                              // exit the task cleanly
                    }
                }
                attempt++;
            }
            outcome.set("exhausted-all-attempts");
        });

        // Let one attempt fail and start sleeping, then shut down
        Thread.sleep(150);
        System.out.println("  [main]   shutting down executor");
        executor.shutdownNow();                              // calls interrupt() on the worker

        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("  [main]   executor terminated cleanly: " + terminated);

        return outcome.get();
    }

    // -----------------------------------------------------------------------
    // Main — runs all scenarios with clear labels
    // -----------------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SCENARIO 1: Already blocked ===");
        System.out.println("  Result: " + scenario1_alreadyBlocked());

        /*System.out.println("\n=== SCENARIO 2: Running, then hits a blocking call ===");
        System.out.println("  Result: " + scenario2_runningThenBlocks());

        System.out.println("\n=== SCENARIO 3: CPU-bound loop, manual flag polling ===");
        System.out.println("  Result: completed " + scenario3_cpuBoundLoop() + " iterations");

        System.out.println("\n=== SCENARIO 4 (BAD): Swallowed interrupt ===");
        System.out.println("  Result: " + scenario4_swallowedInterrupt());

        System.out.println("\n=== SCENARIO 5 (GOOD): Flag restored ===");
        System.out.println("  Flag visible to caller: " + scenario5_flagRestored());

        System.out.println("\n=== SCENARIO 6: Backoff sleep interrupted by executor shutdown ===");
        System.out.println("  Result: " + scenario6_backoffInterrupted());*/
    }
}
