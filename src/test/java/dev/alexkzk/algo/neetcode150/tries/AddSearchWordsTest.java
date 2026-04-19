package dev.alexkzk.algo.neetcode150.tries;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class AddSearchWordsTest {
    @Test
    void example1() {
        AddSearchWords wd = new AddSearchWords();
        wd.addWord("bad");
        wd.addWord("dad");
        wd.addWord("mad");
        assertFalse(wd.search("pad"));
        assertTrue(wd.search("bad"));
        assertTrue(wd.search(".ad"));
        assertTrue(wd.search("b.."));
    }

    @Test
    void example2() {
        AddSearchWords wd = new AddSearchWords();
        wd.addWord("a");
        assertTrue(wd.search("a"));
        assertFalse(wd.search("aa"));
    }

    @Test
    void edgeCase() {
        AddSearchWords wd = new AddSearchWords();
        assertFalse(wd.search("."));
    }
}
