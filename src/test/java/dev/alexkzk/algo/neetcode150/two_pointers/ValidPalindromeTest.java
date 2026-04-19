package dev.alexkzk.algo.neetcode150.two_pointers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ValidPalindromeTest {
    private final ValidPalindrome sol = new ValidPalindrome();

    @Test
    void example1() {
        assertTrue(sol.isPalindrome("A man, a plan, a canal: Panama"));
    }

    @Test
    void example2() {
        assertFalse(sol.isPalindrome("race a car"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isPalindrome(" "));
    }
}
