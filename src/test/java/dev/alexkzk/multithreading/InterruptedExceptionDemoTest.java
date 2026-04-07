package dev.alexkzk.multithreading;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class InterruptedExceptionDemoTest {

    // S1: thread was already blocked — must be woken and report interrupted
    @Test
    @DisplayName("S1: Interrupt wakes a sleeping thread immediately")
    void alreadyBlocked() throws InterruptedException {
        String result = InterruptedExceptionDemo.scenario1_alreadyBlocked();
        assertEquals("interrupted-while-sleeping", result);
    }

    // S2: interrupt() called while running — exception fires at next sleep()
    @Test
    @DisplayName("S2: Interrupt flag held until thread enters a blocking call")
    void runningThenBlocks() throws InterruptedException {
        String result = InterruptedExceptionDemo.scenario2_runningThenBlocks();
        assertEquals("interrupted-at-sleep", result);
    }

    // S3: no blocking calls — thread must poll Thread.interrupted() itself
    @Test
    @DisplayName("S3: CPU-bound loop exits when it polls the interrupt flag")
    void cpuBoundLoop() throws InterruptedException {
        int iterations = InterruptedExceptionDemo.scenario3_cpuBoundLoop();
        assertTrue(iterations > 0, "Loop must have run at least one iteration");
    }

    // S4: swallowed catch — thread keeps running, flag is silently lost
    @Test
    @DisplayName("S4 (BAD): Swallowing InterruptedException lets the thread keep running")
    void swallowedInterrupt() throws InterruptedException {
        String result = InterruptedExceptionDemo.scenario4_swallowedInterrupt();
        assertEquals("kept-running-after-interrupt", result,
                "Thread must continue after swallowing the exception");
    }

    // S5: flag restored — caller up the stack can still observe the interrupt
    @Test
    @DisplayName("S5 (GOOD): Restoring the flag makes it visible to callers")
    void flagRestored() throws InterruptedException {
        boolean flagVisible = InterruptedExceptionDemo.scenario5_flagRestored();
        assertTrue(flagVisible, "Flag must be true after Thread.currentThread().interrupt()");
    }

    // S6: executor shuts down mid-backoff-sleep — worker exits cleanly
    @Test
    @DisplayName("S6: Backoff sleep interrupted by executor shutdown — worker exits cleanly")
    void backoffInterrupted() throws InterruptedException {
        String result = InterruptedExceptionDemo.scenario6_backoffInterrupted();
        assertTrue(result != null && result.startsWith("aborted-at-attempt-"),
                "Worker must abort cleanly on interrupt, got: " + result);
    }
}
