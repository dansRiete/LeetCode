package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCharStringTest {

    /** O(n) solution — primary implementation under test. */
    private static Set<Integer> solve(String str1, String str2) {
        return Set.copyOf(RemoveCharString.findDeletionIndices(str1, str2));
    }

    /** O(n²) brute-force — used to cross-verify the O(n) solution. */
    private static Set<Integer> solveReference(String str1, String str2) {
        return Set.copyOf(RemoveCharString.findDeletionIndices3(str1, str2));
    }

    private static void assertBothAgree(String str1, String str2, Set<Integer> expected) {
        assertEquals(expected, solve(str1, str2),
                "O(n) disagrees for (\"" + str1 + "\", \"" + str2 + "\")");
        assertEquals(expected, solveReference(str1, str2),
                "O(n²) disagrees for (\"" + str1 + "\", \"" + str2 + "\")");
    }

    // ── basic examples ────────────────────────────────────────────────────────

    @Test
    void runOfDuplicates_removeMiddleOne() {
        // "abcccdde" → "abccdde": the extra 'c' can be removed at index 2, 3, or 4
        assertBothAgree("abcccdde", "abccdde", Set.of(2, 3, 4));
    }

    @Test
    void singleDeletion_uniqueChar() {
        // "abcde" → "abce": only index 3 ('d') works
        assertBothAgree("abcde", "abce", Set.of(3));
    }

    @Test
    void removeFirstChar() {
        // "xabc" → "abc": only index 0
        assertBothAgree("xabc", "abc", Set.of(0));
    }

    @Test
    void removeLastChar() {
        // "abcx" → "abc": only index 3
        assertBothAgree("abcx", "abc", Set.of(3));
    }

    @Test
    void removeOnlyChar() {
        // "a" → "": only index 0
        assertBothAgree("a", "", Set.of(0));
    }

    // ── adjacent duplicates ───────────────────────────────────────────────────

    @Test
    void twoDuplicates_eitherRemovable() {
        // "aab" → "ab": removing either 'a' at index 0 or 1 works
        assertBothAgree("aab", "ab", Set.of(0, 1));
    }

    @Test
    void twoDuplicatesAtEnd() {
        // "abb" → "ab": removing either 'b' at index 1 or 2 works
        assertBothAgree("abb", "ab", Set.of(1, 2));
    }

    @Test
    void threeDuplicates_middleGroup() {
        // "aabbbcc" → "aabbcc": any of the three 'b's can be removed
        assertBothAgree("aabbbcc", "aabbcc", Set.of(2, 3, 4));
    }

    // ── two-char strings ──────────────────────────────────────────────────────

    @Test
    void twoChars_removeLast() {
        assertBothAgree("ab", "a", Set.of(1));
    }

    @Test
    void twoChars_removeFirst() {
        assertBothAgree("ab", "b", Set.of(0));
    }

    @Test
    void twoIdenticalChars() {
        // "aa" → "a": either index works
        assertBothAgree("aa", "a", Set.of(0, 1));
    }

    // ── no valid deletion (str2 not reachable by one deletion) ────────────────

    @Test
    void noValidDeletion_twoDifferences() {
        // "abc" → "xc" requires more than one deletion → returns empty
        assertEquals(Set.of(), solve("abc", "xc"));
    }

    // ── invalid input throws ─────────────────────────────────────────────────

    @Test
    void throwsWhenLengthDiffNotOne_tooShort() {
        assertThrows(IllegalArgumentException.class,
                () -> RemoveCharString.findDeletionIndices("ab", "ab"));
    }

    @Test
    void throwsWhenLengthDiffNotOne_tooLong() {
        assertThrows(IllegalArgumentException.class,
                () -> RemoveCharString.findDeletionIndices("ab", "abc"));
    }

    // ── cross-verify O(n) vs O(n²) ───────────────────────────────────────────

    @Test
    void bothImplementationsAgree_variousCases() {
        String[][] pairs = {
                {"abcccdde", "abccdde"},
                {"hello", "hell"},
                {"hello", "ello"},
                {"aabbcc", "abbcc"},
                {"aabbcc", "aabbc"},
                {"zzz", "zz"},
        };
        for (String[] pair : pairs) {
            assertEquals(solve(pair[0], pair[1]), solveReference(pair[0], pair[1]),
                    "implementations disagree for (\"" + pair[0] + "\", \"" + pair[1] + "\")");
        }
    }
}
