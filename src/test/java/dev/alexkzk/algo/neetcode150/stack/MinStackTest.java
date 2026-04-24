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
    void duplicateValues() {
        MinStack stack = new MinStack();
        stack.push(1);
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.pop();
        assertEquals(1, stack.getMin());
    }

    @Test
    void minRestoresAfterPoppingCurrentMin() {
        // popping the current min must reveal the previous min
        MinStack stack = new MinStack();
        stack.push(3);
        stack.push(1);
        stack.push(2);
        assertEquals(1, stack.getMin());
        stack.pop(); // remove 2
        assertEquals(1, stack.getMin());
        stack.pop(); // remove 1 (the min)
        assertEquals(3, stack.getMin());
    }

    @Test
    void pushingLargerValuesDoesNotChangeMin() {
        MinStack stack = new MinStack();
        stack.push(2);
        stack.push(5);
        stack.push(10);
        assertEquals(2, stack.getMin());
    }

    @Test
    void negativeValues() {
        MinStack stack = new MinStack();
        stack.push(-1);
        stack.push(-5);
        stack.push(-3);
        assertEquals(-5, stack.getMin());
        stack.pop();
        assertEquals(-5, stack.getMin());
        stack.pop(); // remove -5
        assertEquals(-1, stack.getMin());
    }

    @Test
    void singleElement() {
        MinStack stack = new MinStack();
        stack.push(42);
        assertEquals(42, stack.top());
        assertEquals(42, stack.getMin());
    }

    @Test
    void minUpdatesOnPushThenRestoresOnPop() {
        // push decreasing values, verify min tracks correctly on both push and pop
        MinStack stack = new MinStack();
        stack.push(5);
        assertEquals(5, stack.getMin());
        stack.push(3);
        assertEquals(3, stack.getMin());
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.pop();
        assertEquals(3, stack.getMin());
        stack.pop();
        assertEquals(5, stack.getMin());
    }
}
