package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestPalindromicSubstringTest {

    private static String solve(String s) {
        return LongestPalindromicSubstring.longestPalindrome(s);
    }

    /** Verifies the result is a palindrome of the expected length (multiple valid answers allowed). */
    private static void assertPalindrome(String input, int expectedLength) {
        String result = solve(input);
        assertNotNull(result, "result must not be null");
        assertEquals(expectedLength, result.length(),
                "expected length " + expectedLength + " but got \"" + result + "\"");
        assertEquals(new StringBuilder(result).reverse().toString(), result,
                "\"" + result + "\" is not a palindrome");
        assertTrue(input.contains(result),
                "\"" + result + "\" is not a substring of \"" + input + "\"");
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void singleChar() {
        assertEquals("a", solve("a"));
    }

    @Test
    void twoSameChars() {
        assertEquals("aa", solve("aa"));
    }

    @Test
    void twoDistinctChars() {
        // both chars are palindromes of length 1 — either is valid
        assertPalindrome("ab", 1);
    }

    // ── odd-length palindromes ────────────────────────────────────────────────

    @Test
    void leetcodeExample1_oddPalindrome() {
        // "bab" and "aba" are both valid answers of length 3
        assertPalindrome("babad", 3);
    }

    @Test
    void centeredPalindrome() {
        assertEquals("racecar", solve("racecar"));
    }

    @Test
    void wholeStringOdd() {
        assertEquals("abcba", solve("abcba"));
    }

    @Test
    void palindromeInMiddle() {
        // "aba" is the longest palindrome inside "xabay"
        assertPalindrome("xabay", 3);
    }

    // ── even-length palindromes ───────────────────────────────────────────────

    @Test
    void leetcodeExample2_evenPalindrome() {
        assertEquals("bb", solve("cbbd"));
    }

    @Test
    void evenLengthWhole() {
        assertEquals("abba", solve("abba"));
    }

    @Test
    void evenPalindromeEmbedded() {
        // "aabbaa" is the longest palindrome in "xaabbaa"
        assertPalindrome("xaabbaa", 6);
    }

    // ── all same characters ───────────────────────────────────────────────────

    @Test
    void allSameChars() {
        // entire string is a palindrome
        assertPalindrome("aaaa", 4);
    }

    @Test
    void allSameCharsLong() {
        assertPalindrome("a".repeat(10), 10);
    }

    // ── palindrome at boundaries ──────────────────────────────────────────────

    @Test
    void palindromeAtStart() {
        // "aba" at the start of "abaxyz"
        assertPalindrome("abaxyz", 3);
    }

    @Test
    void palindromeAtEnd() {
        // "aba" at the end of "xyzaba"
        assertPalindrome("xyzaba", 3);
    }

    // ── no palindrome longer than 1 ───────────────────────────────────────────

    @Test
    void allDistinctChars() {
        // no two adjacent chars are equal — longest palindrome is length 1
        assertPalindrome("abcdef", 1);
    }

    // ── longer inputs ────────────────────────────────────────────────────────

    @Test
    void longerStringWithClearWinner() {
        // "racecar" is embedded and clearly the longest
        assertPalindrome("abcracecarxyz", 7);
    }

    @Test
    void longerStringEvenPalindrome() {
        // "zzzz" is the longest palindrome
        assertPalindrome("abzzzzcde", 4);
    }
}
