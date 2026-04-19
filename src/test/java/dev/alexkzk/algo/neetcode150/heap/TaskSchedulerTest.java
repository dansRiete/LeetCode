package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TaskSchedulerTest {
    private final TaskScheduler sol = new TaskScheduler();

    @Test
    void example1() {
        assertEquals(8, sol.leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
    }

    @Test
    void example2() {
        assertEquals(6, sol.leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0));
    }

    @Test
    void edgeCase() {
        assertEquals(16, sol.leastInterval(new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2));
    }
}
