package dev.alexkzk.algo.neetcode150.two_pointers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ContainerWithMostWaterTest {
    private final ContainerWithMostWater sol = new ContainerWithMostWater();

    @Test
    void example1() {
        assertEquals(49, sol.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.maxArea(new int[]{1, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.maxArea(new int[]{0, 0}));
    }
}
