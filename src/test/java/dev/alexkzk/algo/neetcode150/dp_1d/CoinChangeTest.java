package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CoinChangeTest {
    private final CoinChange sol = new CoinChange();

    @Test
    void example1() {
        assertEquals(3, sol.coinChange(new int[]{1, 2, 5}, 11));
    }

    @Test
    void example2() {
        assertEquals(-1, sol.coinChange(new int[]{2}, 3));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.coinChange(new int[]{1}, 0));
    }
}
