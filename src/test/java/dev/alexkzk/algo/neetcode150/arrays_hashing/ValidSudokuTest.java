package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidSudokuTest {
    private final ValidSudoku sol = new ValidSudoku();

    // ── helpers ──────────────────────────────────────────────────────────────

    private static char[][] empty() {
        char[][] b = new char[9][9];
        for (char[] row : b) java.util.Arrays.fill(row, '.');
        return b;
    }

    /** Assert both implementations agree */
    private void assertValid(char[][] board) {
        assertTrue(sol.isValidSudoku(board),         "original should be valid");
        assertTrue(sol.isValidSudokuOptimal(board),  "optimal should be valid");
    }

    private void assertInvalid(char[][] board) {
        assertFalse(sol.isValidSudoku(board),        "original should be invalid");
        assertFalse(sol.isValidSudokuOptimal(board), "optimal should be invalid");
    }

    // ── provided examples ────────────────────────────────────────────────────

    @Test
    void example1_valid() {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        assertValid(board);
    }

    @Test
    void example2_duplicateInColumn() {
        char[][] board = {
            {'8','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        assertInvalid(board);
    }

    // ── edge cases ───────────────────────────────────────────────────────────

    @Test
    void allDots_isValid() {
        assertValid(empty());
    }

    @Test
    void singleDigit_isValid() {
        char[][] board = empty();
        board[4][4] = '5';
        assertValid(board);
    }

    @Test
    void completedBoard_isValid() {
        char[][] board = {
            {'5','3','4','6','7','8','9','1','2'},
            {'6','7','2','1','9','5','3','4','8'},
            {'1','9','8','3','4','2','5','6','7'},
            {'8','5','9','7','6','1','4','2','3'},
            {'4','2','6','8','5','3','7','9','1'},
            {'7','1','3','9','2','4','8','5','6'},
            {'9','6','1','5','3','7','2','8','4'},
            {'2','8','7','4','1','9','6','3','5'},
            {'3','4','5','2','8','6','1','7','9'}
        };
        assertValid(board);
    }

    // ── row violations ───────────────────────────────────────────────────────

    @Test
    void duplicateInFirstRow() {
        char[][] board = empty();
        board[0][0] = '5';
        board[0][8] = '5';
        assertInvalid(board);
    }

    @Test
    void duplicateInLastRow() {
        char[][] board = empty();
        board[8][0] = '9';
        board[8][5] = '9';
        assertInvalid(board);
    }

    // ── column violations ────────────────────────────────────────────────────

    @Test
    void duplicateInFirstColumn() {
        char[][] board = empty();
        board[0][0] = '3';
        board[7][0] = '3';
        assertInvalid(board);
    }

    @Test
    void duplicateInLastColumn() {
        char[][] board = empty();
        board[1][8] = '7';
        board[6][8] = '7';
        assertInvalid(board);
    }

    // ── box violations ────────────────────────────────────────────────────────

    @Test
    void duplicateInTopLeftBox_rowAndColClean() {
        // '5' at (0,0) and (1,1): different rows, different cols, same box-0
        char[][] board = empty();
        board[0][0] = '5';
        board[1][1] = '5';
        assertInvalid(board);
    }

    @Test
    void duplicateInMiddleBox_rowAndColClean() {
        // box 4 (rows 3-5, cols 3-5): '2' at (3,3) and (5,5)
        char[][] board = empty();
        board[3][3] = '2';
        board[5][5] = '2';
        assertInvalid(board);
    }

    @Test
    void duplicateInBottomRightBox_rowAndColClean() {
        // box 8 (rows 6-8, cols 6-8): '9' at (6,6) and (8,8)
        char[][] board = empty();
        board[6][6] = '9';
        board[8][8] = '9';
        assertInvalid(board);
    }
}
