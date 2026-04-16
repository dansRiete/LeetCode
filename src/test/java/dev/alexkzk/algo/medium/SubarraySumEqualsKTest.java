package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubarraySumEqualsKTest {

    private static int solve(int k, int... nums) {
        return SubarraySumEqualsK.subarraySum(nums, k);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        // [1,1], [1,1] — two subarrays sum to 2
        assertEquals(2, solve(2, 1, 1, 1));
    }

    @Test
    void leetcodeExample2() {
        // [3] and [1,2] both sum to 3
        assertEquals(2, solve(3, 1, 2, 3));
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void singleElementEqualsK() {
        assertEquals(1, solve(5, 5));
    }

    @Test
    void singleElementNotK() {
        assertEquals(0, solve(5, 3));
    }

    @Test
    void noMatchingSubarray() {
        assertEquals(0, solve(10, 1, 2, 3));
    }

    @Test
    void wholeArraySumsToK() {
        assertEquals(1, solve(6, 1, 2, 3));
    }

    // ── negatives ────────────────────────────────────────────────────────────

    @Test
    void negativeNumbers() {
        // [1,-1,1,-1] — subarrays summing to 0: [-1,1], [1,-1], [1,-1,1,-1]... careful
        assertEquals(4, solve(0, 1, -1, 1, -1));
    }

    @Test
    void negativeK() {
        // subarray [-3] sums to -3
        assertEquals(1, solve(-3, 1, 2, -3));
    }

    @Test
    void mixedSignsMultipleMatches() {
        // [3,-3,3]: subarrays summing to 3 → [3], [3,-3,3], [3]
        assertEquals(3, solve(3, 3, -3, 3));
    }

    // ── zeros ────────────────────────────────────────────────────────────────

    @Test
    void kIsZero() {
        // [0], [0,0], [0], [0,0,0] — all sum to 0: careful counting
        assertEquals(6, solve(0, 0, 0, 0));
    }

    @Test
    void arrayContainsZeros() {
        // [1,0,1]: subarrays summing to 1 → [1], [1,0], [0,1], [1]
        assertEquals(4, solve(1, 1, 0, 1));
    }

    // ── duplicates and repeats ────────────────────────────────────────────────

    @Test
    void allSameElements() {
        // [2,2,2], k=2 → [2],[2],[2] = 3
        assertEquals(3, solve(2, 2, 2, 2));
    }

    @Test
    void allSameElementsSumToK() {
        // [2,2,2], k=4 → [2,2],[2,2] = 2
        assertEquals(2, solve(4, 2, 2, 2));
    }

    // ── larger inputs ────────────────────────────────────────────────────────

    @Test
    void longerArray() {
        // [1,2,1,2,1], k=3 → [1,2],[2,1],[1,2],[2,1] = 4
        assertEquals(4, solve(3, 1, 2, 1, 2, 1));
    }
}
