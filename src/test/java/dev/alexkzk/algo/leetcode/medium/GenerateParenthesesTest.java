package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateParenthesesTest {

    private static List<String> solve(int n) {
        return GenerateParentheses.generateParenthesis(n);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample_n1() {
        assertEquals(List.of("()"), solve(1));
    }

    @Test
    void leetcodeExample_n3() {
        var result = solve(3);
        assertEquals(5, result.size());
        assertTrue(result.contains("((()))"));
        assertTrue(result.contains("(()())"));
        assertTrue(result.contains("(())()"));
        assertTrue(result.contains("()(())"));
        assertTrue(result.contains("()()()"));
    }

    // ── size follows Catalan number ──────────────────────────────────────────

    @Test
    void n2_hasTwoCombinations() {
        var result = solve(2);
        assertEquals(2, result.size());
        assertTrue(result.contains("(())"));
        assertTrue(result.contains("()()"));
    }

    @Test
    void n4_has14Combinations() {
        assertEquals(14, solve(4).size());
    }

    @Test
    void n5_has42Combinations() {
        assertEquals(42, solve(5).size());
    }

    // ── every string must be valid ────────────────────────────────────────────

    @Test
    void allStringsAreValid_n3() {
        solve(3).forEach(s -> assertTrue(isValid(s), "Invalid: " + s));
    }

    @Test
    void allStringsAreValid_n4() {
        solve(4).forEach(s -> assertTrue(isValid(s), "Invalid: " + s));
    }

    // ── correct length ────────────────────────────────────────────────────────

    @Test
    void allStringsHaveCorrectLength_n3() {
        solve(3).forEach(s -> assertEquals(6, s.length(), "Wrong length: " + s));
    }

    @Test
    void allStringsHaveCorrectLength_n4() {
        solve(4).forEach(s -> assertEquals(8, s.length(), "Wrong length: " + s));
    }

    // ── no duplicates ─────────────────────────────────────────────────────────

    @Test
    void noDuplicates_n3() {
        var result = solve(3);
        assertEquals(result.size(), result.stream().distinct().count());
    }

    @Test
    void noDuplicates_n5() {
        var result = solve(5);
        assertEquals(result.size(), result.stream().distinct().count());
    }

    // ── boundary / constraint max ─────────────────────────────────────────────

    @Test
    void n8_has1430Combinations() {
        assertEquals(1430, solve(8).size());
    }

    @Test
    void n8_allStringsAreValid() {
        solve(8).forEach(s -> assertTrue(isValid(s), "Invalid: " + s));
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private static boolean isValid(String s) {
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') balance++;
            else balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }
}
