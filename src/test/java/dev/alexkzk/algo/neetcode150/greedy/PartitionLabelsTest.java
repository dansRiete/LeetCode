package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PartitionLabelsTest {
    private final PartitionLabels sol = new PartitionLabels();

    @Test
    void example1() {
        assertEquals(List.of(9, 7, 8), sol.partitionLabels("ababcbacadefegdehijhklij"));
    }

    @Test
    void example2() {
        assertEquals(List.of(10), sol.partitionLabels("eccbbbbdec"));
    }

    @Test
    void edgeCase() {
        assertEquals(List.of(1), sol.partitionLabels("a"));
    }
}
