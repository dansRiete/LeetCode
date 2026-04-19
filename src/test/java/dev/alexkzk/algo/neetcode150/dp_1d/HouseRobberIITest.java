package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class HouseRobberIITest {
    private final HouseRobberII sol = new HouseRobberII();

    @Test
    void example1() {
        assertEquals(3, sol.rob(new int[]{2, 3, 2}));
    }

    @Test
    void example2() {
        assertEquals(4, sol.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.rob(new int[]{1}));
    }
}
