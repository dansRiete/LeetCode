package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PacificAtlanticWaterFlowTest {
    private final PacificAtlanticWaterFlow sol = new PacificAtlanticWaterFlow();

    @Test
    void example1() {
        int[][] heights = {{1,2,2,3,5},{3,2,3,4,4},{2,4,5,3,1},{6,7,1,4,5},{5,1,1,2,4}};
        List<List<Integer>> result = sol.pacificAtlantic(heights);
        assertEquals(7, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.pacificAtlantic(new int[][]{{1}});
        assertEquals(1, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.pacificAtlantic(new int[][]{{1,1},{1,1}});
        assertEquals(4, result.size());
    }
}
