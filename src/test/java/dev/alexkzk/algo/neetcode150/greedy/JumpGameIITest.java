package dev.alexkzk.algo.neetcode150.greedy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class JumpGameIITest {
    private final JumpGameII sol = new JumpGameII();

    @Test
    void example1() {
        assertEquals(2, sol.jump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void example2() {
        assertEquals(2, sol.jump(new int[]{2, 3, 0, 1, 4}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.jump(new int[]{0}));
    }
}
