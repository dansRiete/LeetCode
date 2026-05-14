package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {
    /** LC #36 — Valid Sudoku [Medium] */
    public boolean isValidSudoku(char[][] board) {
        Set<Integer> intSet = new HashSet();
        for(int y = 0; y < 9; y += 3) {
            intSet = new HashSet();
            for(int x = 0; x < 9; x += 3) {
                intSet = new HashSet();
                for(int o = x; o < x + 3; o++) {
                    for(int i = y; i < y + 3; i++) {
                        if(board[o][i] == '.') {
                            continue;
                        }
                        int currNum = Character.getNumericValue(board[o][i]);
                        if(currNum == 0 || intSet.contains(currNum)) {
                            return false;
                        } else {
                            intSet.add(currNum);
                        }
                    }
                }
            }
        }

        for(int o = 0; o < 9; o++) {
            intSet = new HashSet();
            for(int i = 0; i < 9; i++) {
                if(board[o][i] == '.') {
                    continue;
                }
                int currNum = Character.getNumericValue(board[o][i]);
                if(currNum == 0 || intSet.contains(currNum)) {
                    return false;
                } else {
                    intSet.add(currNum);
                }
            }

        }

        for(int o = 0; o < 9; o++) {
            intSet = new HashSet();
            for(int i = 0; i < 9; i++) {
                if(board[i][o] == '.') {
                    continue;
                }
                int currNum = Character.getNumericValue(board[i][o]);
                if(currNum == 0 || intSet.contains(currNum)) {
                    return false;
                } else {
                    intSet.add(currNum);
                }
            }

        }
        return true;
    }

    // Single-pass with fixed boolean arrays — O(1) time and space.
    // Box index = (row/3)*3 + (col/3). Uses char arithmetic to avoid getNumericValue.
    public boolean isValidSudokuOptimal(char[][] board) {
        boolean[][] rows  = new boolean[9][9];
        boolean[][] cols  = new boolean[9][9];
        boolean[][] boxes = new boolean[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') continue;
                int d   = board[r][c] - '1';
                int box = (r / 3) * 3 + (c / 3);
                if (rows[r][d] || cols[c][d] || boxes[box][d]) return false;
                rows[r][d] = cols[c][d] = boxes[box][d] = true;
            }
        }
        return true;
    }
}
