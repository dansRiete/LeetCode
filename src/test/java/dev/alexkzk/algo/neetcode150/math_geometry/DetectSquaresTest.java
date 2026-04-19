package dev.alexkzk.algo.neetcode150.math_geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DetectSquaresTest {
    @Test
    void example1() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{3,10});
        ds.add(new int[]{11,2});
        ds.add(new int[]{3,2});
        assertEquals(1, ds.count(new int[]{11,10}));
        assertEquals(0, ds.count(new int[]{14,8}));
        ds.add(new int[]{11,2});
        assertEquals(2, ds.count(new int[]{11,10}));
    }

    @Test
    void example2() {
        DetectSquares ds = new DetectSquares();
        assertEquals(0, ds.count(new int[]{0,0}));
    }

    @Test
    void edgeCase() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{0,0});
        ds.add(new int[]{0,1});
        ds.add(new int[]{1,0});
        assertEquals(1, ds.count(new int[]{1,1}));
    }
}
