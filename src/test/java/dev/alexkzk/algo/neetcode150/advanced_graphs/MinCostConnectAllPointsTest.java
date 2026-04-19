package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MinCostConnectAllPointsTest {
    private final MinCostConnectAllPoints sol = new MinCostConnectAllPoints();

    @Test
    void example1() {
        assertEquals(20, sol.minCostConnectPoints(new int[][]{{0,0},{2,2},{3,10},{5,2},{7,0}}));
    }

    @Test
    void example2() {
        assertEquals(18, sol.minCostConnectPoints(new int[][]{{3,12},{-2,5},{-4,1}}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.minCostConnectPoints(new int[][]{{0,0}}));
    }
}
