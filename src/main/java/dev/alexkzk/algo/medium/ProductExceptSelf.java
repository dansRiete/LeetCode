package dev.alexkzk.algo.medium;

import java.util.Arrays;

public class ProductExceptSelf {

    /**
     * LEETCODE #238 — PRODUCT OF ARRAY EXCEPT SELF  [Medium] | Expected: ~20 min
     * ─────────────────────────────
     * Given an integer array {@code nums}, return an array {@code answer} such that
     * {@code answer[i]} is equal to the product of all elements of {@code nums}
     * <em>except</em> {@code nums[i]}.
     *
     * <p>The product of any prefix or suffix of {@code nums} is guaranteed to fit
     * in a 32-bit integer.
     *
     * <p>You must write an algorithm that runs in <b>O(n)</b> time and without
     * using the division operation.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   [1, 2, 3, 4]  →  [24, 12, 8, 6]
     *   [-1, 1, 0, -3, 3]  →  [0, 0, 9, 0, 0]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 2 <= nums.length <= 10^5}</li>
     *   <li>{@code -30 <= nums[i] <= 30}</li>
     *   <li>The product of any prefix or suffix fits in a 32-bit integer.</li>
     * </ul>
     *
     * @param nums input array
     * @return array where each element is the product of all other elements
     */
    public static int[] productExceptSelf(int[] nums) { // todo improve to O(n)
        if (nums == null || nums.length < 2){
            return new int[]{};
        }
        if (nums.length == 2) {
            return new int[]{nums[1], nums[0]};
        }
        int[] result = new int[nums.length];

        for(int n = 0; n < nums.length; n++){
            System.out.println("n:" + n);
            Integer product = null;
            for (int i = 0; i < nums.length; i++) {
                System.out.println("i:" + i);
                if (i == n) {
                    continue;
                }
//                System.out.println(nums[i]);
                if (product == null){
                    product = nums[i];
                    System.out.println("product was null, setting " + nums[i]);
                    continue;
                }
                System.out.println("n: " + n + ", i: " + i + ", product: " + product + ", nums[i]: " + nums[i]);
                product = product * nums[i];
                result[n] = product;
            }
            System.out.println(Arrays.toString(result));
        }

        return result;
    }

    public static void main(String[] args) {
//        System.out.println("Expected: [30,30], actual:" + Arrays.toString(productExceptSelf(new int[]{30,30})));
        System.out.println("Expected: [7,0], actual:" + Arrays.toString(productExceptSelf(new int[]{0,7})));
//        System.out.println("Expected: [-24, -12, -8, -6], actual:" + Arrays.toString(productExceptSelf(new int[]{-1, -2, -3, -4})));
    }
}
