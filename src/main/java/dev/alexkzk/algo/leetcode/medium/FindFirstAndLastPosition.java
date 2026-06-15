package dev.alexkzk.algo.leetcode.medium;

public class FindFirstAndLastPosition {

    /**
     * LEETCODE #34 — FIND FIRST AND LAST POSITION OF ELEMENT IN SORTED ARRAY  [Medium]
     * ──────────────────────────────────────────────────────────────────────────────────
     * Given a sorted array of integers, return the starting and ending index of a target.
     *
     * <p><b>Problem:</b> Given {@code nums} sorted in non-decreasing order and a {@code target},
     * return {@code [first, last]} indices of {@code target}, or {@code [-1, -1]} if absent.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   nums = [5,7,7,8,8,10], target = 8  →  [3, 4]
     *   nums = [5,7,7,8,8,10], target = 6  →  [-1, -1]
     *   nums = [],              target = 0  →  [-1, -1]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>O(log n) runtime required</li>
     *   <li>{@code 0 <= nums.length <= 10^5}</li>
     *   <li>{@code -10^9 <= nums[i], target <= 10^9}</li>
     * </ul>
     */
    public static int[] searchRange(int[] nums, int target) {
        return new int[]{findFirst(nums, target), findLast(nums, target)};
    }

    private static int findFirst(int[] nums, int target) {
        int l = 0, r = nums.length - 1, result = -1;
        while (l <= r) {
            int i = l + (r - l) / 2;
            if (nums[i] == target) {
                result = i;
                r = i - 1;   // keep searching left
            } else if (nums[i] < target) {
                l = i + 1;
            } else {
                r = i - 1;
            }
        }
        return result;
    }

    private static int findLast(int[] nums, int target) {
        int l = 0, r = nums.length - 1, result = -1;
        while (l <= r) {
            int i = l + (r - l) / 2;
            if (nums[i] == target) {
                result = i;
                l = i + 1;   // keep searching right
            } else if (nums[i] < target) {
                l = i + 1;
            } else {
                r = i - 1;
            }
        }
        return result;
    }
}