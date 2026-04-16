package dev.alexkzk.algo;

import dev.alexkzk.algo.medium.MergeIntervals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeIntervalsTest {

    private static int[][] merge(int[]... intervals) {
        return MergeIntervals.merge(intervals);
    }

    // ── empty / trivial ──────────────────────────────────────────────────────

    @Test
    void emptyInput() {
        assertArrayEquals(new int[][]{}, merge());
    }

    @Test
    void singleInterval() {
        assertArrayEquals(new int[][]{{2, 5}}, merge(new int[]{2, 5}));
    }

    @Test
    void singlePoint() {
        // degenerate interval where start == end
        assertArrayEquals(new int[][]{{3, 3}}, merge(new int[]{3, 3}));
    }

    // ── no merge needed ──────────────────────────────────────────────────────

    @Test
    void twoNonOverlappingAlreadySorted() {
        // gap between intervals — nothing to merge
        assertArrayEquals(
                new int[][]{{1, 2}, {4, 6}},
                merge(new int[]{1, 2}, new int[]{4, 6}));
    }

    @Test
    void manyNonOverlapping() {
        assertArrayEquals(
                new int[][]{{1, 2}, {4, 5}, {7, 8}},
                merge(new int[]{1, 2}, new int[]{4, 5}, new int[]{7, 8}));
    }

    // ── simple overlaps ──────────────────────────────────────────────────────

    @Test
    void twoOverlapping() {
        // [1,3] and [2,6] share [2,3]
        assertArrayEquals(
                new int[][]{{1, 6}},
                merge(new int[]{1, 3}, new int[]{2, 6}));
    }

    @Test
    void twoTouching() {
        // intervals share exactly one endpoint — must merge
        assertArrayEquals(
                new int[][]{{1, 5}},
                merge(new int[]{1, 3}, new int[]{3, 5}));
    }

    @Test
    void touchingAtZero() {
        assertArrayEquals(
                new int[][]{{-1, 1}},
                merge(new int[]{-1, 0}, new int[]{0, 1}));
    }

    // ── containment ──────────────────────────────────────────────────────────

    @Test
    void secondContainedInFirst() {
        // [1,10] swallows [3,6]
        assertArrayEquals(
                new int[][]{{1, 10}},
                merge(new int[]{1, 10}, new int[]{3, 6}));
    }

    @Test
    void firstContainedInSecond() {
        assertArrayEquals(
                new int[][]{{1, 10}},
                merge(new int[]{3, 6}, new int[]{1, 10}));
    }

    @Test
    void identicalIntervals() {
        assertArrayEquals(
                new int[][]{{2, 4}},
                merge(new int[]{2, 4}, new int[]{2, 4}));
    }

    // ── chain merges ─────────────────────────────────────────────────────────

    @Test
    void chainOfOverlaps() {
        // each consecutive pair overlaps → all collapse into one
        assertArrayEquals(
                new int[][]{{1, 10}},
                merge(new int[]{1, 4}, new int[]{3, 7}, new int[]{6, 10}));
    }

    @Test
    void leetcodeExample1() {
        // canonical LeetCode example
        assertArrayEquals(
                new int[][]{{1, 6}, {8, 10}, {15, 18}},
                merge(new int[]{1, 3}, new int[]{2, 6}, new int[]{8, 10}, new int[]{15, 18}));
    }

    @Test
    void leetcodeExample2() {
        assertArrayEquals(
                new int[][]{{1, 5}},
                merge(new int[]{1, 4}, new int[]{4, 5}));
    }

    // ── unsorted input ───────────────────────────────────────────────────────

    @Test
    void unsortedInputTwoIntervals() {
        // given in reverse order — result must still be sorted
        assertArrayEquals(
                new int[][]{{1, 6}},
                merge(new int[]{2, 6}, new int[]{1, 3}));
    }

    @Test
    void unsortedInputNoOverlap() {
        assertArrayEquals(
                new int[][]{{1, 2}, {4, 6}},
                merge(new int[]{4, 6}, new int[]{1, 2}));
    }

    @Test
    void unsortedAllContainedInOne() {
        // [1,9] covers everything
        assertArrayEquals(
                new int[][]{{1, 9}},
                merge(new int[]{6, 8}, new int[]{1, 9}, new int[]{2, 4}));
    }

    // ── negative and mixed values ────────────────────────────────────────────

    @Test
    void negativeIntervals() {
        assertArrayEquals(
                new int[][]{{-5, -1}},
                merge(new int[]{-5, -3}, new int[]{-4, -1}));
    }

    @Test
    void mixedNegativeAndPositive() {
        assertArrayEquals(
                new int[][]{{-3, 2}, {5, 7}},
                merge(new int[]{-3, 0}, new int[]{-1, 2}, new int[]{5, 7}));
    }

    @Test
    void spanningZero() {
        // single interval that crosses zero
        assertArrayEquals(
                new int[][]{{-2, 3}},
                merge(new int[]{-2, 1}, new int[]{0, 3}));
    }

    // ── output is sorted ─────────────────────────────────────────────────────

    @Test
    void outputSortedAfterMerge() {
        // three disjoint intervals given out of order
        assertArrayEquals(
                new int[][]{{1, 3}, {5, 7}, {9, 11}},
                merge(new int[]{9, 11}, new int[]{1, 3}, new int[]{5, 7}));
    }

    @Test
    void outputSortedWithSomeMerges() {
        // [3,5]+[4,6] merge; [1,2] stays separate; result must be sorted
        assertArrayEquals(
                new int[][]{{1, 2}, {3, 6}},
                merge(new int[]{4, 6}, new int[]{1, 2}, new int[]{3, 5}));
    }
}
