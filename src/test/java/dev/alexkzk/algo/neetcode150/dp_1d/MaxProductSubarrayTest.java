package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MaxProductSubarrayTest {
    private final MaxProductSubarray sol = new MaxProductSubarray();

    @Test
    void example1() {
        assertEquals(6, sol.maxProduct(new int[]{2, 3, -2, 4}));
    }

    @Test
    void example2() {
        assertEquals(0, sol.maxProduct(new int[]{-2, 0, -1}));
    }

    @Test
    void edgeCase() {
        assertEquals(-2, sol.maxProduct(new int[]{-2}));
    }
}
