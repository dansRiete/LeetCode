package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SpiralMatrixTest {
    private final SpiralMatrix sol = new SpiralMatrix();

    @Test
    void example1() {
        assertEquals(List.of(1,2,3,6,9,8,7,4,5), sol.spiralOrder(new int[][]{{1,2,3},{4,5,6},{7,8,9}}));
    }

    @Test
    void example2() {
        assertEquals(List.of(1,2,3,4,8,12,11,10,9,5,6,7), sol.spiralOrder(new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}}));
    }

    @Test
    void edgeCase() {
        assertEquals(List.of(1), sol.spiralOrder(new int[][]{{1}}));
    }
}
