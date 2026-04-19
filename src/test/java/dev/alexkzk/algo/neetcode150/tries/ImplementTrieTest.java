package dev.alexkzk.algo.neetcode150.tries;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ImplementTrieTest {
    @Test
    void example1() {
        ImplementTrie trie = new ImplementTrie();
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
        assertTrue(trie.startsWith("app"));
        trie.insert("app");
        assertTrue(trie.search("app"));
    }

    @Test
    void example2() {
        ImplementTrie trie = new ImplementTrie();
        trie.insert("a");
        assertTrue(trie.startsWith("a"));
        assertFalse(trie.search("b"));
    }

    @Test
    void edgeCase() {
        ImplementTrie trie = new ImplementTrie();
        trie.insert("");
        assertTrue(trie.search(""));
    }
}
