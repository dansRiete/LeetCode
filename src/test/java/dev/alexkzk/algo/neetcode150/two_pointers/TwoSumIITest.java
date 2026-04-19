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
    void edgeCase() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-1, 0}, -1));
    }
}
