package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class AlienDictionaryTest {
    private final AlienDictionary sol = new AlienDictionary();

    @Test
    void example1() {
        String result = sol.alienOrder(new String[]{"wrt","wrf","er","ett","rftt"});
        assertFalse(result.isEmpty());
        assertEquals(5, result.length());
    }

    @Test
    void example2() {
        String result = sol.alienOrder(new String[]{"z","x"});
        assertEquals("zx", result);
    }

    @Test
    void edgeCase() {
        // cycle: invalid
        assertEquals("", sol.alienOrder(new String[]{"z","x","z"}));
    }
}
