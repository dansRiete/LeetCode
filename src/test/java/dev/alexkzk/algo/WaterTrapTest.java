package dev.alexkzk.algo;

import dev.alexkzk.algo.hard.WaterTrap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaterTrapTest {

    // --- canonical examples ---

    @Test
    void leetcodeExample1() {
        // [0,1,0,2,1,0,1,3,2,1,2,1] => 6
        assertEquals(6, WaterTrap.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }

    @Test
    void leetcodeExample2() {
        // [4,2,0,3,2,5] => 9
        assertEquals(9, WaterTrap.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }

    // --- edge cases ---

    @Test
    void emptyArray() {
        assertEquals(0, WaterTrap.trap(new int[]{}));
    }

    @Test
    void singleBar() {
        assertEquals(0, WaterTrap.trap(new int[]{5}));
    }

    @Test
    void twoBars() {
        // two bars can never trap water
        assertEquals(0, WaterTrap.trap(new int[]{3, 4}));
    }

    @Test
    void allSameHeight() {
        // flat surface — no water trapped
        assertEquals(0, WaterTrap.trap(new int[]{3, 3, 3, 3}));
    }

    @Test
    void allZeros() {
        assertEquals(0, WaterTrap.trap(new int[]{0, 0, 0, 0}));
    }

    @Test
    void monotonicallyIncreasing() {
        // water can only be trapped with walls on both sides
        assertEquals(0, WaterTrap.trap(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void monotonicallyDecreasing() {
        assertEquals(0, WaterTrap.trap(new int[]{5, 4, 3, 2, 1}));
    }

    // --- simple, easy-to-verify shapes ---

    @Test
    void simpleValley() {
        // walls of height 2 with a 0-gap in the middle => 2 units
        assertEquals(2, WaterTrap.trap(new int[]{2, 0, 2}));
    }

    @Test
    void asymmetricWalls() {
        // left wall=3, right wall=1 — water level bounded by shorter wall
        // positions: [3,0,0,1] => right wall limits to 1, trapped = 1+1 = 2
        assertEquals(2, WaterTrap.trap(new int[]{3, 0, 0, 1}));
    }

    @Test
    void asymmetricWallsTallRight() {
        // [1,0,0,3] => left wall limits to 1, trapped = 1+1 = 2
        assertEquals(2, WaterTrap.trap(new int[]{1, 0, 0, 3}));
    }

    @Test
    void multipleValleys() {
        // [2,0,2,0,2] => 2 separate valleys, 2 units each = 4
        assertEquals(4, WaterTrap.trap(new int[]{2, 0, 2, 0, 2}));
    }

    @Test
    void pyramidShape() {
        // [0,1,2,3,2,1,0] => no water trapped (mountain shape)
        assertEquals(0, WaterTrap.trap(new int[]{0, 1, 2, 3, 2, 1, 0}));
    }

    @Test
    void singleDeepHole() {
        // [5,0,5] => 5 units
        assertEquals(5, WaterTrap.trap(new int[]{5, 0, 5}));
    }

    @Test
    void innerPeakDoesNotDrain() {
        // [3,1,2,1,3] — inner peak at index 2 (height 2) doesn't prevent trapping
        // water fills to 3 on both sides: (3-1)+(3-2)+(3-1) = 2+1+2 = 5
        assertEquals(5, WaterTrap.trap(new int[]{3, 1, 2, 1, 3}));
    }

    @Test
    void tallBarInMiddle() {
        // [1,3,1] => no water (middle is tallest)
        assertEquals(0, WaterTrap.trap(new int[]{1, 3, 1}));
    }
}
