package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class RotateImageTest {
    private final RotateImage sol = new RotateImage();

    @Test
    void example1() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        sol.rotate(matrix);
        assertArrayEquals(new int[][]{{7,4,1},{8,5,2},{9,6,3}}, matrix);
    }

    @Test
    void example2() {
        int[][] matrix = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};
        sol.rotate(matrix);
        assertArrayEquals(new int[][]{{15,13,2,5},{14,3,4,1},{12,6,8,9},{16,7,10,11}}, matrix);
    }

    @Test
    void edgeCase() {
        int[][] matrix = {{1}};
        sol.rotate(matrix);
        assertArrayEquals(new int[][]{{1}}, matrix);
    }
}
