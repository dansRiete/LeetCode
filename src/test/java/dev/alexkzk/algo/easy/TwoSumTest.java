package dev.alexkzk.algo.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TwoSumTest {

    private static int[] solve(int[] nums, int target) {
        return TwoSum.twoSum(nums, target);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertArrayEquals(new int[]{0, 1}, solve(new int[]{2, 7, 11, 15}, 9));
    }

    @Test
    void leetcodeExample2() {
        assertArrayEquals(new int[]{1, 2}, solve(new int[]{3, 2, 4}, 6));
    }

    @Test
    void leetcodeExample3() {
        assertArrayEquals(new int[]{0, 1}, solve(new int[]{3, 3}, 6));
    }

    // ── negatives ────────────────────────────────────────────────────────────

    @Test
    void negativeNumbers() {
        assertArrayEquals(new int[]{0, 1}, solve(new int[]{-3, 3}, 0));
    }

    @Test
    void bothNegative() {
        assertArrayEquals(new int[]{0, 1}, solve(new int[]{-1, -2, -3}, -3));
    }

    // ── target is zero ───────────────────────────────────────────────────────

    @Test
    void targetZero() {
        assertArrayEquals(new int[]{0, 2}, solve(new int[]{5, 1, -5}, 0));
    }

    // ── answer not at start ──────────────────────────────────────────────────

    @Test
    void answerAtEnd() {
        assertArrayEquals(new int[]{3, 4}, solve(new int[]{1, 2, 3, 4, 5}, 9));
    }

    @Test
    void answerInMiddle() {
        assertArrayEquals(new int[]{1, 3}, solve(new int[]{1, 4, 3, 6}, 10));
    }

    // ── two elements ─────────────────────────────────────────────────────────

    @Test
    void twoElements() {
        assertArrayEquals(new int[]{0, 1}, solve(new int[]{1, 9}, 10));
    }

    // ── large values ─────────────────────────────────────────────────────────

    @Test
    void largeValues() {
        assertArrayEquals(new int[]{0, 1},
                solve(new int[]{1_000_000_000, -1_000_000_000}, 0));
    }
}
