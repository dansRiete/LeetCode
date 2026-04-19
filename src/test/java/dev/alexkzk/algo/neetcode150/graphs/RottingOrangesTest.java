package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class RottingOrangesTest {
    private final RottingOranges sol = new RottingOranges();

    @Test
    void example1() {
        assertEquals(4, sol.orangesRotting(new int[][]{{2,1,1},{1,1,0},{0,1,1}}));
    }

    @Test
    void example2() {
        assertEquals(-1, sol.orangesRotting(new int[][]{{2,1,1},{0,1,1},{1,0,1}}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.orangesRotting(new int[][]{{0,2}}));
    }
}
