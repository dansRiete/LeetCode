package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MultiplyStringsTest {
    private final MultiplyStrings sol = new MultiplyStrings();

    @Test
    void example1() {
        assertEquals("6", sol.multiply("2", "3"));
    }

    @Test
    void example2() {
        assertEquals("56088", sol.multiply("123", "456"));
    }

    @Test
    void edgeCase() {
        assertEquals("0", sol.multiply("0", "12345"));
    }
}
