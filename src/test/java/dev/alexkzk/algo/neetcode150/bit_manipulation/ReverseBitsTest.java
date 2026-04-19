package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ReverseBitsTest {
    private final ReverseBits sol = new ReverseBits();

    @Test
    void example1() {
        assertEquals(964176192, sol.reverseBits(43261596)); // 00000010100101000001111010011100 -> 00111001011110000010100101000000
    }

    @Test
    void example2() {
        assertEquals(-1073741825, sol.reverseBits(-3)); // 11111111111111111111111111111101 -> 10111111111111111111111111111111
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.reverseBits(0));
    }
}
