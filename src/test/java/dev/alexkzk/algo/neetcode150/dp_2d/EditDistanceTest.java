package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class EditDistanceTest {
    private final EditDistance sol = new EditDistance();

    @Test
    void example1() {
        assertEquals(3, sol.minDistance("horse", "ros"));
    }

    @Test
    void example2() {
        assertEquals(5, sol.minDistance("intention", "execution"));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.minDistance("", ""));
    }
}
