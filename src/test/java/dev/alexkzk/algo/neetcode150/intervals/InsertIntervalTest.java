package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class InsertIntervalTest {
    private final InsertInterval sol = new InsertInterval();

    @Test
    void example1() {
        int[][] result = sol.insert(new int[][]{{1,3},{6,9}}, new int[]{2,5});
        assertArrayEquals(new int[][]{{1,5},{6,9}}, result);
    }

    @Test
    void example2() {
        int[][] result = sol.insert(new int[][]{{1,2},{3,5},{6,7},{8,10},{12,16}}, new int[]{4,8});
        assertArrayEquals(new int[][]{{1,2},{3,10},{12,16}}, result);
    }

    @Test
    void edgeCase() {
        int[][] result = sol.insert(new int[][]{}, new int[]{5,7});
        assertArrayEquals(new int[][]{{5,7}}, result);
    }
}
