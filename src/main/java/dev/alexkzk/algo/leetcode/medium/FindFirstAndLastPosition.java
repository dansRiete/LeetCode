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
        int index = findIndex(nums, target);
        if(index == -1) {
            return new int[]{-1, -1};
        } else {
            boolean leftFound = false;
            boolean rightFound = false;
            for(int l = index - 1, r = index + 1;;) {
                if(!leftFound) {
                    if(nums[l] == target) {
                        l--;
                        if(l <= 0) {
                            leftFound = true;
                        }
                    } else {
                        l++;
                        leftFound = true;
                    }
                }
                if(!rightFound) {
                    if(nums[r] == target) {
                        r++;
                        if(r <= nums.length - 1) {
                            rightFound = true;
                        }
                    } else {
                        r--;
                        rightFound = true;
                    }
                }
                if(leftFound && rightFound) {
                    return new int[]{l, r};
                }
            }
        }
//        return new int[]{-1, -1};
    }

    private static int findIndex(int[] nums, int target) {
        for(int l = 0, r = nums.length - 1; ;) {
            System.out.println("l=" + l + ", r=" + r);
            int index = l + ((r - l) / 2);
            if(nums[index] == target) {
                return index;
            } else if (nums[index] < target) {
                l = index + 1;
            }  else if (nums[index] > target) {
                r = index - 1;
            }
            if(r<=l) {
                break;
            }
        }
        return -1;
    }
}