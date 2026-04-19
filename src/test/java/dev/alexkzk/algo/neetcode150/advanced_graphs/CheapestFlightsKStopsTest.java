package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CheapestFlightsKStopsTest {
    private final CheapestFlightsKStops sol = new CheapestFlightsKStops();

    @Test
    void example1() {
        assertEquals(700, sol.findCheapestPrice(4, new int[][]{{0,1,100},{1,2,100},{2,0,100},{1,3,600},{2,3,200}}, 0, 3, 1));
    }

    @Test
    void example2() {
        assertEquals(200, sol.findCheapestPrice(3, new int[][]{{0,1,100},{1,2,100},{0,2,500}}, 0, 2, 1));
    }

    @Test
    void edgeCase() {
        assertEquals(500, sol.findCheapestPrice(3, new int[][]{{0,1,100},{1,2,100},{0,2,500}}, 0, 2, 0));
    }
}
