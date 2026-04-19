package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class RedundantConnectionTest {
    private final RedundantConnection sol = new RedundantConnection();

    @Test
    void example1() {
        assertArrayEquals(new int[]{2,3}, sol.findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}}));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{1,4}, sol.findRedundantConnection(new int[][]{{1,2},{2,3},{3,4},{1,4},{1,5}}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{1,2}, sol.findRedundantConnection(new int[][]{{1,2},{1,2}}));
    }
}
