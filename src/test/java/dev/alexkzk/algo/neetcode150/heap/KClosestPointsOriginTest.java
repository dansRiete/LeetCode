package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class KClosestPointsOriginTest {
    private final KClosestPointsOrigin sol = new KClosestPointsOrigin();

    @Test
    void example1() {
        int[][] result = sol.kClosest(new int[][]{{1, 3}, {-2, 2}}, 1);
        assertEquals(1, result.length);
        assertArrayEquals(new int[]{-2, 2}, result[0]);
    }

    @Test
    void example2() {
        int[][] result = sol.kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2);
        assertEquals(2, result.length);
    }

    @Test
    void edgeCase() {
        int[][] result = sol.kClosest(new int[][]{{0, 0}}, 1);
        assertArrayEquals(new int[]{0, 0}, result[0]);
    }
}
