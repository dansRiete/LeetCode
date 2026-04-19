package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ClimbingStairsTest {
    private final ClimbingStairs sol = new ClimbingStairs();

    @Test
    void example1() {
        assertEquals(2, sol.climbStairs(2));
    }

    @Test
    void example2() {
        assertEquals(3, sol.climbStairs(3));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.climbStairs(1));
    }
}
