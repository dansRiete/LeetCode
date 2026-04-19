package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class WallsAndGatesTest {
    private final WallsAndGates sol = new WallsAndGates();

    private static final int INF = Integer.MAX_VALUE;

    @Test
    void example1() {
        int[][] rooms = {{INF,-1,0,INF},{INF,INF,INF,-1},{INF,-1,INF,-1},{0,-1,INF,INF}};
        sol.wallsAndGates(rooms);
        assertEquals(3, rooms[0][0]);
        assertEquals(2, rooms[0][3]);
    }

    @Test
    void example2() {
        int[][] rooms = {{-1}};
        sol.wallsAndGates(rooms);
        assertEquals(-1, rooms[0][0]);
    }

    @Test
    void edgeCase() {
        int[][] rooms = {{0}};
        sol.wallsAndGates(rooms);
        assertEquals(0, rooms[0][0]);
    }
}
