package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestSubstringNoRepeatTest {
    private final LongestSubstringNoRepeat sol = new LongestSubstringNoRepeat();

    @Test
    void example1() {
        assertEquals(3, sol.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void example2() {
        assertEquals(1, sol.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.lengthOfLongestSubstring(""));
    }
}
