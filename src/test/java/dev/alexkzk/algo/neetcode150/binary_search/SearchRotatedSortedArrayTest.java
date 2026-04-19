package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SearchRotatedSortedArrayTest {
    private final SearchRotatedSortedArray sol = new SearchRotatedSortedArray();

    @Test
    void example1() {
        assertEquals(4, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
    }

    @Test
    void example2() {
        assertEquals(-1, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
    }

    @Test
    void edgeCase() {
        assertEquals(-1, sol.search(new int[]{1}, 0));
    }
}
