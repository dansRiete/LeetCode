package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BestTimeBuySellStockTest {
    private final BestTimeBuySellStock sol = new BestTimeBuySellStock();

    @Test
    void example1() {
        assertEquals(5, sol.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }

    @Test
    void example2() {
        assertEquals(0, sol.maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.maxProfit(new int[]{1}));
    }
}
