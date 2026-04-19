package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class GasStationTest {
    private final GasStation sol = new GasStation();

    @Test
    void example1() {
        assertEquals(3, sol.canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    void example2() {
        assertEquals(-1, sol.canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.canCompleteCircuit(new int[]{5}, new int[]{4}));
    }
}
