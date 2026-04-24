package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DailyTemperaturesTest {
    private final DailyTemperatures sol = new DailyTemperatures();

    @Test
    void example1() {
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, sol.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
    }

    @Test
    void example2_strictlyIncreasing() {
        assertArrayEquals(new int[]{1, 1, 0}, sol.dailyTemperatures(new int[]{30, 40, 50}));
    }

    @Test
    void strictlyDecreasing_allZeros() {
        assertArrayEquals(new int[]{0, 0, 0}, sol.dailyTemperatures(new int[]{30, 20, 10}));
    }

    @Test
    void singleElement() {
        assertArrayEquals(new int[]{0}, sol.dailyTemperatures(new int[]{42}));
    }

    @Test
    void allSameTemperature() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, sol.dailyTemperatures(new int[]{20, 20, 20, 20}));
    }

    @Test
    void duplicatesFollowedByWarmer() {
        // verifies that equal temperatures do not count as warmer
        assertArrayEquals(new int[]{2, 1, 0}, sol.dailyTemperatures(new int[]{1, 1, 3}));
    }

    @Test
    void warmerDayFarAway() {
        // stack must hold many indices before finding a warmer day
        assertArrayEquals(new int[]{3, 2, 1, 0}, sol.dailyTemperatures(new int[]{2, 1, 1, 3}));
    }

    @Test
    void lastElementAlwaysZero() {
        assertArrayEquals(new int[]{1, 0}, sol.dailyTemperatures(new int[]{10, 20}));
    }

    @Test
    void warmthOnlyAtEnd() {
        assertArrayEquals(new int[]{4, 3, 2, 1, 0}, sol.dailyTemperatures(new int[]{10, 10, 10, 10, 50}));
    }
}
