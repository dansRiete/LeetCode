package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SlidingWindowMaximumTest {
    private final SlidingWindowMaximum sol = new SlidingWindowMaximum();

    @Test
    void example1() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, sol.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{1}, sol.maxSlidingWindow(new int[]{1}, 1));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{3, 3}, sol.maxSlidingWindow(new int[]{1, 3, 1}, 2));
    }
}
