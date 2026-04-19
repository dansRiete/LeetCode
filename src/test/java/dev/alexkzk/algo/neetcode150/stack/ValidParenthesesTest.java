package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ValidParenthesesTest {
    private final ValidParentheses sol = new ValidParentheses();

    @Test
    void example1() {
        assertTrue(sol.isValid("()"));
    }

    @Test
    void example2() {
        assertTrue(sol.isValid("()[]{}"));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.isValid("(]"));
    }
}
