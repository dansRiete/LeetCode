package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class HappyNumberTest {
    private final HappyNumber sol = new HappyNumber();

    @Test
    void example1() {
        assertTrue(sol.isHappy(19));
    }

    @Test
    void example2() {
        assertFalse(sol.isHappy(2));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isHappy(1));
    }
}
