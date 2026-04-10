package dev.alexkzk.algo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskSchedulerTest {

    private static int minTime(int[] tasks, int k) {
        return TaskScheduler.minTime(tasks, k);
    }

    // ── empty / trivial ──────────────────────────────────────────────────────

    @Test
    void emptyTasks() {
        assertEquals(0, minTime(new int[]{}, 3));
    }

    @Test
    void singleTask() {
        assertEquals(1, minTime(new int[]{1}, 3));
    }

    @Test
    void zeroCooldown() {
        // K=0 means no waiting ever — total time equals number of tasks
        assertEquals(4, minTime(new int[]{1, 2, 1, 2}, 0));
    }

    // ── no wait needed ───────────────────────────────────────────────────────

    @Test
    void allDistinctTasksNoCooldownNeeded() {
        // all IDs are unique — never need to wait regardless of K
        assertEquals(4, minTime(new int[]{1, 2, 3, 4}, 2));
    }

    @Test
    void cooldownAlreadySatisfiedByOtherTasks() {
        // tasks [1,2,3,1]: between the two 1s there are 2 tasks (2 and 3),
        // so with K=2 no wait is required → total = 4
        assertEquals(4, minTime(new int[]{1, 2, 3, 1}, 2));
    }

    // ── wait required ────────────────────────────────────────────────────────

    @Test
    void sameTaskBackToBack() {
        // [1,1] with K=2: after first 1, must wait 2 units → total = 1 + 2 + 1 = 4
        assertEquals(4, minTime(new int[]{1, 1}, 2));
    }

    @Test
    void sameTaskWithPartialCooldownFilled() {
        // [1,2,1] with K=2: one task between the two 1s, still need 1 wait unit → 1+1+1+1 = 4
        assertEquals(4, minTime(new int[]{1, 2, 1}, 2));
    }

    @Test
    void multipleCooldownGaps() {
        // [1,1,1] with K=2: each repeat of 1 requires waiting
        // t=1 exec 1, t=2 wait, t=3 wait, t=4 exec 1, t=5 wait, t=6 wait, t=7 exec 1 → 7
        assertEquals(7, minTime(new int[]{1, 1, 1}, 2));
    }

    @Test
    void twoDistinctTasksAlternating() {
        // [1,2,1,2] with K=2: between repeated 1s there is only one other task (2)
        // t=1 exec 1, t=2 exec 2, t=3 wait (idle ticks down task 2 cooldown too), t=4 exec 1, t=5 exec 2 → 5
        assertEquals(5, minTime(new int[]{1, 2, 1, 2}, 2));
    }

    @Test
    void largeKSmallGap() {
        // [1,2,1] with K=5: only 1 task between the two 1s, need 4 more waits
        // t=1 exec 1, t=2 exec 2, t=3..6 wait (4 waits), t=7 exec 1 → 7
        assertEquals(7, minTime(new int[]{1, 2, 1}, 5));
    }

    // ── mixed patterns ───────────────────────────────────────────────────────

    @Test
    void interleaved() {
        // [1,2,1,2,1,2] with K=1: same task must be separated by at least 1
        // consecutive tasks alternate 1 and 2, gap between same IDs = 1 = K → no wait
        assertEquals(6, minTime(new int[]{1, 2, 1, 2, 1, 2}, 1));
    }

    @Test
    void threeDistinctRotating() {
        // [1,2,3,1,2,3] with K=2: gap between repeats is exactly 2 → no wait
        assertEquals(6, minTime(new int[]{1, 2, 3, 1, 2, 3}, 2));
    }

    @Test
    void repeatedBlockWithWaits() {
        // [1,2,3,1,2,3] with K=3: gap = 2 < K=3, so 1 wait before second occurrence of 1
        // t=1 exec 1, t=2 exec 2, t=3 exec 3, t=4 wait, t=5 exec 1, t=6 exec 2, t=7 exec 3, t=8 wait, t=9 exec 1 ...
        // sequence [1,2,3,1,2,3] → between first 1 and second 1: tasks 2,3 fill 2 slots, need 1 wait
        // t1 exec1=1, t2 exec2=2, t3 exec3=3, t4 wait, t5 exec1=4, t6 exec2=5, t7 exec3=6 → 7
        assertEquals(7, minTime(new int[]{1, 2, 3, 1, 2, 3}, 3));
    }
}
