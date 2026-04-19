package dev.alexkzk.algo.neetcode150.dp_1d;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class WordBreakTest {
    private final WordBreak sol = new WordBreak();

    @Test
    void example1() {
        assertTrue(sol.wordBreak("leetcode", List.of("leet", "code")));
    }

    @Test
    void example2() {
        assertTrue(sol.wordBreak("applepenapple", List.of("apple", "pen")));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));
    }
}
