package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BurstBalloonsTest {
    private final BurstBalloons sol = new BurstBalloons();

    @Test
    void example1() {
        assertEquals(167, sol.maxCoins(new int[]{3, 1, 5, 8}));
    }

    @Test
    void example2() {
        assertEquals(10, sol.maxCoins(new int[]{1, 5}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.maxCoins(new int[]{1}));
    }
}
