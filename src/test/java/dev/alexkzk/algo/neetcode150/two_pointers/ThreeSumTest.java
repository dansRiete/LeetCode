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
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 1, 1});
        assertEquals(0, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 0, 0});
        assertEquals(1, result.size());
    }
}
