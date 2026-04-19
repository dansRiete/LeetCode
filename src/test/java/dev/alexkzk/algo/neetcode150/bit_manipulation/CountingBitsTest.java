package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CountingBitsTest {
    private final CountingBits sol = new CountingBits();

    @Test
    void example1() {
        assertArrayEquals(new int[]{0,1,1}, sol.countBits(2));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{0,1,1,2,1,2}, sol.countBits(5));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{0}, sol.countBits(0));
    }
}
