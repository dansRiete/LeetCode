package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PermutationsTest {
    private final Permutations sol = new Permutations();

    @Test
    void example1() {
        List<List<Integer>> result = sol.permute(new int[]{1, 2, 3});
        assertEquals(6, result.size());
    }

    @Test
    void example2() {
        List<List<Integer>> result = sol.permute(new int[]{0, 1});
        assertEquals(2, result.size());
    }

    @Test
    void edgeCase() {
        List<List<Integer>> result = sol.permute(new int[]{1});
        assertEquals(1, result.size());
    }
}
