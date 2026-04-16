package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DailyTemperaturesTest {

    private static int[] solve(int... temperatures) {
        return DailyTemperatures.dailyTemperatures(temperatures);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, solve(73, 74, 75, 71, 69, 72, 76, 73));
    }

    @Test
    void leetcodeExample2() {
        assertArrayEquals(new int[]{1, 1, 1, 0}, solve(30, 40, 50, 60));
    }

    @Test
    void leetcodeExample3() {
        assertArrayEquals(new int[]{1, 1, 0}, solve(30, 60, 90));
    }

    // ── no warmer day ────────────────────────────────────────────────────────

    @Test
    void descendingTemperatures() {
        // no day is warmer than the previous — all zeros
        assertArrayEquals(new int[]{0, 0, 0, 0}, solve(90, 80, 70, 60));
    }

    @Test
    void allSameTemperature() {
        // equal temperatures don't count as warmer
        assertArrayEquals(new int[]{0, 0, 0}, solve(50, 50, 50));
    }

    // ── single element ───────────────────────────────────────────────────────

    @Test
    void singleDay() {
        assertArrayEquals(new int[]{0}, solve(50));
    }

    // ── two elements ─────────────────────────────────────────────────────────

    @Test
    void twoDaysWarmer() {
        assertArrayEquals(new int[]{1, 0}, solve(40, 50));
    }

    @Test
    void twoDaysCooler() {
        assertArrayEquals(new int[]{0, 0}, solve(50, 40));
    }

    // ── warmer day is far away ────────────────────────────────────────────────

    @Test
    void warmerDayAtEnd() {
        // only the last day is warmer than the first
        assertArrayEquals(new int[]{4, 3, 2, 1, 0}, solve(30, 31, 32, 33, 100));
    }

    @Test
    void peakInMiddle() {
        // peak at index 2, everything after is cooler
        assertArrayEquals(new int[]{1, 1, 0, 0, 0}, solve(50, 60, 70, 65, 60));
    }

    // ── boundary temperatures ────────────────────────────────────────────────

    @Test
    void minTemperatures() {
        assertArrayEquals(new int[]{0, 0, 0}, solve(30, 30, 30));
    }

    @Test
    void maxTemperatures() {
        assertArrayEquals(new int[]{0, 0, 0}, solve(100, 100, 100));
    }
}
