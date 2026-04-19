package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CoinChangeIITest {
    private final CoinChangeII sol = new CoinChangeII();

    @Test
    void example1() {
        assertEquals(4, sol.change(5, new int[]{1, 2, 5}));
    }

    @Test
    void example2() {
        assertEquals(0, sol.change(3, new int[]{2}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.change(0, new int[]{1, 2, 3}));
    }
}
