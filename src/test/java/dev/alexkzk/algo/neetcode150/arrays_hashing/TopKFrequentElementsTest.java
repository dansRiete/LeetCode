package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TopKFrequentElementsTest {
    private final TopKFrequentElements sol = new TopKFrequentElements();

    private Set<Integer> asSet(int[] arr) {
        Set<Integer> s = new HashSet<>();
        for (int v : arr) s.add(v);
        return s;
    }

    private void assertTopK(int[] nums, int k, int... expectedElements) {
        int[] result = sol.topKFrequent(nums, k);
        assertEquals(k, result.length, "result must contain exactly k elements");
        assertEquals(Set.of(Arrays.stream(expectedElements).boxed().toArray(Integer[]::new)), asSet(result));
    }

    @Test
    void example1_twoMostFrequent() {
        assertTopK(new int[]{1, 1, 1, 2, 2, 3}, 2, 1, 2);
    }

    @Test
    void example2_singleElement() {
        assertTopK(new int[]{1}, 1, 1);
    }

    @Test
    void allElementsSameFrequency() {
        assertTopK(new int[]{1, 2}, 2, 1, 2);
    }

    @Test
    void kEqualsOne() {
        assertTopK(new int[]{4, 4, 4, 5, 5, 6}, 1, 4);
    }

    @Test
    void negativeNumbers_twoTiedAtFreq2() {
        // -1 appears 2x, 2 appears 2x — both should be returned; result must be exactly k=2 elements
        assertTopK(new int[]{4, 1, -1, 2, -1, 2, 3}, 2, -1, 2);
    }

    @Test
    void allNegativeNumbers() {
        assertTopK(new int[]{-1, -1, -2, -2, -2, -3}, 1, -2);
    }

    @Test
    void negativeAndPositiveMixed() {
        assertTopK(new int[]{-5, -5, 3, 3, 7}, 2, -5, 3);
    }

    @Test
    void largeK_returnsAllWhenAllTied() {
        assertTopK(new int[]{1, 2, 3}, 3, 1, 2, 3);
    }
}
