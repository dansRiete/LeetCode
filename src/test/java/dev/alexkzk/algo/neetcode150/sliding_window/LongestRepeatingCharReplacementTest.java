package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestRepeatingCharReplacementTest {
    private final LongestRepeatingCharReplacement sol = new LongestRepeatingCharReplacement();

    @Test
    void example1() {
        assertEquals(4, sol.characterReplacement("ABAB", 2));
    }

    @Test
    void example2() {
        assertEquals(4, sol.characterReplacement("AABABBA", 1));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.characterReplacement("A", 0));
    }
}
