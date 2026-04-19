package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class HandOfStraightsTest {
    private final HandOfStraights sol = new HandOfStraights();

    @Test
    void example1() {
        assertTrue(sol.isNStraightHand(new int[]{1, 2, 3, 6, 2, 3, 4, 7, 8}, 3));
    }

    @Test
    void example2() {
        assertFalse(sol.isNStraightHand(new int[]{1, 2, 3, 4, 5}, 4));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isNStraightHand(new int[]{1}, 1));
    }
}
