package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class InterleavingStringTest {
    private final InterleavingString sol = new InterleavingString();

    @Test
    void example1() {
        assertTrue(sol.isInterleave("aabcc", "dbbca", "aadbbcbcac"));
    }

    @Test
    void example2() {
        assertFalse(sol.isInterleave("aabcc", "dbbca", "aadbbbaccc"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isInterleave("", "", ""));
    }
}
