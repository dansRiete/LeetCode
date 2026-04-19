package dev.alexkzk.algo.neetcode150.dp_2d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DistinctSubsequencesTest {
    private final DistinctSubsequences sol = new DistinctSubsequences();

    @Test
    void example1() {
        assertEquals(3, sol.numDistinct("rabbbit", "rabbit"));
    }

    @Test
    void example2() {
        assertEquals(5, sol.numDistinct("babgbag", "bag"));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.numDistinct("a", "a"));
    }
}
