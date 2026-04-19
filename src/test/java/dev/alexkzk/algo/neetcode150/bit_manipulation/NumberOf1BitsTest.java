package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NumberOf1BitsTest {
    private final NumberOf1Bits sol = new NumberOf1Bits();

    @Test
    void example1() {
        assertEquals(3, sol.hammingWeight(11)); // 00000000000000000000000000001011
    }

    @Test
    void example2() {
        assertEquals(1, sol.hammingWeight(128)); // 10000000
    }

    @Test
    void edgeCase() {
        assertEquals(32, sol.hammingWeight(-1)); // all 1s
    }
}
