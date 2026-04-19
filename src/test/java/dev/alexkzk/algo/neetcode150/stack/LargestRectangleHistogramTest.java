package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LargestRectangleHistogramTest {
    private final LargestRectangleHistogram sol = new LargestRectangleHistogram();

    @Test
    void example1() {
        assertEquals(10, sol.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }

    @Test
    void example2() {
        assertEquals(4, sol.largestRectangleArea(new int[]{2, 4}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.largestRectangleArea(new int[]{1}));
    }
}
