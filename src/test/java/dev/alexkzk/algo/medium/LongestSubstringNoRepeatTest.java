package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestSubstringNoRepeatTest {

    private static int len(String s) {
        return LongestSubstringNoRepeat.lengthOfLongestSubstring(s);
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void emptyString() {
        assertEquals(0, len(""));
    }

    @Test
    void singleChar() {
        assertEquals(1, len("a"));
    }

    @Test
    void allSameChars() {
        // "bbbbb" → longest window is just one "b"
        assertEquals(1, len("bbbbb"));
    }

    @Test
    void twoDistinctChars() {
        assertEquals(2, len("au"));
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        // "abc" is the longest unique window
        assertEquals(3, len("abcabcbb"));
    }

    @Test
    void leetcodeExample2() {
        assertEquals(3, len("pwwkew"));
    }

    // ── sliding window correctness ───────────────────────────────────────────

    @Test
    void duplicateAfterGap() {
        // "vdf" — window must slide past the first 'd'
        assertEquals(3, len("dvdf"));
    }

    @Test
    void repeatAtTheEnd() {
        // "abcd" is unique, then 'a' repeats — answer is 4
        assertEquals(4, len("abcda"));
    }

    @Test
    void repeatAtTheStart() {
        // after sliding past the first 'a', window becomes "abcde" = 5
        assertEquals(5, len("aabcde"));
    }

    @Test
    void longestIsFullString() {
        // no repeats at all
        assertEquals(6, len("abcdef"));
    }

    @Test
    void longestInTheMiddle() {
        // window slides to "abcde" (len 5) before the trailing 'a' causes a shrink
        assertEquals(5, len("aabcdea"));
    }

    @Test
    void windowShrinksThenGrows() {
        // "wke" after the first repeat; then "kew"
        assertEquals(3, len("pwwkew"));
    }

    // ── non-alpha characters ─────────────────────────────────────────────────

    @Test
    void withSpaces() {
        // "a b c" has a space — each char is distinct including the space
        assertEquals(3, len("a b"));
    }

    @Test
    void withDigitsAndSymbols() {
        assertEquals(5, len("a1!b2"));
    }

    @Test
    void repeatedSpace() {
        // two spaces — window can only hold one
        assertEquals(1, len("  "));
    }

    // ── longer inputs ────────────────────────────────────────────────────────

    @Test
    void longStringNoDuplicates() {
        String s = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(26, len(s));
    }

    @Test
    void longStringAllSame() {
        assertEquals(1, len("z".repeat(1000)));
    }

    @Test
    void longStringWithPattern() {
        // repeating "ab" — window is always 2
        assertEquals(2, len("ab".repeat(500)));
    }
}
