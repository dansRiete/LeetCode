package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MergeIntervalsTest {

    private static int[][] solve(int[]... intervals) {
        return MergeIntervals.merge(intervals);
    }

    private static void assertIntervals(int[][] expected, int[][] actual) {
        assertNotNull(actual);
        assertEquals(expected.length, actual.length, "wrong number of merged intervals");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i],
                    "interval[" + i + "] mismatch");
        }
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertIntervals(
                new int[][]{{1, 6}, {8, 10}, {15, 18}},
                solve(new int[]{1, 3}, new int[]{2, 6}, new int[]{8, 10}, new int[]{15, 18})
        );
    }

    @Test
    void leetcodeExample2_touching() {
        assertIntervals(
                new int[][]{{1, 5}},
                solve(new int[]{1, 4}, new int[]{4, 5})
        );
    }

    // ── single interval ───────────────────────────────────────────────────────

    @Test
    void singleInterval() {
        assertIntervals(new int[][]{{3, 7}}, solve(new int[]{3, 7}));
    }

    // ── empty input ───────────────────────────────────────────────────────────

    @Test
    void emptyInput() {
        assertEquals(0, MergeIntervals.merge(new int[0][]).length);
    }

    // ── no overlap ───────────────────────────────────────────────────────────

    @Test
    void noOverlap() {
        assertIntervals(
                new int[][]{{1, 2}, {5, 6}, {10, 11}},
                solve(new int[]{1, 2}, new int[]{5, 6}, new int[]{10, 11})
        );
    }

    @Test
    void gapBetweenIntervals() {
        // [1,4] and [5,6] have a gap (4 < 5) — should NOT merge
        assertIntervals(
                new int[][]{{1, 4}, {5, 6}},
                solve(new int[]{1, 4}, new int[]{5, 6})
        );
    }

    // ── full containment ──────────────────────────────────────────────────────

    @Test
    void oneContainsAnother() {
        // [1,4] fully contains [2,3]
        assertIntervals(
                new int[][]{{1, 4}},
                solve(new int[]{1, 4}, new int[]{2, 3})
        );
    }

    @Test
    void largerContainsSmaller_reversed() {
        assertIntervals(
                new int[][]{{1, 9}},
                solve(new int[]{6, 8}, new int[]{1, 9}, new int[]{2, 4})
        );
    }

    // ── unsorted input ────────────────────────────────────────────────────────

    @Test
    void unsortedInput() {
        assertIntervals(
                new int[][]{{1, 6}, {8, 10}, {15, 18}},
                solve(new int[]{8, 10}, new int[]{15, 18}, new int[]{1, 3}, new int[]{2, 6})
        );
    }

    // ── all merge into one ────────────────────────────────────────────────────

    @Test
    void allMergeIntoOne() {
        assertIntervals(
                new int[][]{{1, 10}},
                solve(new int[]{1, 4}, new int[]{2, 6}, new int[]{5, 10})
        );
    }

    // ── duplicate starts ──────────────────────────────────────────────────────

    @Test
    void duplicateStartValues() {
        // Two intervals starting at 1 — both must be kept (TreeMap-based approach would lose one)
        assertIntervals(
                new int[][]{{1, 5}},
                solve(new int[]{1, 3}, new int[]{1, 5})
        );
    }

    // ── identical intervals ───────────────────────────────────────────────────

    @Test
    void identicalIntervals() {
        assertIntervals(
                new int[][]{{2, 4}},
                solve(new int[]{2, 4}, new int[]{2, 4})
        );
    }

    // ── two touching but not overlapping ─────────────────────────────────────

    @Test
    void touchingIntervalsMerge() {
        // [1,3] and [3,5] share endpoint 3 → merge to [1,5]
        assertIntervals(
                new int[][]{{1, 5}},
                solve(new int[]{1, 3}, new int[]{3, 5})
        );
    }

    // ── result is sorted by start ────────────────────────────────────────────

    @Test
    void outputIsSortedByStart() {
        int[][] result = solve(new int[]{10, 12}, new int[]{1, 2}, new int[]{5, 7});
        assertEquals(3, result.length);
        assertEquals(1, result[0][0]);
        assertEquals(5, result[1][0]);
        assertEquals(10, result[2][0]);
    }
}
