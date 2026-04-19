package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class NetworkDelayTimeTest {
    private final NetworkDelayTime sol = new NetworkDelayTime();

    @Test
    void example1() {
        assertEquals(2, sol.networkDelayTime(new int[][]{{2,1,1},{2,3,1},{3,4,1}}, 4, 2));
    }

    @Test
    void example2() {
        assertEquals(1, sol.networkDelayTime(new int[][]{{1,2,1}}, 2, 1));
    }

    @Test
    void edgeCase() {
        assertEquals(-1, sol.networkDelayTime(new int[][]{{1,2,1}}, 2, 2));
    }
}
