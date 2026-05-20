package dev.alexkzk.algo.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerToRomanTest {

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertEquals("MMMDCCXLIX", IntegerToRoman.intToRoman(3749));
    }

    @Test
    void leetcodeExample2() {
        assertEquals("LVIII", IntegerToRoman.intToRoman(58));
    }

    @Test
    void leetcodeExample3() {
        assertEquals("MCMXCIV", IntegerToRoman.intToRoman(1994));
    }

    // ── boundaries ───────────────────────────────────────────────────────────

    @Test
    void minimum() {
        assertEquals("I", IntegerToRoman.intToRoman(1));
    }

    @Test
    void maximum() {
        assertEquals("MMMCMXCIX", IntegerToRoman.intToRoman(3999));
    }

    // ── subtractive cases ────────────────────────────────────────────────────

    @Test
    void four() {
        assertEquals("IV", IntegerToRoman.intToRoman(4));
    }

    @Test
    void nine() {
        assertEquals("IX", IntegerToRoman.intToRoman(9));
    }

    @Test
    void forty() {
        assertEquals("XL", IntegerToRoman.intToRoman(40));
    }

    @Test
    void ninety() {
        assertEquals("XC", IntegerToRoman.intToRoman(90));
    }

    @Test
    void fourHundred() {
        assertEquals("CD", IntegerToRoman.intToRoman(400));
    }

    @Test
    void nineHundred() {
        assertEquals("CM", IntegerToRoman.intToRoman(900));
    }

    // ── round numbers ────────────────────────────────────────────────────────

    @Test
    void thousand() {
        assertEquals("M", IntegerToRoman.intToRoman(1000));
    }

    @Test
    void threeThousand() {
        assertEquals("MMM", IntegerToRoman.intToRoman(3000));
    }
}
