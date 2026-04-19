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
    void example2() {
        assertArrayEquals(new int[]{1, 1, 0}, sol.dailyTemperatures(new int[]{30, 40, 50}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{0, 0, 0}, sol.dailyTemperatures(new int[]{30, 20, 10}));
    }
}
