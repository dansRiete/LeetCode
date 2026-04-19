package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PlusOneTest {
    private final PlusOne sol = new PlusOne();

    @Test
    void example1() {
        assertArrayEquals(new int[]{1,2,4}, sol.plusOne(new int[]{1,2,3}));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{4,3,2,2}, sol.plusOne(new int[]{4,3,2,1}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{1,0,0}, sol.plusOne(new int[]{9,9}));
    }
}
