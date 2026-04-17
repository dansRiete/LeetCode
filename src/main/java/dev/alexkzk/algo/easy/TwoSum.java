package dev.alexkzk.algo.easy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    /**
     * Given an array of integers and a target, return indices of the two numbers
     * that add up to the target. Exactly one solution exists, same element
     * cannot be used twice.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   nums = [2, 7, 11, 15], target = 9  →  [0, 1]
     *   nums = [3, 2, 4],      target = 6  →  [1, 2]
     *   nums = [3, 3],         target = 6  →  [0, 1]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 2 <= nums.length <= 10^4}</li>
     *   <li>{@code -10^9 <= nums[i] <= 10^9}</li>
     *   <li>Exactly one valid answer exists</li>
     * </ul>
     *
     * @param nums   array of integers
     * @param target target sum
     * @return indices of the two numbers that add up to target
     */
    public static int[] twoSumOn2(int[] nums, int target) {
        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j < nums.length; j++) {
                if(i == j) {
                    continue;
                }
                if(nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        numMap.put(nums[0], 0);
        for(int i = 1; i < nums.length; i++) {
            numMap.putIfAbsent(nums[i], i);
            if(numMap.get(target - nums[i]) != null) {
                return new int[]{numMap.get(target - nums[i]), i};
            }
        }
        System.out.println("null");
        return null;

    }

    public static void main(String[] args) {
//        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
        // expected: [0, 1]

//        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));
        // expected: [1, 2]

//        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));
        System.out.println(Arrays.toString(twoSum(new int[]{3, 3}, 6)));

//        System.out.println(Arrays.toString(twoSum(new int[]{3, 3}, 6)));
        // expected: [0, 1]
    }
}
