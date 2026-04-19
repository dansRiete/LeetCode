package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SwimInRisingWaterTest {
    private final SwimInRisingWater sol = new SwimInRisingWater();

    @Test
    void example1() {
        assertEquals(3, sol.swimInWater(new int[][]{{0,2},{1,3}}));
    }

    @Test
    void example2() {
        assertEquals(16, sol.swimInWater(new int[][]{{0,1,2,3,4},{24,23,22,21,5},{12,13,14,15,16},{11,17,18,19,20},{10,9,8,7,6}}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.swimInWater(new int[][]{{0}}));
    }
}
