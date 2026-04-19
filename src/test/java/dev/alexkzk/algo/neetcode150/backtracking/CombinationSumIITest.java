package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CombinationSumIITest {
    private final CombinationSumII sol = new CombinationSumII();

    @Test
    void example1() {
        List<List<Integer>> result = sol.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
        assertEquals(4, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.combinationSum2(new int[]{2, 5, 2, 1, 2}, 5);
        assertEquals(2, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.combinationSum2(new int[]{1}, 2);
        assertEquals(0, result.size());
    }
}
