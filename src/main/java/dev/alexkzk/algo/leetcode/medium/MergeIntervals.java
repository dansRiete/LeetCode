package dev.alexkzk.algo.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LEETCODE #56 — MERGE INTERVALS  [Medium] | Expected: ~20 min
 * ──────────────────────────────────────────────────────────────
 * MERGE INTERVALS
 * ───────────────
 * Given an array of intervals where intervals[i] = [start_i, end_i], merge all
 * overlapping intervals and return an array of the non-overlapping intervals that
 * together cover all intervals in the input.
 *
 * Two intervals are considered overlapping (and must be merged) if one starts
 * before or exactly when the other ends — i.e. they share at least one point.
 * Touching intervals such as [1,3] and [3,5] must be merged into [1,5].
 *
 * The input may be given in any order. The output must be sorted by start value.
 *
 * Examples:
 *   [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
 *   [[1,4],[4,5]]               → [[1,5]]     (touching)
 *   [[1,4],[2,3]]               → [[1,4]]     (contained)
 *   [[1,4],[5,6]]               → [[1,4],[5,6]] (gap — no merge)
 *   [[6,8],[1,9],[2,4]]         → [[1,9]]     (unsorted, all contained)
 *   []                          → []
 *
 * Constraints:
 *   - 0 <= intervals.length <= 10^4
 *   - intervals[i].length == 2
 *   - start_i <= end_i
 *   - Values may be negative.
 */
public class MergeIntervals {

    /**
     * Merges all overlapping intervals.
     *
     * @param intervals array of [start, end] pairs, in any order
     * @return merged intervals sorted by start; empty array if input is empty
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return new int[0][];
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                merged.add(current);
                current = intervals[i];
            }
        }
        merged.add(current);
        return merged.toArray(new int[merged.size()][]);
    }




    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(merge(new int[][]{{2,6},{15,18},{1,3},{8,10}})));
        // expected: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
    }
}
