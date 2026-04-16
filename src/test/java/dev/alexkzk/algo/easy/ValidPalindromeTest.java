package dev.alexkzk.algo.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidPalindromeTest {

    private static boolean check(String s) {
        return ValidPalindrome.isPalindrome(s);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertTrue(check("A man, a plan, a canal: Panama"));
    }

    @Test
    void leetcodeExample2() {
        assertFalse(check("race a car"));
    }

    @Test
    void onlySpaces() {
        // after filtering: empty string → palindrome
        assertTrue(check(" "));
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void singleLetter() {
        assertTrue(check("a"));
    }

    @Test
    void singleDigit() {
        assertTrue(check("1"));
    }

    @Test
    void twoSameLetters() {
        assertTrue(check("aa"));
    }

    @Test
    void twoDistinctLetters() {
        assertFalse(check("ab"));
    }

    @Test
    void digitMixedWithLetter() {
        // "0P" → "0p" → not a palindrome
        assertFalse(check("0P"));
    }

    // ── case insensitivity ────────────────────────────────────────────────────

    @Test
    void mixedCase() {
        assertTrue(check("RaceCar"));
    }

    @Test
    void allUpperCase() {
        assertTrue(check("ABBA"));
    }

    // ── punctuation and special chars ─────────────────────────────────────────

    @Test
    void punctuationOnly() {
        // after filtering: empty → palindrome
        assertTrue(check(",.!?"));
    }

    @Test
    void lettersWithPunctuation() {
        assertTrue(check("Was it a car or a cat I saw?"));
    }

    @Test
    void numbersWithSpaces() {
        assertTrue(check("1 2 2 1"));
    }

    @Test
    void numbersNotPalindrome() {
        assertFalse(check("1 2 3"));
    }

    // ── digits only ───────────────────────────────────────────────────────────

    @Test
    void palindromicNumber() {
        assertTrue(check("12321"));
    }

    @Test
    void nonPalindromicNumber() {
        assertFalse(check("12345"));
    }

    // ── longer inputs ────────────────────────────────────────────────────────

    @Test
    void longPalindrome() {
        assertTrue(check("abcdefedcba"));
    }

    @Test
    void longNonPalindrome() {
        assertFalse(check("abcdefghij"));
    }
}
