package dev.alexkzk.algo;

import dev.alexkzk.algo.medium.RemoveCharString;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCharStringTest {

    private static Set<Integer> indices(String str1, String str2) {
        return new HashSet<>(RemoveCharString.findDeletionIndices(str1, str2));
    }

    // --- problem statement example ---

    @Test
    void duplicateCharsInMiddle() {
        // "aabdeef" vs "aabdef": both 'e' at indices 4 and 5 can be removed
        assertEquals(Set.of(4, 5), indices("aabdeef", "aabdef"));
    }

    // --- single valid deletion position ---

    @Test
    void removeAtStart() {
        // "xabc" → remove 'x' at index 0
        assertEquals(Set.of(0), indices("xabc", "abc"));
    }

    @Test
    void removeAtEnd() {
        // "abcx" → remove 'x' at index 3
        assertEquals(Set.of(3), indices("abcx", "abc"));
    }

    @Test
    void removeUniqueCharInMiddle() {
        // "abXcd" → remove 'X' at index 2 (unique, no other position works)
        assertEquals(Set.of(2), indices("abXcd", "abcd"));
    }

    // --- multiple valid deletion positions ---

    @Test
    void allSameChars() {
        // every position is valid when all chars are identical
        assertEquals(Set.of(0, 1, 2, 3), indices("aaaa", "aaa"));
    }

    @Test
    void duplicateAtStart() {
        // "aabbc" vs "abbc": either 'a' at index 0 or 1 can be removed
        assertEquals(Set.of(0, 1), indices("aabbc", "abbc"));
    }

    @Test
    void duplicateAtEnd() {
        // "abcc" vs "abc": either 'c' at index 2 or 3 can be removed
        assertEquals(Set.of(2, 3), indices("abcc", "abc"));
    }

    // --- minimal input ---

    @Test
    void twoCharBothSame() {
        // "aa" vs "a": both indices are valid
        assertEquals(Set.of(0, 1), indices("aa", "a"));
    }

    @Test
    void twoCharRemoveFirst() {
        // "ab" vs "b": only index 0
        assertEquals(Set.of(0), indices("ab", "b"));
    }

    @Test
    void twoCharRemoveLast() {
        // "ab" vs "a": only index 1
        assertEquals(Set.of(1), indices("ab", "a"));
    }

    // --- illegal argument cases ---

    @Test
    void equalStrings_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> RemoveCharString.findDeletionIndices("abc", "abc"));
    }

    @Test
    void lengthDiffMoreThanOne_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> RemoveCharString.findDeletionIndices("abcde", "ab"));
    }

    @Test
    void sameLengthDifferentChars_throws() {
        int size = RemoveCharString.findDeletionIndices("abcde", "abfde").size();
        assertTrue(size == 0);
    }

    @Test
    void str2LongerThanStr1_throws() {
        // str1 must be the longer string
        assertThrows(IllegalArgumentException.class,
                () -> RemoveCharString.findDeletionIndices("abc", "abcd"));
    }
}
