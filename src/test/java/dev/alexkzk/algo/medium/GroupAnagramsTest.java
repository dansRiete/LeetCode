package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GroupAnagramsTest {

    private static List<List<String>> solve(String... strs) {
        return GroupAnagrams.groupAnagrams(strs);
    }

    /** Normalises a group to a sorted set so order within a group doesn't matter. */
    private static Set<String> toSet(List<String> group) {
        return Set.copyOf(group);
    }

    /** Normalises all groups so order of groups doesn't matter either. */
    private static Set<Set<String>> toGroupSet(List<List<String>> result) {
        return result.stream().map(GroupAnagramsTest::toSet).collect(Collectors.toSet());
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
        var result = solve("");
        assertEquals(1, result.size());
        assertEquals(List.of(""), result.get(0));
    }

    @Test
    void leetcodeExample3_singleChar() {
        var result = solve("a");
        assertEquals(1, result.size());
        assertEquals(List.of("a"), result.get(0));
    }

    // ── grouping correctness ─────────────────────────────────────────────────

    @Test
    void allSameAnagram() {
        // "abc", "bca", "cab" all belong to one group
        var result = toGroupSet(solve("abc", "bca", "cab"));
        assertEquals(1, result.size());
        assertTrue(result.contains(Set.of("abc", "bca", "cab")));
    }

    @Test
    void noAnagramsAtAll() {
        // each word is its own group
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
        var result = solve("hello");
        assertEquals(1, result.size());
        assertEquals(List.of("hello"), result.get(0));
    }

    @Test
    void multipleEmptyStrings() {
        // all empty strings are anagrams of each other
        var result = toGroupSet(solve("", "", ""));
        assertEquals(1, result.size());
        assertEquals(3, result.iterator().next().size() == 1
                ? solve("", "", "").get(0).size()  // all grouped together
                : -1);
        // simpler check: only one group and it has 3 elements
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
    void duplicateWords() {
        // identical words are trivially anagrams of each other
        var result = solve("cat", "cat");
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
    }

    // ── size differences ─────────────────────────────────────────────────────

    @Test
    void differentLengthsNeverGroup() {
        // "ab" and "abc" cannot be anagrams — different lengths
        var result = toGroupSet(solve("ab", "abc", "ba"));
        assertEquals(2, result.size());
        assertTrue(result.contains(Set.of("ab", "ba")));
        assertTrue(result.contains(Set.of("abc")));
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
