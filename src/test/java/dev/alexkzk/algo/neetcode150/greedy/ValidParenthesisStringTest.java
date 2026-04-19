package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ValidParenthesisStringTest {
    private final ValidParenthesisString sol = new ValidParenthesisString();

    @Test
    void example1() {
        assertTrue(sol.checkValidString("()"));
    }

    @Test
    void example2() {
        assertTrue(sol.checkValidString("(*)(*)"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.checkValidString("(*"));
    }
}
