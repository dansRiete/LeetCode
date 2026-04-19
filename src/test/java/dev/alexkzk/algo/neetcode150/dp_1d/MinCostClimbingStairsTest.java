package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MinCostClimbingStairsTest {
    private final MinCostClimbingStairs sol = new MinCostClimbingStairs();

    @Test
    void example1() {
        assertEquals(15, sol.minCostClimbingStairs(new int[]{10, 15, 20}));
    }

    @Test
    void example2() {
        assertEquals(6, sol.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.minCostClimbingStairs(new int[]{0, 0}));
    }
}
