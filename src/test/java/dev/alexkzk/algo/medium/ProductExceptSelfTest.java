package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ProductExceptSelfTest {

    private static int[] solve(int... nums) {
        return ProductExceptSelf.productExceptSelf(nums);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        // each position = product of all others
        assertArrayEquals(new int[]{24, 12, 8, 6}, solve(1, 2, 3, 4));
    }

    @Test
    void leetcodeExample2() {
        // single zero collapses most products to 0
        assertArrayEquals(new int[]{0, 0, 9, 0, 0}, solve(-1, 1, 0, -3, 3));
    }

    // ── zeros ────────────────────────────────────────────────────────────────

    @Test
    void singleZero() {
        // only the position of the zero gets a non-zero result
        assertArrayEquals(new int[]{0, 0, 6, 0}, solve(1, 2, 0, 3));
    }

    @Test
    void twoZeros() {
        // two zeros → every product is 0
        assertArrayEquals(new int[]{0, 0, 0, 0}, solve(1, 0, 3, 0));
    }

    @Test
    void allZeros() {
        assertArrayEquals(new int[]{0, 0, 0}, solve(0, 0, 0));
    }

    // ── negatives ────────────────────────────────────────────────────────────

    @Test
    void allNegatives() {
        // [-1,-2,-3,-4]: products are (-2*-3*-4)=-24, (-1*-3*-4)=-12, (-1*-2*-4)=-8, (-1*-2*-3)=-6
        assertArrayEquals(new int[]{-24, -12, -8, -6}, solve(-1, -2, -3, -4));
    }

    @Test
    void mixedSign() {
        // [2,-3,4]: products are (-3*4)=-12, (2*4)=8, (2*-3)=-6
        assertArrayEquals(new int[]{-12, 8, -6}, solve(2, -3, 4));
    }

    // ── ones and identity ────────────────────────────────────────────────────

    @Test
    void allOnes() {
        assertArrayEquals(new int[]{1, 1, 1, 1}, solve(1, 1, 1, 1));
    }

    @Test
    void containsOne() {
        // [1,2,3]: [6,3,2]
        assertArrayEquals(new int[]{6, 3, 2}, solve(1, 2, 3));
    }

    // ── minimum length ────────────────────────────────────────────────────────

    @Test
    void twoElements() {
        // each element is just the other element
        assertArrayEquals(new int[]{5, 3}, solve(3, 5));
    }

    @Test
    void twoElementsWithZero() {
        assertArrayEquals(new int[]{0, 7}, solve(7, 0));
    }

    // ── larger values ────────────────────────────────────────────────────────

    @Test
    void largerValues() {
        // [2, 3, 4, 5]: [60, 40, 30, 24]
        assertArrayEquals(new int[]{60, 40, 30, 24}, solve(2, 3, 4, 5));
    }

    @Test
    void boundaryValues() {
        // [30, 30]: each side is just the other
        assertArrayEquals(new int[]{30, 30}, solve(30, 30));
    }
}
