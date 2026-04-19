package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NumberConnectedComponentsTest {
    private final NumberConnectedComponents sol = new NumberConnectedComponents();

    @Test
    void example1() {
        assertEquals(2, sol.countComponents(5, new int[][]{{0,1},{1,2},{3,4}}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.countComponents(5, new int[][]{{0,1},{1,2},{2,3},{3,4}}));
    }

    @Test
    void edgeCase() {
        assertEquals(3, sol.countComponents(3, new int[][]{}));
    }
}
