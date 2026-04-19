package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PalindromicSubstringsTest {
    private final PalindromicSubstrings sol = new PalindromicSubstrings();

    @Test
    void example1() {
        assertEquals(3, sol.countSubstrings("abc"));
    }

    @Test
    void example2() {
        assertEquals(6, sol.countSubstrings("aaa"));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.countSubstrings("a"));
    }
}
