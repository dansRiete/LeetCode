package dev.alexkzk.algo.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RomanToIntegerTest {

    private static int solve(String s) {
        return RomanToInteger.romanToInt(s);
    }

    // ── single symbols ────────────────────────────────────────────────────────

    @Test
    void singleI() { assertEquals(1, solve("I")); }

    @Test
    void singleV() { assertEquals(5, solve("V")); }

    @Test
    void singleX() { assertEquals(10, solve("X")); }

    @Test
    void singleL() { assertEquals(50, solve("L")); }

    @Test
    void singleC() { assertEquals(100, solve("C")); }

    @Test
    void singleD() { assertEquals(500, solve("D")); }

    @Test
    void singleM() { assertEquals(1000, solve("M")); }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertEquals(3, solve("III"));
    }

    @Test
    void leetcodeExample2() {
        assertEquals(58, solve("LVIII"));
    }

    @Test
    void leetcodeExample3() {
        assertEquals(1994, solve("MCMXCIV"));
    }

    // ── subtraction cases ────────────────────────────────────────────────────

    @Test
    void subtraction_IV() { assertEquals(4, solve("IV")); }

    @Test
    void subtraction_IX() { assertEquals(9, solve("IX")); }

    @Test
    void subtraction_XL() { assertEquals(40, solve("XL")); }

    @Test
    void subtraction_XC() { assertEquals(90, solve("XC")); }

    @Test
    void subtraction_CD() { assertEquals(400, solve("CD")); }

    @Test
    void subtraction_CM() { assertEquals(900, solve("CM")); }

    // ── combined ─────────────────────────────────────────────────────────────

    @Test
    void combined_XIV() {
        assertEquals(14, solve("XIV"));
    }

    @Test
    void combined_XLII() {
        assertEquals(42, solve("XLII"));
    }

    @Test
    void combined_CDXLIV() {
        assertEquals(444, solve("CDXLIV"));
    }

    // ── boundaries ───────────────────────────────────────────────────────────

    @Test
    void minimum() {
        assertEquals(1, solve("I"));
    }

    @Test
    void maximum() {
        assertEquals(3999, solve("MMMCMXCIX"));
    }
}
