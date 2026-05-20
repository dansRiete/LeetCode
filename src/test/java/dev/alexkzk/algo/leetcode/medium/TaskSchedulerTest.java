package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskSchedulerTest {

    private static int solve(int k, int... tasks) {
        return TaskScheduler.minTime(tasks, k);
    }

    // ── docstring examples ────────────────────────────────────────────────────

    @Test
    void example_noIdleNeeded() {
        // [1,2,3,1], k=2: tasks fill the cooldown gap perfectly → 4
        assertEquals(4, solve(2, 1, 2, 3, 1));
    }

    @Test
    void example_allIdleNeeded() {
        // [1,1], k=2: exec 1, idle, idle, exec 1 → 4
        assertEquals(4, solve(2, 1, 1));
    }

    @Test
    void example_oneIdleNeeded() {
        // [1,2,1], k=2: exec 1, exec 2, idle, exec 1 → 4
        assertEquals(4, solve(2, 1, 2, 1));
    }

    @Test
    void example_twoIdlePeriods() {
        // [1,1,1], k=2: 1, idle, idle, 1, idle, idle, 1 → 7
        assertEquals(7, solve(2, 1, 1, 1));
    }

    // ── zero cooldown ─────────────────────────────────────────────────────────

    @Test
    void zeroCooldown_noIdleEver() {
        // k=0: every task runs back-to-back → total time == task count
        assertEquals(4, solve(0, 1, 2, 3, 1));
    }

    @Test
    void zeroCooldown_allSameTask() {
        assertEquals(5, solve(0, 1, 1, 1, 1, 1));
    }

    // ── empty and null input ──────────────────────────────────────────────────

    @Test
    void emptyTasks() {
        assertEquals(0, TaskScheduler.minTime(new int[]{}, 3));
    }

    @Test
    void nullTasks() {
        assertEquals(0, TaskScheduler.minTime(null, 3));
    }

    // ── single task ───────────────────────────────────────────────────────────

    @Test
    void singleTask() {
        assertEquals(1, solve(5, 99));
    }

    // ── large cooldown forces many idles ──────────────────────────────────────

    @Test
    void largeCooldown() {
        // [1,1,1], k=100: each repeat waits 100 slots → 1 + 101 + 101 = ... no:
        // time = max(1, 1+101) = 102, then max(103, 102+101) = 203 → total = 203
        assertEquals(203, solve(100, 1, 1, 1));
    }

    @Test
    void largeCooldown_exactFit() {
        // 101 distinct tasks before the repeat, k=100 → no idle needed
        int[] tasks = new int[102];
        for (int i = 0; i < 101; i++) tasks[i] = i + 1;
        tasks[101] = 1; // repeat task 1 after 101 other tasks
        assertEquals(102, solve(100, tasks));
    }

    // ── multiple different tasks ──────────────────────────────────────────────

    @Test
    void multipleDifferentTasks_noCooldown() {
        // entirely distinct tasks with k=3: no repeats → total = length
        assertEquals(5, solve(3, 1, 2, 3, 4, 5));
    }

    @Test
    void interleavedRepeats() {
        // [1,2,3,1,2,3], k=3: each task repeats exactly after 3 other tasks → no idle
        assertEquals(7, solve(3, 1, 2, 3, 1, 2, 3));
    }

    @Test
    void manyRepeatsOfOneTask() {
        // [1,1,1,1,1], k=3: 1, idle, idle, idle, 1, idle, idle, idle, 1 ... → 17
        assertEquals(17, solve(3, 1, 1, 1, 1, 1));
    }

    @Test
    void mixedCooldownBehaviour() {
        // [1,1,3,4,5], k=3: task 1 repeats immediately — needs idle
        assertEquals(8, solve(3, 1, 1, 3, 4, 5));
    }

    // ── two tasks alternating ─────────────────────────────────────────────────

    @Test
    void twoTasksAlternating_sufficientGap() {
        // [1,2,1,2], k=1: gap of 1 between same tasks → no idle → total = 4 + 0 idles?
        // task1 at t=1, task2 at t=2, task1 at max(3, 1+1+1)=max(3,3)=3, task2 at max(4,2+1+1)=max(4,4)=4 → 4
        assertEquals(4, solve(1, 1, 2, 1, 2));
    }

    @Test
    void twoTasksAlternating_needsIdle() {
        // [1,2,1,2], k=2:
        // t=1 exec 1, t=2 exec 2, t=3 idle (task1 cooldown expires at t=4), t=4 exec 1, t=5 exec 2
        // → total = 5
        assertEquals(5, solve(2, 1, 2, 1, 2));
    }
}
