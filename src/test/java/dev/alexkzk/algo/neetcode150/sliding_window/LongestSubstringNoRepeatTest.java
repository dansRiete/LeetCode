package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LongestSubstringNoRepeatTest {
    private final LongestSubstringNoRepeat sol = new LongestSubstringNoRepeat();

    @Test
    void example1() {
        assertEquals(3, sol.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void example2_allSameChar() {
        assertEquals(1, sol.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void emptyString() {
        assertEquals(0, sol.lengthOfLongestSubstring(""));
    }

    @Test
    void singleChar() {
        assertEquals(1, sol.lengthOfLongestSubstring("a"));
    }

    @Test
    void twoDistinctChars() {
        assertEquals(2, sol.lengthOfLongestSubstring("ab"));
    }

    @Test
    void twoSameChars() {
        assertEquals(1, sol.lengthOfLongestSubstring("aa"));
    }

    @Test
    void allUniqueChars() {
        assertEquals(6, sol.lengthOfLongestSubstring("abcdef"));
    }

    @Test
    void repeatAtEnd() {
        // optimal window is at the start: "abc"
        assertEquals(3, sol.lengthOfLongestSubstring("abca"));
    }

    @Test
    void repeatAtStart() {
        // optimal window starts after the first duplicate: "abcde"
        assertEquals(5, sol.lengthOfLongestSubstring("aabcde"));
    }

    @Test
    void betterWindowAfterDuplicate() {
        // "abcbef": duplicate b at index 3, best window is "cbef" = 4
        assertEquals(4, sol.lengthOfLongestSubstring("abcbef"));
    }

    @Test
    void pwwkew() {
        assertEquals(3, sol.lengthOfLongestSubstring("pwwkew"));
    }

    @Test
    void specialCharacters() {
        assertEquals(5, sol.lengthOfLongestSubstring("a b!c b"));
    }

    @Test
    void pangram() {
        assertEquals(14, sol.lengthOfLongestSubstring("thequickbrownfoxjumpsoverthelazydog"));
    }

    @Test
    void doubledPangram() {
        assertEquals(17, sol.lengthOfLongestSubstring("thequickbrownfoxjumpsoverthelazydogthequickbrownfoxjumpsovert"));
    }
}
