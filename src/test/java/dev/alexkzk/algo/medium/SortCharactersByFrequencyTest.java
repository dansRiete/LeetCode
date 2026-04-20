package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortCharactersByFrequencyTest {

    private static Map<Character, Integer> freq(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) map.merge(c, 1, Integer::sum);
        return map;
    }

    private static void assertValidResult(String input, String result) {
        assertEquals(input.length(), result.length(), "length must be preserved");
        assertEquals(freq(input), freq(result), "character counts must be preserved");
        // verify descending frequency order
        for (int i = 1; i < result.length(); i++) {
            int prevCount = freq(result).get(result.charAt(i - 1));
            int currCount = freq(result).get(result.charAt(i));
            org.junit.jupiter.api.Assertions.assertTrue(prevCount >= currCount,
                    "frequency must be non-increasing at index " + i);
        }
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertValidResult("tree", SortCharactersByFrequency.frequencySort("tree"));
    }

    @Test
    void leetcodeExample2() {
        assertValidResult("cccaaa", SortCharactersByFrequency.frequencySort("cccaaa"));
    }

    @Test
    void leetcodeExample3() {
        assertValidResult("Aabb", SortCharactersByFrequency.frequencySort("Aabb"));
    }

    // ── the motivating example ───────────────────────────────────────────────

    @Test
    void mississippi() {
        String result = SortCharactersByFrequency.frequencySort("Mississippi");
        assertValidResult("Mississippi", result);
        // i and s both appear 4 times and must come before p(2) and M(1)
        assertEquals(4, freq(result).get('i'));
        assertEquals(4, freq(result).get('s'));
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void singleCharacter() {
        assertEquals("a", SortCharactersByFrequency.frequencySort("a"));
    }

    @Test
    void allSameCharacter() {
        assertEquals("aaaa", SortCharactersByFrequency.frequencySort("aaaa"));
    }

    @Test
    void allUniqueCharacters() {
        assertValidResult("abcd", SortCharactersByFrequency.frequencySort("abcd"));
    }

    @Test
    void mixedCaseDistinct() {
        // 'A' and 'a' are different characters
        assertValidResult("AaAa", SortCharactersByFrequency.frequencySort("AaAa"));
    }

    @Test
    void digitsAndLetters() {
        assertValidResult("abc1112", SortCharactersByFrequency.frequencySort("abc1112"));
    }
}
