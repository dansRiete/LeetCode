package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ReverseIntegerTest {
    private final ReverseInteger sol = new ReverseInteger();

    @Test
    void example1() {
        assertEquals(321, sol.reverse(123));
    }

    @Test
    void example2() {
        assertEquals(-321, sol.reverse(-123));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.reverse(1534236469)); // overflow
    }
}
