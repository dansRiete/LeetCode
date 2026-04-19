package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestConsecutiveSequenceTest {
    private final LongestConsecutiveSequence sol = new LongestConsecutiveSequence();

    @Test
    void example1() {
        assertEquals(4, sol.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }

    @Test
    void example2() {
        assertEquals(9, sol.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.longestConsecutive(new int[]{}));
    }
}
