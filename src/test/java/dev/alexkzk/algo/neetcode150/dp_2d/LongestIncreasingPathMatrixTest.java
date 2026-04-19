package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestIncreasingPathMatrixTest {
    private final LongestIncreasingPathMatrix sol = new LongestIncreasingPathMatrix();

    @Test
    void example1() {
        assertEquals(4, sol.longestIncreasingPath(new int[][]{{9,9,4},{6,6,8},{2,1,1}}));
    }

    @Test
    void example2() {
        assertEquals(4, sol.longestIncreasingPath(new int[][]{{3,4,5},{3,2,6},{2,2,1}}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.longestIncreasingPath(new int[][]{{1}}));
    }
}
