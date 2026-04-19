package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class JumpGameTest {
    private final JumpGame sol = new JumpGame();

    @Test
    void example1() {
        assertTrue(sol.canJump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void example2() {
        assertFalse(sol.canJump(new int[]{3, 2, 1, 0, 4}));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.canJump(new int[]{0}));
    }
}
