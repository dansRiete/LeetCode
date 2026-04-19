package dev.alexkzk.algo.neetcode150.backtracking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class WordSearchTest {
    private final WordSearch sol = new WordSearch();

    @Test
    void example1() {
        char[][] board = {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        assertTrue(sol.exist(board, "ABCCED"));
    }

    @Test
    void example2() {
        char[][] board = {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        assertFalse(sol.exist(board, "ABCB"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.exist(new char[][]{{'a'}}, "a"));
    }
}
