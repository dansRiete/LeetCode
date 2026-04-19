package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SingleNumberTest {
    private final SingleNumber sol = new SingleNumber();

    @Test
    void example1() {
        assertEquals(1, sol.singleNumber(new int[]{2, 2, 1}));
    }

    @Test
    void example2() {
        assertEquals(4, sol.singleNumber(new int[]{4, 1, 2, 1, 2}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.singleNumber(new int[]{1}));
    }
}
