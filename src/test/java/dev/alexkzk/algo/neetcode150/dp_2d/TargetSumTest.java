package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TargetSumTest {
    private final TargetSum sol = new TargetSum();

    @Test
    void example1() {
        assertEquals(5, sol.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
    }

    @Test
    void example2() {
        assertEquals(1, sol.findTargetSumWays(new int[]{1}, 1));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.findTargetSumWays(new int[]{100}, 1));
    }
}
