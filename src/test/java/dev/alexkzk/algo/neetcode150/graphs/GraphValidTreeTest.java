package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class GraphValidTreeTest {
    private final GraphValidTree sol = new GraphValidTree();

    @Test
    void example1() {
        assertTrue(sol.validTree(5, new int[][]{{0,1},{0,2},{0,3},{1,4}}));
    }

    @Test
    void example2() {
        assertFalse(sol.validTree(5, new int[][]{{0,1},{1,2},{2,3},{1,3},{1,4}}));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.validTree(1, new int[][]{}));
    }
}
