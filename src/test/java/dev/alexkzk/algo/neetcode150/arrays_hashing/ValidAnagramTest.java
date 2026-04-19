package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ValidAnagramTest {
    private final ValidAnagram sol = new ValidAnagram();

    @Test
    void example1() {
        assertTrue(sol.isAnagram("anagram", "nagaram"));
    }

    @Test
    void example2() {
        assertFalse(sol.isAnagram("rat", "car"));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.isAnagram("ab", "a"));
    }
}
