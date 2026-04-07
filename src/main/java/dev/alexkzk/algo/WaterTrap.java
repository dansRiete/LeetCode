package dev.alexkzk.algo;

import java.util.*;

public class WaterTrap {

    /**
     * Calculates the amount of trapped rainwater.
     *
     * @param height Array of bar heights
     * @return Total units of water trapped
     *
     * Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
     * Output: 6
     */
    public static int trap(int[] height) {
        int summ = 0;
        int[] maxLeftArr = getMaxLeftArr(height);
        int[] maxRightArr = getMaxRightArr(height);

        for (int i = 0; i < height.length; i++) {
            if (i == 0 || i == height.length - 1) {
                continue;
            }

            int currentHeight = height[i];
            int maxLeft = maxLeftArr[i] - currentHeight;
            int maxRight = maxRightArr[i] - currentHeight;
            int water = Math.min(maxLeft, maxRight);

            summ += water < 0 ? 0 : water;
        }

        return summ;
    }

    public static int[] getMaxLeftArr(int[] height) {
        int[] maxLeftArr = new int[height.length];
        int leftMax = 0;

        for (int i = 0; i < height.length; i++) {
            leftMax = Math.max(leftMax, i - 1 < 0 ? 0 : height[i - 1]);
            maxLeftArr[i] = leftMax;
        }

        System.out.println("maxLeftArr:" + Arrays.toString(maxLeftArr));
        return maxLeftArr;
    }

    public static int[] getMaxRightArr(int[] height) {
        int n = height.length;
        int[] maxRightArr = new int[n];
        int currentMax = 0;

        for (int i = n - 1; i >= 0; i--) {
            maxRightArr[i] = currentMax;
            currentMax = Math.max(currentMax, height[i]);
        }

        System.out.println("maxRightArr:" + Arrays.toString(maxRightArr));
        return maxRightArr;
    }

    /*public static int[] getMaxRightArr(int[] height) {    //  WRONG!!! MUST GO FROM THE RIGHT SIDE!!
        int[] maxRightArr = new int[height.length];
        int rightMax = 0;
        for(int i = 0; i < height.length; i++) {
            rightMax = Math.max(rightMax, i+1>=height.length ? 0 : height[i+1]);
            maxRightArr[i] = rightMax;
        }
        return maxRightArr;
    }*/

    public static void main(String[] args) {
        System.out.println(trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }
}
