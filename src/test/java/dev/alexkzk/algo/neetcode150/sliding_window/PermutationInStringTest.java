package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PermutationInStringTest {
    private final PermutationInString sol = new PermutationInString();

    @Test
    void example1() {
        assertTrue(sol.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void example2() {
        assertFalse(sol.checkInclusion("ab", "eidboaoo"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.checkInclusion("a", "a"));
    }
}
