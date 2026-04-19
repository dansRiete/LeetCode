package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class GenerateParenthesesTest {
    private final GenerateParentheses sol = new GenerateParentheses();

    @Test
    void example1() {
        List<String> result = sol.generateParenthesis(3);
        assertEquals(5, result.size());
        assertTrue(result.contains("((()))"));
    }

    @Test
    void example2() {
        List<String> result = sol.generateParenthesis(1);
        assertEquals(List.of("()"), result);
    }

    @Test
    void edgeCase() {
        List<String> result = sol.generateParenthesis(2);
        assertEquals(2, result.size());
    }
}
