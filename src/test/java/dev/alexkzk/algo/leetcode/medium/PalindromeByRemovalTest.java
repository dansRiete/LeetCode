package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PalindromeByRemovalTest {

    /** Brute-force O(n²) — the reference implementation. */
    private static Set<Integer> solve(String s) {
        return Set.copyOf(PalindromeByRemoval.findRemovalIndices2(s));
    }

    /** O(n) optimised — must return the same set of indices. */
    private static Set<Integer> solveOptimised(String s) {
        return Set.copyOf(PalindromeByRemoval.findRemovalIndices3(s));
    }

    private static void assertBothAgree(String s, Set<Integer> expected) {
        assertEquals(expected, solve(s),        "brute-force disagrees for \"" + s + "\"");
        assertEquals(expected, solveOptimised(s), "optimised disagrees for \"" + s + "\"");
    }

    // ── docstring examples ────────────────────────────────────────────────────

    @Test
    void palindromeCenter_abcba() {
        // removing 'c' at index 2 → "abba"
        assertBothAgree("abcba", Set.of(2));
    }

    @Test
    void palindromeCenterDuplicate_abba() {
        // removing either 'b' at index 1 or 2 → "aba"
        assertBothAgree("abba", Set.of(1, 2));
    }

    @Test
    void allSameChars_aaaa() {
        // any removal gives "aaa"
        assertBothAgree("aaaa", Set.of(0, 1, 2, 3));
    }

    @Test
    void noRemovalWorks_abcd() {
        assertBothAgree("abcd", Set.of());
    }

    @Test
    void singleChar() {
        // removing the only char gives "" — empty string is a palindrome
        assertBothAgree("a", Set.of(0));
    }

    @Test
    void removeAtEnd_aab() {
        // removing 'b' at index 2 → "aa"
        assertBothAgree("aab", Set.of(2));
    }

    @Test
    void removeAtStart_baa() {
        // removing 'b' at index 0 → "aa"
        assertBothAgree("baa", Set.of(0));
    }

    @Test
    void palindromeCenterSingle_racecar() {
        // removing 'e' at index 3 → "raccar" which is not — wait, let's verify
        // "racecar" → already palindrome → center char 'e' at index 3 → [3]
        assertBothAgree("racecar", Set.of(3));
    }

    @Test
    void palindromeWithCenterRun_aabaa() {
        // removing 'b' at index 2 → "aaaa"
        assertBothAgree("aabaa", Set.of(2));
    }

    // ── edge cases ────────────────────────────────────────────────────────────

    @Test
    void twoSameChars() {
        // "aa" is already a palindrome; center chars are both at index 0 and 1
        assertBothAgree("aa", Set.of(0, 1));
    }

    @Test
    void twoDistinctChars() {
        // removing either char gives a 1-char string (palindrome)
        assertBothAgree("ab", Set.of(0, 1));
    }

    @Test
    void threeCharsMiddleMismatch() {
        // "abc" — removing 'a' → "bc", removing 'b' → "ac", removing 'c' → "ab" — none palindrome
        assertBothAgree("abc", Set.of());
    }

    @Test
    void threeCharsPalindrome_aba() {
        // already palindrome; center is 'b' at index 1
        assertBothAgree("aba", Set.of(1));
    }

    @Test
    void longerNoPalindrome() {
        assertBothAgree("abcdef", Set.of());
    }

    @Test
    void longPalindromeOdd_abcba_extended() {
        // "xabcbax" is already a palindrome; center 'c' at index 3
        assertBothAgree("xabcbax", Set.of(3));
    }

    @Test
    void runOfDuplicatesInCenter_aaabbaaa() {
        // "aaabbaaa" is a palindrome; center run is 'b','b' at indices 3,4
        assertBothAgree("aaabbaaa", Set.of(3, 4));
    }

    // ── brute-force consistency for longer inputs ─────────────────────────────

    @Test
    void optimisedMatchesBruteForce_mixed() {
        for (String s : List.of("aabba", "xyzyx", "aabbcc", "aaabb", "bbaaa", "abacaba")) {
            assertEquals(solve(s), solveOptimised(s),
                    "implementations disagree for \"" + s + "\"");
        }
    }
}
