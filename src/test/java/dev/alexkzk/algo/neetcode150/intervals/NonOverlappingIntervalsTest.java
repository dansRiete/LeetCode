package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NonOverlappingIntervalsTest {
    private final NonOverlappingIntervals sol = new NonOverlappingIntervals();

    @Test
    void example1() {
        assertEquals(1, sol.eraseOverlapIntervals(new int[][]{{1,2},{2,3},{3,4},{1,3}}));
    }

    @Test
    void example2() {
        assertEquals(2, sol.eraseOverlapIntervals(new int[][]{{1,2},{1,2},{1,2}}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.eraseOverlapIntervals(new int[][]{{1,2},{2,3}}));
    }
}
