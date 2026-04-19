package dev.alexkzk.algo.neetcode150.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MinStackTest {
    @Test
    void example1() {
        MinStack stack = new MinStack();
        stack.push(-2);
        stack.push(0);
        stack.push(-3);
        assertEquals(-3, stack.getMin());
        stack.pop();
        assertEquals(0, stack.top());
        assertEquals(-2, stack.getMin());
    }

    @Test
    void example2() {
        MinStack stack = new MinStack();
        stack.push(5);
        assertEquals(5, stack.top());
        assertEquals(5, stack.getMin());
    }

    @Test
    void edgeCase() {
        MinStack stack = new MinStack();
        stack.push(1);
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.pop();
        assertEquals(1, stack.getMin());
    }
}
