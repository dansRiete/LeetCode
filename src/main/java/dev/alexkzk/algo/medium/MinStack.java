package dev.alexkzk.algo.medium;

import java.util.LinkedList;

/**
 * LeetCode 155 — Min Stack
 *
 * <p><b>Problem:</b> Design a stack that supports push, pop, top, and retrieving
 * the minimum element — all in O(1) time.
 *
 * <p><b>Operations:</b>
 * <pre>
 *   MinStack stack = new MinStack();
 *   stack.push(-2);
 *   stack.push(0);
 *   stack.push(-3);
 *   stack.getMin(); → -3
 *   stack.pop();
 *   stack.top();    → 0
 *   stack.getMin(); → -2
 * </pre>
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>{@code -2^31 <= val <= 2^31 - 1}</li>
 *   <li>pop, top, getMin called only on non-empty stack</li>
 *   <li>At most 3 * 10^4 calls total</li>
 * </ul>
 */
public class MinStack {

    private LinkedList<Integer> list = new LinkedList<>(); // -2, 0, -3
    private LinkedList<Integer> min = new LinkedList<>(); // -2, -2, -3

    // TODO: implement

    public void push(int val) {
        list.add(val);
        min.add(min.isEmpty() ? val : Math.min(val, min.peekLast()));
    }

    public void pop() {
        if(!list.isEmpty()) {
            list.removeLast();
            min.removeLast();
        }
    }

    public int top() {
        if(!list.isEmpty()) {
            return list.get(list.size() -1);
        } else {
            return -1;
        }

    }

    public int getMin() {
        return min.peekLast();
    }

    public static void main(String[] args) {
        MinStack stack = new MinStack();
        stack.push(-2);
        stack.push(0);
        stack.push(-3);
        System.out.println(stack.getMin()); // expected: -3
        stack.pop();
        System.out.println(stack.top());    // expected: 0
        System.out.println(stack.getMin()); // expected: -2
    }
}
