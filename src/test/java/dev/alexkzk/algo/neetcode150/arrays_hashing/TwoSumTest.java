package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TwoSumTest {
    private final TwoSum sol = new TwoSum();

    @Test
    void example1() {
        assertArrayEquals(new int[]{0, 1}, sol.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{3, 2, 4}, 6));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{0, 1}, sol.twoSum(new int[]{3, 3}, 6));
    }
}
