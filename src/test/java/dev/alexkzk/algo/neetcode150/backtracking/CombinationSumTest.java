package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CombinationSumTest {
    private final CombinationSum sol = new CombinationSum();

    @Test
    void example1() {
        List<List<Integer>> result = sol.combinationSum(new int[]{2, 3, 6, 7}, 7);
        assertEquals(2, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.combinationSum(new int[]{2, 3, 5}, 8);
        assertEquals(3, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.combinationSum(new int[]{2}, 1);
        assertEquals(0, result.size());
    }
}
