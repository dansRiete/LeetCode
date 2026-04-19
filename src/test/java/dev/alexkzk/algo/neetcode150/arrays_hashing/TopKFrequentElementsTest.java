package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TopKFrequentElementsTest {
    private final TopKFrequentElements sol = new TopKFrequentElements();

    @Test
    void example1() {
        int[] result = sol.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    @Test
    void example2() {
        int[] result = sol.topKFrequent(new int[]{1}, 1);
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    void edgeCase() {
        int[] result = sol.topKFrequent(new int[]{4, 4, 4, 5, 5, 6}, 1);
        assertArrayEquals(new int[]{4}, result);
    }
}
