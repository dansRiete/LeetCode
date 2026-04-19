package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class KokoEatingBananasTest {
    private final KokoEatingBananas sol = new KokoEatingBananas();

    @Test
    void example1() {
        assertEquals(4, sol.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
    }

    @Test
    void example2() {
        assertEquals(30, sol.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.minEatingSpeed(new int[]{1, 1}, 100));
    }
}
