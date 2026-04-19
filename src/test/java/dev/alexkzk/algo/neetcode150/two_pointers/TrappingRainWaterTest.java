package dev.alexkzk.algo.neetcode150.two_pointers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TrappingRainWaterTest {
    private final TrappingRainWater sol = new TrappingRainWater();

    @Test
    void example1() {
        assertEquals(6, sol.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }

    @Test
    void example2() {
        assertEquals(9, sol.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.trap(new int[]{1, 2, 3}));
    }
}
