package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestPalindromicSubstringTest {
    private final LongestPalindromicSubstring sol = new LongestPalindromicSubstring();

    @Test
    void example1() {
        String result = sol.longestPalindrome("babad");
        assertTrue(result.equals("bab") || result.equals("aba"));
    }

    @Test
    void example2() {
        assertEquals("bb", sol.longestPalindrome("cbbd"));
    }

    @Test
    void edgeCase() {
        assertEquals("a", sol.longestPalindrome("a"));
    }
}
