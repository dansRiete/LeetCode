package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SubsetsTest {
    private final Subsets sol = new Subsets();

    @Test
    void example1() {
        List<List<Integer>> result = sol.subsets(new int[]{1, 2, 3});
        assertEquals(8, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.subsets(new int[]{0});
        assertEquals(2, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.subsets(new int[]{});
        assertEquals(1, result.size());
        assertTrue(result.get(0).isEmpty());
    }
}
