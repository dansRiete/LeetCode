package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CourseScheduleIITest {
    private final CourseScheduleII sol = new CourseScheduleII();

    @Test
    void example1() {
        int[] result = sol.findOrder(2, new int[][]{{1,0}});
        assertArrayEquals(new int[]{0, 1}, result);
    }

    @Test
    void example2() {
        int[] result = sol.findOrder(4, new int[][]{{1,0},{2,0},{3,1},{3,2}});
        assertEquals(4, result.length);
    }

    @Test
    void edgeCase() {
        int[] result = sol.findOrder(2, new int[][]{{1,0},{0,1}});
        assertEquals(0, result.length);
    }
}
