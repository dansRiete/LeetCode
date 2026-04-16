package dev.alexkzk.algo.medium;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {

    /**
     * LEETCODE #560 — SUBARRAY SUM EQUALS K  [Medium] | Expected: ~30 min
     * ──────────────────────────────────────────────────
     * Given an array of integers {@code nums} and an integer {@code k},
     * return the total number of subarrays whose sum equals {@code k}.
     *
     * <p>A subarray is a contiguous non-empty sequence of elements within an array.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   nums = [1, 1, 1], k = 2
     *     subarrays: [1,1] at index 0-1, [1,1] at index 1-2  →  count = 2
     *
     *   nums = [1, 2, 3], k = 3
     *     subarrays: [1,2] at index 0-1, [3] at index 2-2    →  count = 2
     *
     *   nums = [1, 2, 1, 2], k = 3
     *     subarrays: [1,2] at 0-1, [2,1] at 1-2, [1,2] at 2-3  →  count = 3
     *
     *   nums = [3], k = 3
     *     subarrays: [3] at 0-0                               →  count = 1
     *
     *   nums = [1, 2, 3], k = 7
     *     no subarray sums to 7                               →  count = 0
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= nums.length <= 2 * 10^4}</li>
     *   <li>{@code -1000 <= nums[i] <= 1000}</li>
     *   <li>{@code -10^7 <= k <= 10^7}</li>
     * </ul>
     *
     * @param nums input array (may contain negative numbers)
     * @param k    target sum
     * @return number of subarrays that sum to {@code k}
     */


    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        freqMap.put(0,1);
        int counter = 0;
        int summ = 0;
        for(int currentNumber : nums) {
            summ += currentNumber;
            counter += freqMap.getOrDefault(summ - k, 0);
            if (freqMap.get(summ) == null) {    //  can be replaced by freqMap.put(sum, freqMap.getOrDefault(sum, 0) + 1);
                freqMap.put(summ, 1);
            } else {
                freqMap.put(summ, freqMap.get(summ) + 1);
            }
        }
        return counter;
    }

    public static int subarraySum2(int[] nums, int k) {
        int count = 0;
        for(int i = 0; i < nums.length; i++) {
            int summ = nums[i];
            if(summ == k) {
                System.out.println("count++ outer loop, i:" + i);
                count++;
                if (i + 1 < nums.length && nums[i + 1] == 0) {
                    count++;
                }
//                continue;
            }
            for(int j = i + 1; j < nums.length; j++) {
//                System.out.println("sub loop, summ: " + summ + ", nums[j]: " + nums[j]);
                summ += nums[j];
                if (summ == k) {
                    System.out.println("count++ inner loop");
                    System.out.println("i:" + i + ", j:" + j);
                   count++;
//                   break;
                }
            }
        }
        return count;
    }

    public static int subarraySum3(int[] nums, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        freqMap.put(0, 1);
        int count = 0;
        int sum = 0;
        for (int num : nums) {
            sum += num;
            count += freqMap.getOrDefault(sum - k, 0);
            freqMap.put(sum, freqMap.getOrDefault(sum, 0) + 1);
        }
        return count;

    }

    public static void main(String[] args) {
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1, 2, 3, 4, 5, 2,3,3}, 3));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,1,1}, 1));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,2,3}, 7));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{3}, 3));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,2,1,2,1,2}, 3));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,2,1,2}, 3));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,0,1}, 1));
//        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{3, -3, 3}, 3));
        System.out.println(new SubarraySumEqualsK().subarraySum(new int[]{1,0,1}, 1));
    }
}
