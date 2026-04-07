package dev.alexkzk.algo;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PalindromeByRemovalTest {

    private static Set<Integer> indices(String s) {
        return new HashSet<>(PalindromeByRemoval.findRemovalIndices(s));
    }

    // --- single valid removal ---

    @Test
    void removeCenter() {
        // "abcba" remove 'c' at index 2 → "abba" palindrome
        assertEquals(Set.of(2), indices("abcba"));
    }

    @Test
    void removeOnlyOddChar() {
        // "aab" → remove 'b' at index 2 → "aa"
        assertEquals(Set.of(2), indices("aab"));
    }

    @Test
    void removeOnlyOddChar2() {
        assertEquals(Set.of(7), indices("racecarw"));
    }

    @Test
    void removeOnlyOddCharAtStart() {
        // "baa" → remove 'b' at index 0 → "aa"
        assertEquals(Set.of(0), indices("babcba"));
    }

    @Test
    void removeCenterOfLongPalindrome() {
        // "racecar" remove 'e' at index 3 → "raccar"
        assertEquals(Set.of(3), indices("racecar"));
    }

    // --- multiple valid removals ---

    @Test
    void removePairInMiddle() {
        // "abba" → remove index 1 or 2 → "aba"
        assertEquals(Set.of(1, 2), indices("abba"));
    }

    @Test
    void allSameChars() {
        // "aaaa" → any removal gives "aaa"
        assertEquals(Set.of(0, 1, 2, 3), indices("aaaa"));
    }

    @Test
    void twoSameChars() {
        // "aa" → either removal gives "a"
        assertEquals(Set.of(0, 1), indices("aa"));
    }

    // --- remove middle char in non-palindrome ---

    @Test
    void removeMiddleCharProducesPalindrome() {
        // "abzcba" → remove 'z' at index 2 → "abcba", or remove 'c' at index 3 → "abzba"
        assertEquals(Set.of(2, 3), indices("abzcba"));
    }

    @Test
    void removeMiddleCharSingleOption() {
        // "aXbba" → remove 'X' at index 1 → "abba"
        assertEquals(Set.of(1), indices("aXbba"));
    }

    // --- no valid removal ---

    @Test
    void noValidRemoval() {
        // "abcd" → no removal produces a palindrome
        assertEquals(Set.of(), indices("abcd"));
    }

    @Test
    void noValidRemovalThreeChars() {
        // "abc" → no removal produces a palindrome
        assertEquals(Set.of(), indices("abc"));
    }

    // --- minimal input ---

    @Test
    void singleChar() {
        // removing the only char produces "" which is a palindrome
        assertEquals(Set.of(0), indices("a"));
    }

    // --- already a palindrome, only center removal works ---

    @Test
    void oddLengthPalindromeOnlyCenterWorks() {
        // "aabaa" → remove index 2 ('b') → "aaaa"
        assertEquals(Set.of(2), indices("aabaa"));
    }

    // --- already a palindrome ---

    @Test
    void alreadyPalindromeOnlyCenterCanBeRemoved() {
        // "abacaba" is a palindrome; only removing 'c' at index 3 keeps it a palindrome → "abaaba"? no
        // remove 3 ('c') → "abaaba" = a,b,a,a,b,a → palindrome ✓
        // remove 0 ('a') → "bacaba" → not palindrome
        assertEquals(Set.of(3), indices("abacaba"));
    }

    @Test
    void alreadyPalindromeAllRemovalsValid() {
        // "aba" is a palindrome; remove 0 → "ba" no, remove 1 → "aa" yes, remove 2 → "ab" no
        assertEquals(Set.of(1), indices("aba"));
    }

    @Test
    void alreadyPalindromeEvenLength() {
        // "abba" is a palindrome; remove 1 or 2 → "aba" yes; remove 0 or 3 → "bba"/"abb" no
        assertEquals(Set.of(1, 2), indices("abba"));
    }
}
