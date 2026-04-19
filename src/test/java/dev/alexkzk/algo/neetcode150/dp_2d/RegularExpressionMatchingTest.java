package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class RegularExpressionMatchingTest {
    private final RegularExpressionMatching sol = new RegularExpressionMatching();

    @Test
    void example1() {
        assertFalse(sol.isMatch("aa", "a"));
    }

    @Test
    void example2() {
        assertTrue(sol.isMatch("aa", "a*"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isMatch("aab", "c*a*b"));
    }
}
