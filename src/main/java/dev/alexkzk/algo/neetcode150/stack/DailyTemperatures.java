package dev.alexkzk.algo.neetcode150.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class DailyTemperatures {
    /** LC #739 — Daily Temperatures [Medium] */
    public int[] dailyTemperatures(int[] temperatures) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] result = new int[temperatures.length];
        for(int i = temperatures.length -1; i >= 0; i--) {
            while(!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
                stack.pop();
            }
            if(stack.isEmpty()) {
                result[i] = 0;
            } else {
                result[i] = stack.peek() - i;
            }
            stack.push(i);
        }
        return result;
    }
}
