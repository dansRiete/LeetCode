package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestIncreasingSubsequenceTest {
    private final LongestIncreasingSubsequence sol = new LongestIncreasingSubsequence();

    @Test
    void example1() {
        assertEquals(4, sol.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
    }

    @Test
    void example2() {
        assertEquals(4, sol.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.lengthOfLIS(new int[]{7, 7, 7, 7}));
    }
}
