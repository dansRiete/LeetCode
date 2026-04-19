package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MedianTwoSortedArraysTest {
    private final MedianTwoSortedArrays sol = new MedianTwoSortedArrays();

    @Test
    void example1() {
        assertEquals(2.0, sol.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
    }

    @Test
    void example2() {
        assertEquals(2.5, sol.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
    }

    @Test
    void edgeCase() {
        assertEquals(1.0, sol.findMedianSortedArrays(new int[]{}, new int[]{1}));
    }
}
