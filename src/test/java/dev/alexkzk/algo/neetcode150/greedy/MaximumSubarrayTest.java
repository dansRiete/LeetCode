package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MaximumSubarrayTest {
    private final MaximumSubarray sol = new MaximumSubarray();

    @Test
    void example1() {
        assertEquals(6, sol.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.maxSubArray(new int[]{1}));
    }

    @Test
    void edgeCase() {
        assertEquals(-1, sol.maxSubArray(new int[]{-5, -1, -3}));
    }
}
