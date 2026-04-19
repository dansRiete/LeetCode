package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PowXNTest {
    private final PowXN sol = new PowXN();

    @Test
    void example1() {
        assertEquals(1024.0, sol.myPow(2.0, 10), 1e-5);
    }

    @Test
    void example2() {
        assertEquals(9.261, sol.myPow(2.1, 3), 1e-3);
    }

    @Test
    void edgeCase() {
        assertEquals(0.25, sol.myPow(2.0, -2), 1e-5);
    }
}
