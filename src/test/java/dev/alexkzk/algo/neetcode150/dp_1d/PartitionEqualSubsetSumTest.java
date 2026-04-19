package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PartitionEqualSubsetSumTest {
    private final PartitionEqualSubsetSum sol = new PartitionEqualSubsetSum();

    @Test
    void example1() {
        assertTrue(sol.canPartition(new int[]{1, 5, 11, 5}));
    }

    @Test
    void example2() {
        assertFalse(sol.canPartition(new int[]{1, 2, 3, 5}));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.canPartition(new int[]{1, 1, 1, 1, 1}));
    }
}
