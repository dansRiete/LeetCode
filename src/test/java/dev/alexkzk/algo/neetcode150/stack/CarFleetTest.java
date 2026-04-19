package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CarFleetTest {
    private final CarFleet sol = new CarFleet();

    @Test
    void example1() {
        assertEquals(3, sol.carFleet(12, new int[]{10, 8, 0, 5, 3}, new int[]{2, 4, 1, 1, 3}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.carFleet(10, new int[]{3}, new int[]{3}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.carFleet(100, new int[]{0, 2, 4}, new int[]{4, 2, 1}));
    }
}
