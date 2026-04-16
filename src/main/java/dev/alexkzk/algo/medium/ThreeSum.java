package dev.alexkzk.algo.medium;

import java.util.List;

public class ThreeSum {

    /**
     * Returns all unique triplets in the array that sum to zero.
     *
     * <p><b>Problem:</b> Given an integer array {@code nums}, return all triplets
     * {@code [nums[i], nums[j], nums[k]]} such that {@code i}, {@code j}, {@code k} are distinct
     * indices and {@code nums[i] + nums[j] + nums[k] == 0}. The result must not contain duplicate
     * triplets.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   nums = [-1, 0, 1, 2, -1, -4]  →  [[-1, -1, 2], [-1, 0, 1]]
     *   nums = [0, 1, 1]               →  []
     *   nums = [0, 0, 0]               →  [[0, 0, 0]]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 3 <= nums.length <= 3000}</li>
     *   <li>{@code -10^5 <= nums[i] <= 10^5}</li>
     * </ul>
     *
     * @param nums input array of integers
     * @return list of all unique triplets that sum to zero
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        // TODO: implement
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
        // expected: [[-1, -1, 2], [-1, 0, 1]]

        System.out.println(threeSum(new int[]{0, 1, 1}));
        // expected: []

        System.out.println(threeSum(new int[]{0, 0, 0}));
        // expected: [[0, 0, 0]]
    }
}
