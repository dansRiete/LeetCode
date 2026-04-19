package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SumTwoIntegersTest {
    private final SumTwoIntegers sol = new SumTwoIntegers();

    @Test
    void example1() {
        assertEquals(3, sol.getSum(1, 2));
    }

    @Test
    void example2() {
        assertEquals(5, sol.getSum(2, 3));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.getSum(-1, 1));
    }
}
