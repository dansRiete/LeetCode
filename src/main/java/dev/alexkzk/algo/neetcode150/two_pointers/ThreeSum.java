package dev.alexkzk.algo.neetcode150.two_pointers;

import java.util.*;
import java.util.stream.Stream;

public class ThreeSum {
    /** LC #15 — 3Sum [Medium] */
    public List<List<Integer>> threeSum(int[] nums) {
        int[] sortedNums = nums;
        Arrays.sort(sortedNums);
        Set<List<Integer>> result = new HashSet<>();
        for(int i = 0; i < sortedNums.length; i++) {
            int seek = 0 - sortedNums[i];
            for(int start = 0, end = sortedNums.length -1; end > start;) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;
//                System.out.println(String.format("i:%d, start:%d, end:%d, nums[i]:%d, nums[start]:%d, nums[end]:%d, seek:%d", i, start, end, sortedNums[i], sortedNums[start], sortedNums[end], seek));
                if(sortedNums[start] + sortedNums[end] > seek) {
                    end--;
                }else if(sortedNums[start] + sortedNums[end] < seek) {
                    start++;
                } else {
                    result.add(List.of(nums[i], nums[start], nums[end]));
                    while (start < end && nums[start] == nums[start + 1]) start++;
                    while (start < end && nums[end] == nums[end - 1]) end--;
                    start++;
                    end--;
                }
            }
        }
        return new ArrayList<>(result);
    }

    /** O(n²) time, O(1) extra space — no HashSet, deduplication via pointer skipping */
    public static List<List<Integer>> threeSum2(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int start = i + 1, end = nums.length - 1;
            while (start < end) {
                int sum = nums[i] + nums[start] + nums[end];
                if (sum > 0) {
                    end--;
                } else if (sum < 0) {
                    start++;
                } else {
                    result.add(List.of(nums[i], nums[start], nums[end]));
                    while (start < end && nums[start] == nums[start + 1]) start++;
                    while (start < end && nums[end] == nums[end - 1]) end--;
                    start++;
                    end--;
                }
            }
        }
        return result;  //  [[-1, -1, 2], [-1, 0, 1]]
    }

    public static void main(String[] args) {
        ThreeSum threeSum = new ThreeSum();
        System.out.println(threeSum.threeSum2(new int[]{-1, 0, 1, 2, -1, -4}));
    }
}
