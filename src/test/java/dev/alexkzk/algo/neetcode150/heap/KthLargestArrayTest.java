package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class KthLargestArrayTest {
    private final KthLargestArray sol = new KthLargestArray();

    @Test
    void example1() {
        assertEquals(5, sol.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
    }

    @Test
    void example2() {
        assertEquals(4, sol.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.findKthLargest(new int[]{1}, 1));
    }
}
