package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BinarySearchTest {
    private final BinarySearch sol = new BinarySearch();

    @Test
    void example1() {
        assertEquals(4, sol.search(new int[]{-1, 0, 3, 5, 9, 12}, 9));
    }

    @Test
    void example2() {
        assertEquals(-1, sol.search(new int[]{-1, 0, 3, 5, 9, 12}, 2));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.search(new int[]{5}, 5));
    }
}
