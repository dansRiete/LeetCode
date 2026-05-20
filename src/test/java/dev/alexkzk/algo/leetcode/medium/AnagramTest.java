package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AnagramTest {

    private static List<List<String>> solve(String... strs) {
        return Anagram.groupAnagrams(strs);
    }

    private static Set<Set<String>> toGroupSet(List<List<String>> result) {
        return result.stream()
                .map(Set::copyOf)
                .collect(Collectors.toSet());
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        var result = toGroupSet(solve("eat", "tea", "tan", "ate", "nat", "bat"));
        assertEquals(3, result.size());
        assertTrue(result.contains(Set.of("eat", "tea", "ate")));
        assertTrue(result.contains(Set.of("tan", "nat")));
        assertTrue(result.contains(Set.of("bat")));
    }

    @Test
    void leetcodeExample2_emptyString() {
        var groups = solve("");
        assertEquals(1, groups.size());
        assertEquals(List.of(""), groups.get(0));
    }

    @Test
    void leetcodeExample3_singleChar() {
        var groups = solve("a");
        assertEquals(1, groups.size());
        assertEquals(List.of("a"), groups.get(0));
    }

    // ── grouping correctness ─────────────────────────────────────────────────

    @Test
    void allSameAnagram() {
        var result = toGroupSet(solve("abc", "bca", "cab"));
        assertEquals(1, result.size());
        assertTrue(result.contains(Set.of("abc", "bca", "cab")));
    }

    @Test
    void noAnagramsAtAll() {
        var result = toGroupSet(solve("cat", "dog", "bird"));
        assertEquals(3, result.size());
        assertTrue(result.contains(Set.of("cat")));
        assertTrue(result.contains(Set.of("dog")));
        assertTrue(result.contains(Set.of("bird")));
    }

    @Test
    void twoGroups() {
        var result = toGroupSet(solve("ab", "ba", "cd", "dc"));
        assertEquals(2, result.size());
        assertTrue(result.contains(Set.of("ab", "ba")));
        assertTrue(result.contains(Set.of("cd", "dc")));
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void singleWord() {
        var groups = solve("hello");
        assertEquals(1, groups.size());
        assertEquals(List.of("hello"), groups.get(0));
    }

    @Test
    void multipleEmptyStrings() {
        var groups = solve("", "", "");
        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size());
    }

    @Test
    void emptyStringMixedWithNonEmpty() {
        var result = toGroupSet(solve("", "a", ""));
        assertEquals(2, result.size());
        assertTrue(result.contains(Set.of("a")));
    }

    @Test
    void differentLengthsNeverGroup() {
        var result = toGroupSet(solve("ab", "abc", "ba"));
        assertEquals(2, result.size());
        assertTrue(result.contains(Set.of("ab", "ba")));
        assertTrue(result.contains(Set.of("abc")));
    }

    // ── character frequency key correctness ──────────────────────────────────

    @Test
    void sameCharsUnequalCounts() {
        // "aab" and "abb" share the same characters but different counts — different groups
        var result = toGroupSet(solve("aab", "bba", "abb", "baa"));
        assertEquals(2, result.size());
        assertTrue(result.contains(Set.of("aab", "baa")));
        assertTrue(result.contains(Set.of("abb", "bba")));
    }

    @Test
    void repeatedCharsAllSameAnagram() {
        var result = toGroupSet(solve("aaa", "aaa"));
        assertEquals(1, result.size());
        assertEquals(2, solve("aaa", "aaa").get(0).size());
    }

    // ── larger input ─────────────────────────────────────────────────────────

    @Test
    void largerInput() {
        var result = toGroupSet(solve("eat", "tea", "tan", "ate", "nat", "bat", "tab", "tna"));
        assertTrue(result.contains(Set.of("eat", "tea", "ate")));
        assertTrue(result.contains(Set.of("tan", "nat", "tna")));
        assertTrue(result.contains(Set.of("bat", "tab")));
    }
}
