package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreeSumTest {

    private static List<List<Integer>> solve(int... nums) {
        return ThreeSum.threeSum(nums);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        var result = solve(-1, 0, 1, 2, -1, -4);
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void leetcodeExample2() {
        assertTrue(solve(0, 1, 1).isEmpty());
    }

    @Test
    void leetcodeExample3() {
        var result = solve(0, 0, 0);
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(0, 0, 0)));
    }

    // ── no valid triplet ─────────────────────────────────────────────────────

    @Test
    void allPositive() {
        assertTrue(solve(1, 2, 3, 4).isEmpty());
    }

    @Test
    void allNegative() {
        assertTrue(solve(-4, -3, -2, -1).isEmpty());
    }

    @Test
    void minimumLengthNoSolution() {
        assertTrue(solve(1, 2, 3).isEmpty());
    }

    // ── duplicates in input ──────────────────────────────────────────────────

    @Test
    void noDuplicateTriplets() {
        // multiple ways to form [-1, 0, 1] — should appear only once
        var result = solve(-1, -1, 0, 0, 1, 1);
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void multipleZeros() {
        var result = solve(0, 0, 0, 0);
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(0, 0, 0)));
    }

    // ── multiple valid triplets ───────────────────────────────────────────────

    @Test
    void multipleTriplets() {
        var result = solve(-2, 0, 1, 1, 2);
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-2, 0, 2)));
        assertTrue(result.contains(List.of(-2, 1, 1)));
    }

    // ── negatives and positives ───────────────────────────────────────────────

    @Test
    void mixedValues() {
        var result = solve(-4, -2, -2, -2, 0, 1, 2, 2, 2, 3, 3, 4, 4, 6, 6);
        assertTrue(result.contains(List.of(-4, -2, 6)));
        assertTrue(result.contains(List.of(-4, 0, 4)));
        assertTrue(result.contains(List.of(-4, 1, 3)));
        assertTrue(result.contains(List.of(-4, 2, 2)));
        assertTrue(result.contains(List.of(-2, -2, 4)));
        assertTrue(result.contains(List.of(-2, 0, 2)));
    }

    // ── boundary ──────────────────────────────────────────────────────────────

    @Test
    void largeNegativeAndPositive() {
        var result = solve(-100000, 0, 100000);
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-100000, 0, 100000)));
    }
}
