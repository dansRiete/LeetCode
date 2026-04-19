package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MergeTripletsFormTargetTest {
    private final MergeTripletsFormTarget sol = new MergeTripletsFormTarget();

    @Test
    void example1() {
        assertTrue(sol.mergeTriplets(new int[][]{{2,5,3},{1,8,4},{1,7,5}}, new int[]{2,7,5}));
    }

    @Test
    void example2() {
        assertFalse(sol.mergeTriplets(new int[][]{{3,4,5},{4,5,6}}, new int[]{3,2,5}));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.mergeTriplets(new int[][]{{5,5,5}}, new int[]{5,5,5}));
    }
}
