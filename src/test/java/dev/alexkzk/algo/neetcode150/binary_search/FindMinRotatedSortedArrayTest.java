package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class FindMinRotatedSortedArrayTest {
    private final FindMinRotatedSortedArray sol = new FindMinRotatedSortedArray();

    @Test
    void example1() {
        assertEquals(1, sol.findMin(new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    void example2() {
        assertEquals(0, sol.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.findMin(new int[]{1}));
    }
}
