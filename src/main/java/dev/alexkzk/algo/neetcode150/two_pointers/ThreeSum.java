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
                if(start == i) {
                    start++;
                    continue;
                }
                if(end == i) {
                    end--;
                    continue;
                }
                System.out.println(String.format("i:%d, start:%d, end:%d, nums[i]:%d, nums[start]:%d, nums[end]:%d, seek:%d", i, start, end, sortedNums[i], sortedNums[start], sortedNums[end], seek));
                if(sortedNums[start] + sortedNums[end] > seek) {
                    end--;
                }else if(sortedNums[start] + sortedNums[end] < seek) {
                    start++;
                } else if(sortedNums[start] + sortedNums[end] == seek) {
                    result.add(Stream.of(sortedNums[i], sortedNums[start], sortedNums[end]).sorted().toList());
                    System.out.println("break");
                    break;
                }
            }
        }
        return new ArrayList<>(result);

    }
}
