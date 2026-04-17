package dev.alexkzk.algo.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinStackTest {

    // ── leetcode example ─────────────────────────────────────────────────────

    @Test
    void leetcodeExample() {
        MinStack stack = new MinStack();
        stack.push(-2);
        stack.push(0);
        stack.push(-3);
        assertEquals(-3, stack.getMin());
        stack.pop();
        assertEquals(0, stack.top());
        assertEquals(-2, stack.getMin());
    }

    // ── single element ───────────────────────────────────────────────────────

    @Test
    void singleElement() {
        MinStack stack = new MinStack();
        stack.push(5);
        assertEquals(5, stack.top());
        assertEquals(5, stack.getMin());
    }

    // ── min updates on push ──────────────────────────────────────────────────

    @Test
    void minDecreasesOnPush() {
        MinStack stack = new MinStack();
        stack.push(3);
        assertEquals(3, stack.getMin());
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.push(2);
        assertEquals(1, stack.getMin());
    }

    // ── min restores after pop ────────────────────────────────────────────────

    @Test
    void minRestoresAfterPop() {
        MinStack stack = new MinStack();
        stack.push(5);
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.pop();
        assertEquals(5, stack.getMin()); // min restores to 5
    }

    @Test
    void minRestoresMultiplePops() {
        MinStack stack = new MinStack();
        stack.push(10);
        stack.push(5);
        stack.push(1);
        stack.pop();
        assertEquals(5, stack.getMin());
        stack.pop();
        assertEquals(10, stack.getMin());
    }

    // ── duplicate minimums ────────────────────────────────────────────────────

    @Test
    void duplicateMinValues() {
        MinStack stack = new MinStack();
        stack.push(1);
        stack.push(1);
        assertEquals(1, stack.getMin());
        stack.pop();
        assertEquals(1, stack.getMin()); // still 1 after popping one
    }

    // ── all same values ───────────────────────────────────────────────────────

    @Test
    void allSameValues() {
        MinStack stack = new MinStack();
        stack.push(7);
        stack.push(7);
        stack.push(7);
        assertEquals(7, stack.getMin());
        stack.pop();
        assertEquals(7, stack.getMin());
    }

    // ── negative values ───────────────────────────────────────────────────────

    @Test
    void allNegative() {
        MinStack stack = new MinStack();
        stack.push(-1);
        stack.push(-5);
        stack.push(-3);
        assertEquals(-5, stack.getMin());
        stack.pop();
        assertEquals(-5, stack.getMin());
        stack.pop();
        assertEquals(-1, stack.getMin());
    }
}
