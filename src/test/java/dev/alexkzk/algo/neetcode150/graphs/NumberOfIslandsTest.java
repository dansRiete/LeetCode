package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NumberOfIslandsTest {
    private final NumberOfIslands sol = new NumberOfIslands();

    @Test
    void example1() {
        char[][] grid = {{'1','1','1','1','0'},{'1','1','0','1','0'},{'1','1','0','0','0'},{'0','0','0','0','0'}};
        assertEquals(1, sol.numIslands(grid));
    }

    @Test
    void example2() {
        char[][] grid = {{'1','1','0','0','0'},{'1','1','0','0','0'},{'0','0','1','0','0'},{'0','0','0','1','1'}};
        assertEquals(3, sol.numIslands(grid));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.numIslands(new char[][]{{'0'}}));
    }
}
