package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MergeIntervalsTest {
    private final MergeIntervals sol = new MergeIntervals();

    @Test
    void example1() {
        assertArrayEquals(new int[][]{{1,6},{8,10},{15,18}}, sol.merge(new int[][]{{1,3},{2,6},{8,10},{15,18}}));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[][]{{1,5}}, sol.merge(new int[][]{{1,4},{4,5}}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[][]{{1,4}}, sol.merge(new int[][]{{1,4}}));
    }
}
