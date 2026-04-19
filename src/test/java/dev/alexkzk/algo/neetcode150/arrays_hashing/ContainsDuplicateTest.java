package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ContainsDuplicateTest {
    private final ContainsDuplicate sol = new ContainsDuplicate();

    @Test
    void example1() {
        assertTrue(sol.containsDuplicate(new int[]{1, 2, 3, 1}));
    }

    @Test
    void example2() {
        assertFalse(sol.containsDuplicate(new int[]{1, 2, 3, 4}));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.containsDuplicate(new int[]{1}));
    }
}
