package dev.alexkzk.multithreading;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * Demonstrates why ArrayDeque outperforms Stack.
 *
 * Stack extends Vector — every push/pop acquires a mutex lock even on a
 * single thread. ArrayDeque uses a plain resizable circular buffer with no
 * locking, giving dramatically lower overhead in single-threaded use.
 *
 * Circular buffer layout (capacity = 8, head=2, tail=5):
 *
 *   index:  0    1   [2]  [3]  [4]  [5]   6    7
 *   value:  -    -    A    B    C    -     -    -
 *                    ^head            ^tail
 *
 * push  → write at tail, tail = (tail + 1) & (capacity - 1)
 * pop   → read  at tail - 1, decrement tail
 * No allocation per element; the array doubles when full.
 */
public class StackVsDeque {

    // -----------------------------------------------------------------------
    // Stack (legacy — synchronized on every call)
    // -----------------------------------------------------------------------

    public static void pushAll(Stack<Integer> stack, int[] values) {
        for (int v : values) stack.push(v);          // each call: synchronized
    }

    public static int[] popAll(Stack<Integer> stack) {
        int[] result = new int[stack.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = stack.pop();                 // each call: synchronized
        }
        return result;
    }

    // -----------------------------------------------------------------------
    // ArrayDeque (modern — zero locking overhead)
    // -----------------------------------------------------------------------

    public static void pushAll(Deque<Integer> deque, int[] values) {
        for (int v : values) deque.push(v);          // no lock, array write + index bump
    }

    public static int[] popAll(Deque<Integer> deque) {
        int[] result = new int[deque.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = deque.pop();
        }
        return result;
    }

    // -----------------------------------------------------------------------
    // Benchmark: measure nanoseconds for N push+pop cycles
    // -----------------------------------------------------------------------

    public static long benchmarkStack(int n) {
        Stack<Integer> stack = new Stack<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) stack.push(i);
        for (int i = 0; i < n; i++) stack.pop();
        return System.nanoTime() - start;
    }

    public static long benchmarkDeque(int n) {
        Deque<Integer> deque = new ArrayDeque<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) deque.push(i);
        for (int i = 0; i < n; i++) deque.pop();
        return System.nanoTime() - start;
    }

    // -----------------------------------------------------------------------
    // Practical example: balanced parentheses checker
    // Shows typical stack usage — ArrayDeque is the right choice here.
    // -----------------------------------------------------------------------

    public static boolean isBalanced(String expression) {
        Deque<Character> deque = new ArrayDeque<>();
        for (char c : expression.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> deque.push(c);
                case ')' -> { if (deque.isEmpty() || deque.pop() != '(') return false; }
                case ']' -> { if (deque.isEmpty() || deque.pop() != '[') return false; }
                case '}' -> { if (deque.isEmpty() || deque.pop() != '{') return false; }
            }
        }
        return deque.isEmpty();
    }

    // -----------------------------------------------------------------------
    // Demo main
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        int n = 1_000_000;

        // Warm up JIT so benchmark reflects steady-state performance
        benchmarkStack(10_000);
        benchmarkDeque(10_000);

        long stackNs = benchmarkStack(n);
        long dequeNs = benchmarkDeque(n);

        System.out.printf("Stack    : %,d ns for %,d push+pop cycles%n", stackNs, n);
        System.out.printf("ArrayDeque: %,d ns for %,d push+pop cycles%n", dequeNs, n);
        System.out.printf("Speedup  : %.1fx%n", (double) stackNs / dequeNs);

        System.out.println("\nBalanced parentheses:");
        System.out.println("  ({[]})  -> " + isBalanced("({[]})"));   // true
        System.out.println("  ({[})   -> " + isBalanced("({[})")); // false
        System.out.println("  ()[]{}  -> " + isBalanced("()[]{}")); // true
    }
}
