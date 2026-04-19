package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DecodeWaysTest {
    private final DecodeWays sol = new DecodeWays();

    @Test
    void example1() {
        assertEquals(2, sol.numDecodings("12"));
    }

    @Test
    void example2() {
        assertEquals(3, sol.numDecodings("226"));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.numDecodings("06"));
    }
}
