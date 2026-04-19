package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class UniquePathsTest {
    private final UniquePaths sol = new UniquePaths();

    @Test
    void example1() {
        assertEquals(28, sol.uniquePaths(3, 7));
    }

    @Test
    void example2() {
        assertEquals(3, sol.uniquePaths(3, 2));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.uniquePaths(1, 1));
    }
}
