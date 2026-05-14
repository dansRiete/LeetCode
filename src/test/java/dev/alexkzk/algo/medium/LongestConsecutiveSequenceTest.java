package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestConsecutiveSequenceTest {

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertEquals(4, LongestConsecutiveSequence.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }

    @Test
    void leetcodeExample2() {
        assertEquals(9, LongestConsecutiveSequence.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void emptyArray() {
        assertEquals(0, LongestConsecutiveSequence.longestConsecutive(new int[]{}));
    }

    @Test
    void singleElement() {
        assertEquals(1, LongestConsecutiveSequence.longestConsecutive(new int[]{42}));
    }

    @Test
    void allSameElement() {
        assertEquals(1, LongestConsecutiveSequence.longestConsecutive(new int[]{5, 5, 5, 5}));
    }

    @Test
    void noConsecutivePairs() {
        // every neighbour differs by 2+
        assertEquals(1, LongestConsecutiveSequence.longestConsecutive(new int[]{10, 30, 50, 70}));
    }

    // ── sequence properties ──────────────────────────────────────────────────

    @Test
    void entireArrayIsOneSequence() {
        assertEquals(6, LongestConsecutiveSequence.longestConsecutive(new int[]{3, 4, 5, 6, 7, 8}));
    }

    @Test
    void entireArrayReverseOrder() {
        assertEquals(5, LongestConsecutiveSequence.longestConsecutive(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void twoEqualLengthSequences_returnsEitherLength() {
        // [1,2,3] and [10,11,12] — both length 3
        assertEquals(3, LongestConsecutiveSequence.longestConsecutive(new int[]{1, 2, 3, 10, 11, 12}));
    }

    @Test
    void longerSequenceWins() {
        // [1,2,3,4] length 4 vs [10,11,12] length 3
        assertEquals(4, LongestConsecutiveSequence.longestConsecutive(new int[]{10, 11, 12, 1, 2, 3, 4}));
    }

    @Test
    void duplicatesWithinSequence() {
        // duplicates must not inflate the count
        assertEquals(4, LongestConsecutiveSequence.longestConsecutive(new int[]{1, 2, 2, 3, 4}));
    }

    @Test
    void duplicatesAtSequenceBoundary() {
        assertEquals(3, LongestConsecutiveSequence.longestConsecutive(new int[]{1, 1, 2, 3, 3}));
    }

    // ── negative numbers ─────────────────────────────────────────────────────

    @Test
    void allNegative() {
        assertEquals(4, LongestConsecutiveSequence.longestConsecutive(new int[]{-4, -3, -2, -1}));
    }

    @Test
    void sequenceSpanningNegativeToPositive() {
        assertEquals(5, LongestConsecutiveSequence.longestConsecutive(new int[]{-2, -1, 0, 1, 2}));
    }

    @Test
    void sequenceIncludingZero() {
        assertEquals(3, LongestConsecutiveSequence.longestConsecutive(new int[]{-1, 0, 1, 100}));
    }

    // ── large gaps / overflow-safe ────────────────────────────────────────────

    @Test
    void extremeValuesNoSequence() {
        assertEquals(1, LongestConsecutiveSequence.longestConsecutive(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
    }

    @Test
    void extremeValuesWithSequence() {
        assertEquals(2, LongestConsecutiveSequence.longestConsecutive(
                new int[]{Integer.MAX_VALUE - 1, Integer.MAX_VALUE, 0}));
    }
}
