package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SetMatrixZeroesTest {
    private final SetMatrixZeroes sol = new SetMatrixZeroes();

    @Test
    void example1() {
        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{1,0,1},{0,0,0},{1,0,1}}, matrix);
    }

    @Test
    void example2() {
        int[][] matrix = {{0,1,2,0},{3,4,5,2},{1,3,1,5}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{0,0,0,0},{0,4,5,0},{0,3,1,0}}, matrix);
    }

    @Test
    void edgeCase() {
        int[][] matrix = {{1}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{1}}, matrix);
    }
}
