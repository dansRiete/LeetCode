package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FindFirstAndLastPositionTest {

    private static int[] solve(int[] nums, int target) {
        return FindFirstAndLastPosition.searchRange(nums, target);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertArrayEquals(new int[]{3, 4}, solve(new int[]{5, 7, 7, 8, 8, 10}, 8));
    }

    @Test
    void leetcodeExample2_notFound() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{5, 7, 7, 8, 8, 10}, 6));
    }

    @Test
    void leetcodeExample3_emptyArray() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{}, 0));
    }

    // ── single occurrence ────────────────────────────────────────────────────

    @Test
    void singleElement_found() {
        assertArrayEquals(new int[]{0, 0}, solve(new int[]{5}, 5));
    }

    @Test
    void singleElement_notFound() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{5}, 3));
    }

    @Test
    void appearsOnce_inMiddle() {
        assertArrayEquals(new int[]{2, 2}, solve(new int[]{1, 2, 3, 4, 5}, 3));
    }

    // ── target at boundaries ─────────────────────────────────────────────────

    @Test
    void targetAtStart() {
        assertArrayEquals(new int[]{0, 2}, solve(new int[]{2, 2, 2, 5, 7}, 2));
    }

    @Test
    void targetAtEnd() {
        assertArrayEquals(new int[]{3, 4}, solve(new int[]{1, 2, 3, 9, 9}, 9));
    }

    @Test
    void targetIsFirstElement_once() {
        assertArrayEquals(new int[]{0, 0}, solve(new int[]{1, 2, 3}, 1));
    }

    @Test
    void targetIsLastElement_once() {
        assertArrayEquals(new int[]{2, 2}, solve(new int[]{1, 2, 3}, 3));
    }

    // ── all elements are the target ───────────────────────────────────────────

    @Test
    void allSame_found() {
        assertArrayEquals(new int[]{0, 4}, solve(new int[]{7, 7, 7, 7, 7}, 7));
    }

    @Test
    void allSame_notFound() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{7, 7, 7, 7, 7}, 3));
    }

    // ── not found cases ───────────────────────────────────────────────────────

    @Test
    void targetBelowMin() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{3, 5, 7}, 1));
    }

    @Test
    void targetAboveMax() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{3, 5, 7}, 9));
    }

    @Test
    void targetInGap() {
        assertArrayEquals(new int[]{-1, -1}, solve(new int[]{1, 3, 5, 7}, 4));
    }
}