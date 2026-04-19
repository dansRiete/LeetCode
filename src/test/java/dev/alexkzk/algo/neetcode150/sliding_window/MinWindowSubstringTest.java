package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MinWindowSubstringTest {
    private final MinWindowSubstring sol = new MinWindowSubstring();

    @Test
    void example1() {
        assertEquals("BANC", sol.minWindow("ADOBECODEBANC", "ABC"));
    }

    @Test
    void example2() {
        assertEquals("a", sol.minWindow("a", "a"));
    }

    @Test
    void edgeCase() {
        assertEquals("", sol.minWindow("a", "aa"));
    }
}
