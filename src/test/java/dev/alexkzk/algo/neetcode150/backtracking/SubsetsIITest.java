package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SubsetsIITest {
    private final SubsetsII sol = new SubsetsII();

    @Test
    void example1() {
        List<List<Integer>> result = sol.subsetsWithDup(new int[]{1, 2, 2});
        assertEquals(6, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.subsetsWithDup(new int[]{0});
        assertEquals(2, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.subsetsWithDup(new int[]{1, 1, 1});
        assertEquals(4, result.size());
    }
}
