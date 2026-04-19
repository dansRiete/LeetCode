package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MinIntervalIncludeQueryTest {
    private final MinIntervalIncludeQuery sol = new MinIntervalIncludeQuery();

    @Test
    void example1() {
        assertArrayEquals(new int[]{3,-1,3,6}, sol.minInterval(new int[][]{{1,4},{2,4},{3,6},{4,4}}, new int[]{2,3,4,5}));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{2,-1,4,6}, sol.minInterval(new int[][]{{2,3},{2,5},{1,8},{20,25}}, new int[]{2,19,22,6}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{-1}, sol.minInterval(new int[][]{{1,4}}, new int[]{5}));
    }
}
