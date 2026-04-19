package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestCommonSubsequenceTest {
    private final LongestCommonSubsequence sol = new LongestCommonSubsequence();

    @Test
    void example1() {
        assertEquals(3, sol.longestCommonSubsequence("abcde", "ace"));
    }

    @Test
    void example2() {
        assertEquals(3, sol.longestCommonSubsequence("abc", "abc"));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.longestCommonSubsequence("abc", "def"));
    }
}
