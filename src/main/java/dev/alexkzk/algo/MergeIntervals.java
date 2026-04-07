package dev.alexkzk.algo;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
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
        Map<Integer, int[]> sortedArrays = new TreeMap<>();
        for (int[] arr : intervals) {
            sortedArrays.put(arr[0], arr);
        }
        int[][] sortedIntervals = sortedArrays.values().toArray(new int[sortedArrays.size()][]);
        for(int i = 0; i < sortedIntervals.length; i++) {
            if(i + 1 < sortedIntervals.length) {
                int[] mergedArr = mergeArrays(sortedIntervals[i], sortedIntervals[i + 1]);
                if(mergedArr != null) {
                    int[][] result = new int[intervals.length - 1][];
                    intervals[i+1] = mergedArr;
                    System.arraycopy(intervals, 0, result, 0, i);
                    System.arraycopy(intervals, i + 1, result, i, intervals.length - i - 1);
                    intervals = result;
                    i = 0;
                }
            }
        }
        print(intervals);
        return null;
    }

    static void print(int[][] arr) {
        System.out.println(Arrays.deepToString(arr));
    }

    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        if (arr2[0] >= arr1[0] || arr2[1] <= arr1[1]) {
            return new int[]{arr1[0], Math.max(arr1[1], arr2[1])};
        }
        return null;
    }




    public static void main(String[] args) {
        merge(new int[][]{{2,6},{15,18},{1,3},{8,10}});
    }
}
