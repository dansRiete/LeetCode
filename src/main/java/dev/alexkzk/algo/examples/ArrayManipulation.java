package dev.alexkzk.algo.examples;

import java.util.Arrays;

public class ArrayManipulation {

    static int[] src = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static void main(String[] args) {
        int indexToRemove = 2; // third element
        int[] dst = new int[src.length - 1];
        System.arraycopy(src, 0, dst, 0, indexToRemove);
        System.arraycopy(src, indexToRemove + 1, dst, indexToRemove, src.length - 1 - indexToRemove);
        System.out.println(Arrays.toString(dst));
    }

}
