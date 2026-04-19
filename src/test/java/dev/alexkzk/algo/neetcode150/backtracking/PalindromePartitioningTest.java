package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PalindromePartitioningTest {
    private final PalindromePartitioning sol = new PalindromePartitioning();

    @Test
    void example1() {
        List<List<String>> result = sol.partition("aab");
        assertEquals(2, result.size());
    }

    @Test
    void example2() {
        List<List<String>> result = sol.partition("a");
        assertEquals(1, result.size());
        assertEquals(List.of("a"), result.get(0));
    }

    @Test
    void edgeCase() {
        List<List<String>> result = sol.partition("aa");
        assertEquals(2, result.size());
    }
}
