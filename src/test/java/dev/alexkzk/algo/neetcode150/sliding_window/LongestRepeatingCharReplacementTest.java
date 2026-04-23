package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LongestRepeatingCharReplacementTest {
    private final LongestRepeatingCharReplacement sol = new LongestRepeatingCharReplacement();

    @Test
    void example1() {
        assertEquals(4, sol.characterReplacement("ABAB", 2));
    }

    @Test
    void example2() {
        assertEquals(4, sol.characterReplacement("AABABBA", 1));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.characterReplacement("A", 0));
    }

    // ── k=0: no replacements allowed ────────────────────────────────────────

    @Test
    void kZeroAllSame() {
        // already uniform, whole string counts
        assertEquals(4, sol.characterReplacement("AAAA", 0));
    }

    @Test
    void kZeroAlternating() {
        // longest run of same char is 1
        assertEquals(1, sol.characterReplacement("ABABAB", 0));
    }

    @Test
    void kZeroLongestRunInMiddle() {
        // "BBB" is the longest uniform run
        assertEquals(3, sol.characterReplacement("ABBBAC", 0));
    }

    // ── k >= length: whole string is always valid ────────────────────────────

    @Test
    void kCoversWholeString() {
        assertEquals(5, sol.characterReplacement("ABCDE", 5));
    }

    @Test
    void kLargerThanLength() {
        assertEquals(3, sol.characterReplacement("ABC", 100));
    }

    // ── already uniform ──────────────────────────────────────────────────────

    @Test
    void allSameChars() {
        assertEquals(5, sol.characterReplacement("BBBBB", 2));
    }

    // ── single character ─────────────────────────────────────────────────────

    @Test
    void singleCharKZero() {
        assertEquals(1, sol.characterReplacement("Z", 0));
    }

    @Test
    void singleCharKPositive() {
        assertEquals(1, sol.characterReplacement("Z", 3));
    }

    // ── two character alphabet ───────────────────────────────────────────────

    @Test
    void twoCharsKOne() {
        // "AAAA" after one replacement of B
        assertEquals(5, sol.characterReplacement("AAAAB", 1));
    }

    @Test
    void longestWindowInMiddle() {
        // "BAA"→"AAA", "ABB"→"BBB", "BBC"→"BBB" — all length 3
        assertEquals(3, sol.characterReplacement("CBAABBC", 1));
    }

    // ── answer at the end of the string ─────────────────────────────────────

    @Test
    void bestWindowAtEnd() {
        // "BBB" at the end, replace one A → "BBBB"
        assertEquals(4, sol.characterReplacement("ABBB", 1));
    }

    // ── answer at the start of the string ───────────────────────────────────

    @Test
    void bestWindowAtStart() {
        // "AAA" at start, replace one B → "AAAA"
        assertEquals(4, sol.characterReplacement("AAAB", 1));
    }
}
