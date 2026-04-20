package dev.alexkzk.algo.neetcode150.two_pointers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TwoSumIITest {
    private final TwoSumII sol = new TwoSumII();

    @Test
    void example1() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{1, 3}, sol.twoSum(new int[]{2, 3, 4}, 6));
    }

    @Test
    void negativeNumbers() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-1, 0}, -1));
    }

    @Test
    void twoElementArray() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{1, 2}, 3));
    }

    @Test
    void targetIsZero() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-3, 3}, 0));
    }

    @Test
    void bothNegative() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-5, -3, -1}, -8));
    }

    @Test
    void answerAtEnd() {
        assertArrayEquals(new int[]{3, 4}, sol.twoSum(new int[]{1, 2, 3, 4}, 7));
    }

    @Test
    void answerAtStart() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{1, 2, 5, 10}, 3));
    }

    @Test
    void largeTarget() {
        assertArrayEquals(new int[]{3, 4}, sol.twoSum(new int[]{1, 2, 1000, 1001}, 2001));
    }

    @Test
    void duplicateValues() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{3, 3}, 6));
    }

    @Test
    void duplicatesNotAnswer() {
        assertArrayEquals(new int[]{2, 3}, sol.twoSum(new int[]{1, 3, 3, 5}, 6));
    }

    @Test
    void negativeTarget() {
        assertArrayEquals(new int[]{1, 3}, sol.twoSum(new int[]{-5, -3, -1, 0}, -6));
    }

    @Test
    void mixedNegativePositive() {
        assertArrayEquals(new int[]{1, 4}, sol.twoSum(new int[]{-3, 1, 2, 4}, 1));
    }

    @Test
    void longerArray() {
        // only 7+8=15
        assertArrayEquals(new int[]{7, 8}, sol.twoSum(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 15));
    }

    @Test
    void firstAndLast() {
        // only 1+13=14
        assertArrayEquals(new int[]{1, 5}, sol.twoSum(new int[]{1, 5, 8, 11, 13}, 14));
    }

    @Test
    void middleElements() {
        // only 3+5=8
        assertArrayEquals(new int[]{2, 3}, sol.twoSum(new int[]{1, 3, 5, 8, 10}, 8));
    }

    @Test
    void zeros() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{0, 0}, 0));
    }

    @Test
    void singleNegativeAndPositive() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-100, 100}, 0));
    }

    @Test
    void largeNegatives() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-1000, -500, 0, 500}, -1500));
    }

    @Test
    void maxValueTarget() {
        assertArrayEquals(new int[]{3, 4}, sol.twoSum(new int[]{1, 2, 999, 1000}, 1999));
    }

    @Test
    void threeElementsFirstTwo() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{1, 4, 10}, 5));
    }

    @Test
    void threeElementsLastTwo() {
        assertArrayEquals(new int[]{2, 3}, sol.twoSum(new int[]{1, 4, 10}, 14));
    }

    @Test
    void allSameExceptAnswer() {
        assertArrayEquals(new int[]{1, 5}, sol.twoSum(new int[]{1, 2, 2, 2, 5}, 6));
    }
}
