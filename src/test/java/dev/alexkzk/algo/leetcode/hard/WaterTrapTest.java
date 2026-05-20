package dev.alexkzk.algo.leetcode.hard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterTrapTest {

    private static int solve(int... height) {
        return WaterTrap.trap(height);
    }

    // ── leetcode examples ────────────────────────────────────────────────────

    @Test
    void leetcodeExample1() {
        assertEquals(6, solve(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1));
    }

    @Test
    void leetcodeExample2() {
        assertEquals(9, solve(4, 2, 0, 3, 2, 5));
    }

    // ── no water trapped ─────────────────────────────────────────────────────

    @Test
    void monotonicallyIncreasing() {
        assertEquals(0, solve(1, 2, 3, 4, 5));
    }

    @Test
    void monotonicallyDecreasing() {
        assertEquals(0, solve(5, 4, 3, 2, 1));
    }

    @Test
    void allSameHeight() {
        assertEquals(0, solve(3, 3, 3, 3));
    }

    @Test
    void flatSurface() {
        assertEquals(0, solve(0, 0, 0, 0));
    }

    // ── minimal cases ────────────────────────────────────────────────────────

    @Test
    void singleBar() {
        assertEquals(0, solve(5));
    }

    @Test
    void twoBars() {
        // never traps water with only two bars
        assertEquals(0, solve(3, 5));
        assertEquals(0, solve(5, 3));
    }

    @Test
    void threeBars_valley() {
        // [3, 0, 3]: 3 units trapped
        assertEquals(3, solve(3, 0, 3));
    }

    @Test
    void threeBars_asymmetricWalls() {
        // [3, 0, 1]: water limited by the shorter wall (1) → 1 unit
        assertEquals(1, solve(3, 0, 1));
    }

    // ── simple basin ─────────────────────────────────────────────────────────

    @Test
    void singleDeepValley() {
        // [5, 0, 5]: 5 units trapped
        assertEquals(5, solve(5, 0, 5));
    }

    @Test
    void multipleValleysIndependent() {
        // [3, 0, 3, 0, 3]: two separate valleys, each 3 deep → 6 total
        assertEquals(6, solve(3, 0, 3, 0, 3));
    }

    // ── peak in middle ────────────────────────────────────────────────────────

    @Test
    void peakInMiddle_noWater() {
        // walls go up then down from a peak — no basin
        assertEquals(0, solve(1, 2, 3, 2, 1));
    }

    // ── water on both sides of an island ─────────────────────────────────────

    @Test
    void islandInLake() {
        // [4, 2, 4]: island at height 2 inside walls of height 4 → 2 units
        assertEquals(2, solve(4, 2, 4));
    }

    // ── zeros as floor ────────────────────────────────────────────────────────

    @Test
    void floorOfZerosWithWalls() {
        // [2, 0, 0, 0, 2]: 6 units of water fill the basin
        assertEquals(6, solve(2, 0, 0, 0, 2));
    }

    // ── longer / richer inputs ───────────────────────────────────────────────

    @Test
    void stairStepUp() {
        assertEquals(0, solve(0, 1, 2, 3, 4));
    }

    @Test
    void complexTerrain() {
        // [0,5,0,0,0,1,0,1,0,5,0]: large walls on sides, mixed fill
        // Left wall=5 at index 1, right wall=5 at index 9
        // fill from 2 to 8: min(5,5)=5 for most, limited by inner bars
        // i=2: min(5,5)-0=5, i=3: min(5,5)-0=5, i=4: min(5,5)-0=5
        // i=5: min(5,5)-1=4, i=6: min(5,5)-0=5, i=7: min(5,5)-1=4, i=8: min(5,5)-0=5
        // total = 5+5+5+4+5+4+5 = 33
        assertEquals(33, solve(0, 5, 0, 0, 0, 1, 0, 1, 0, 5, 0));
    }

    // ── all zeros ────────────────────────────────────────────────────────────

    @Test
    void allZeroHeights() {
        assertEquals(0, solve(0, 0, 0, 0, 0));
    }
}
