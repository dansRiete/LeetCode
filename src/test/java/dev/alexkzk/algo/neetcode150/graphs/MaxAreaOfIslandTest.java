package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MaxAreaOfIslandTest {
    private final MaxAreaOfIsland sol = new MaxAreaOfIsland();

    @Test
    void example1() {
        int[][] grid = {{0,0,1,0,0,0,0,1,0,0,0,0,0},{0,0,0,0,0,0,0,1,1,1,0,0,0},{0,1,1,0,1,0,0,0,0,0,0,0,0},{0,1,0,0,1,1,0,0,1,0,1,0,0},{0,1,0,0,1,1,0,0,1,1,1,0,0},{0,0,0,0,0,0,0,0,0,0,1,0,0},{0,0,0,0,0,0,0,1,1,1,0,0,0},{0,0,0,0,0,0,0,1,1,0,0,0,0}};
        assertEquals(6, sol.maxAreaOfIsland(grid));
    }

    @Test
    void example2() {
        assertEquals(0, sol.maxAreaOfIsland(new int[][]{{0,0,0,0,0,0,0,0}}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.maxAreaOfIsland(new int[][]{{1}}));
    }
}
