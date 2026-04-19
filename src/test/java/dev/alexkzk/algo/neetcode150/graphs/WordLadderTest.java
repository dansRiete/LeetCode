package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class WordLadderTest {
    private final WordLadder sol = new WordLadder();

    @Test
    void example1() {
        assertEquals(5, sol.ladderLength("hit", "cog", List.of("hot","dot","dog","lot","log","cog")));
    }

    @Test
    void example2() {
        assertEquals(0, sol.ladderLength("hit", "cog", List.of("hot","dot","dog","lot","log")));
    }

    @Test
    void edgeCase() {
        assertEquals(2, sol.ladderLength("a", "c", List.of("a","b","c")));
    }
}
