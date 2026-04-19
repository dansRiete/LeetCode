package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SurroundedRegionsTest {
    private final SurroundedRegions sol = new SurroundedRegions();

    @Test
    void example1() {
        char[][] board = {{'X','X','X','X'},{'X','O','O','X'},{'X','X','O','X'},{'X','O','X','X'}};
        sol.solve(board);
        assertEquals('X', board[1][1]);
        assertEquals('X', board[1][2]);
        assertEquals('O', board[3][1]);
    }

    @Test
    void example2() {
        char[][] board = {{'X'}};
        sol.solve(board);
        assertEquals('X', board[0][0]);
    }

    @Test
    void edgeCase() {
        char[][] board = {{'O'}};
        sol.solve(board);
        assertEquals('O', board[0][0]);
    }
}
