package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SearchA2DMatrixTest {
    private final SearchA2DMatrix sol = new SearchA2DMatrix();

    @Test
    void example1() {
        assertTrue(sol.searchMatrix(new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 3));
    }

    @Test
    void example2() {
        assertFalse(sol.searchMatrix(new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 13));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.searchMatrix(new int[][]{{1}}, 2));
    }
}
