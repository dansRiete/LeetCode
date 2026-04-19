package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class EvaluateRPNTest {
    private final EvaluateRPN sol = new EvaluateRPN();

    @Test
    void example1() {
        assertEquals(9, sol.evalRPN(new String[]{"2", "1", "+", "3", "*"}));
    }

    @Test
    void example2() {
        assertEquals(6, sol.evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }

    @Test
    void edgeCase() {
        assertEquals(22, sol.evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));
    }
}
