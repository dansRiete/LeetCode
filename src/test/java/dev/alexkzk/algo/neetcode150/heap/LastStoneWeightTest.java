package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LastStoneWeightTest {
    private final LastStoneWeight sol = new LastStoneWeight();

    @Test
    void example1() {
        assertEquals(1, sol.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.lastStoneWeight(new int[]{1}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.lastStoneWeight(new int[]{2, 2}));
    }
}
