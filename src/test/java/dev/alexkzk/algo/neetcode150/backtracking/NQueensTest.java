package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NQueensTest {
    private final NQueens sol = new NQueens();

    @Test
    void example1() {
        List<List<String>> result = sol.solveNQueens(4);
        assertEquals(2, result.size());
    }

    @Test
    void example2() {
        List<List<String>> result = sol.solveNQueens(1);
        assertEquals(1, result.size());
    }

    @Test
    void edgeCase() {
        List<List<String>> result = sol.solveNQueens(5);
        assertEquals(10, result.size());
    }
}
