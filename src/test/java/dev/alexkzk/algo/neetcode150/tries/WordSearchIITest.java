package dev.alexkzk.algo.neetcode150.tries;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class WordSearchIITest {
    private final WordSearchII sol = new WordSearchII();

    @Test
    void example1() {
        char[][] board = {{'o','a','a','n'},{'e','t','a','e'},{'i','h','k','r'},{'i','f','l','v'}};
        List<String> result = sol.findWords(board, new String[]{"oath","pea","eat","rain"});
        assertEquals(2, result.size());
        assertTrue(result.contains("eat"));
        assertTrue(result.contains("oath"));
    }

    @Test
    void example2() {
        char[][] board = {{'a','b'},{'c','d'}};
        List<String> result = sol.findWords(board, new String[]{"abcd"});
        assertEquals(0, result.size());
    }

    @Test
    void edgeCase() {
        char[][] board = {{'a'}};
        List<String> result = sol.findWords(board, new String[]{"a"});
        assertEquals(1, result.size());
    }
}
