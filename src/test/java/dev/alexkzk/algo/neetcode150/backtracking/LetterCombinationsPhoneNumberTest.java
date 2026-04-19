package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LetterCombinationsPhoneNumberTest {
    private final LetterCombinationsPhoneNumber sol = new LetterCombinationsPhoneNumber();

    @Test
    void example1() {
        List<String> result = sol.letterCombinations("23");
        assertEquals(9, result.size());
        assertTrue(result.contains("ad"));
    }

    @Test
    void example2() {
        List<String> result = sol.letterCombinations("");
        assertEquals(0, result.size());
    }

    @Test
    void edgeCase() {
        List<String> result = sol.letterCombinations("2");
        assertEquals(3, result.size());
    }
}
