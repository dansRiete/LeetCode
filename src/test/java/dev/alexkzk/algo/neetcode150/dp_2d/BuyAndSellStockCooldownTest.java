package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BuyAndSellStockCooldownTest {
    private final BuyAndSellStockCooldown sol = new BuyAndSellStockCooldown();

    @Test
    void example1() {
        assertEquals(3, sol.maxProfit(new int[]{1, 2, 3, 0, 2}));
    }

    @Test
    void example2() {
        assertEquals(0, sol.maxProfit(new int[]{1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.maxProfit(new int[]{2, 1}));
    }
}
