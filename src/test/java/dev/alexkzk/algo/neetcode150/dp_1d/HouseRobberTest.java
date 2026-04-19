package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class HouseRobberTest {
    private final HouseRobber sol = new HouseRobber();

    @Test
    void example1() {
        assertEquals(4, sol.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    void example2() {
        assertEquals(12, sol.rob(new int[]{2, 7, 9, 3, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(5, sol.rob(new int[]{5}));
    }
}
