package dev.alexkzk.algo.neetcode150.two_pointers;

import java.util.*;
import java.util.stream.Stream;

public class ThreeSum {
    /** LC #15 — 3Sum [Medium] */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < nums.length - 2; i++) {
            if(i > 0 && nums[i] == nums[i-1]) continue;
            for(int l = i + 1, r = nums.length - 1; l < r;) {
                int outer = nums[i];
                int left = nums[l];
                int right = nums[r];
                int sum = outer + left + right;
                if(sum < 0) {
                    l++;
                } else if(sum > 0) {
                    r--;
                } else {
                    result.add(List.of(nums[i], nums[l], nums[r]));
                    l++;
                    r--;
                    while (l < r && nums[l] == nums[l-1]) l++;
                    while (l < r && nums[r] == nums[r+1]) r--;
                }
            }
        }
        return result;
    }

    /** O(n²) time, O(1) extra space — no HashSet, deduplication via pointer skipping */
    public static List<List<Integer>> threeSumReference(int[] nums) {
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
        System.out.println(threeSum.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }
}
