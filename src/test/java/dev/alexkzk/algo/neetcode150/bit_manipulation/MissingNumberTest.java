package dev.alexkzk.algo.neetcode150.bit_manipulation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MissingNumberTest {
    private final MissingNumber sol = new MissingNumber();

    @Test
    void example1() {
        assertEquals(2, sol.missingNumber(new int[]{3, 0, 1}));
    }

    @Test
    void example2() {
        assertEquals(2, sol.missingNumber(new int[]{0, 1}));
    }

    @Test
    void edgeCase() {
        assertEquals(8, sol.missingNumber(new int[]{9,6,4,2,3,5,7,0,1}));
    }
}
