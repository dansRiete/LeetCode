package dev.alexkzk.algo.neetcode150.two_pointers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ThreeSumTest {
    private final ThreeSum sol = new ThreeSum();

    @Test
    void example1() {
        List<List<Integer>> result = sol.threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 1, 1});
        assertEquals(0, result.size());
    }

    @Test
    void allZeros() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 0, 0});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(0, 0, 0)));
    }

    @Test
    void allPositive() {
        assertEquals(0, sol.threeSum(new int[]{1, 2, 3, 4}).size());
    }

    @Test
    void allNegative() {
        assertEquals(0, sol.threeSum(new int[]{-4, -3, -2, -1}).size());
    }

    @Test
    void singleTriplet() {
        List<List<Integer>> result = sol.threeSum(new int[]{-1, 0, 1});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void singleTripletNegatives() {
        List<List<Integer>> result = sol.threeSum(new int[]{-3, 1, 2});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-3, 1, 2)));
    }

    @Test
    void duplicateInputElements() {
        List<List<Integer>> result = sol.threeSum(new int[]{-2, -2, 0, 0, 2, 2});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-2, 0, 2)));
    }

    @Test
    void fourZeros() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 0, 0, 0});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(0, 0, 0)));
    }

    @Test
    void multipleZerosWithPair() {
        List<List<Integer>> result = sol.threeSum(new int[]{-2, 0, 0, 0, 2});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-2, 0, 2)));
        assertTrue(result.contains(List.of(0, 0, 0)));
    }

    @Test
    void twoSameNegatives() {
        List<List<Integer>> result = sol.threeSum(new int[]{-1, -1, 2});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
    }

    @Test
    void noTripletWithDuplicates() {
        assertEquals(0, sol.threeSum(new int[]{1, 1, 1}).size());
    }

    @Test
    void noDuplicateTriplets() {
        List<List<Integer>> result = sol.threeSum(new int[]{-4, -1, -1, 0, 1, 2});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void largeMixedArray() {
        List<List<Integer>> result = sol.threeSum(new int[]{-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5});
        assertTrue(result.contains(List.of(-5, 1, 4)));
        assertTrue(result.contains(List.of(-4, 0, 4)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
        assertFalse(result.isEmpty());
    }

    @Test
    void twoPositiveOneNegative() {
        List<List<Integer>> result = sol.threeSum(new int[]{-4, 2, 2});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-4, 2, 2)));
    }

    @Test
    void sortedInput() {
        List<List<Integer>> result = sol.threeSum(new int[]{-3, -2, -1, 0, 1, 2, 3});
        assertTrue(result.contains(List.of(-3, 0, 3)));
        assertTrue(result.contains(List.of(-2, -1, 3)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void noSolutionThreeElements() {
        assertEquals(0, sol.threeSum(new int[]{1, 2, 4}).size());
    }

    @Test
    void negativeAndTwoPositiveZeroSum() {
        List<List<Integer>> result = sol.threeSum(new int[]{-6, 2, 4});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-6, 2, 4)));
    }

    @Test
    void onlyTwoDistinctValues() {
        List<List<Integer>> result = sol.threeSum(new int[]{-1, -1, -1, 2, 2});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
    }
}
