package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CourseScheduleTest {
    private final CourseSchedule sol = new CourseSchedule();

    @Test
    void example1() {
        assertTrue(sol.canFinish(2, new int[][]{{1,0}}));
    }

    @Test
    void example2() {
        assertFalse(sol.canFinish(2, new int[][]{{1,0},{0,1}}));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.canFinish(1, new int[][]{}));
    }
}
