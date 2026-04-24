package dev.alexkzk.algo.neetcode150.stack;

import java.util.LinkedList;
import java.util.List;

public class MinStack {
    /** LC #155 — Min Stack [Medium] */
    List<Integer> stack = new LinkedList<>();
    List<Integer> min = new LinkedList<>();

    public MinStack() {

    }

    public void push(int val) {
        if(min.size() == 0 || val < min.get(min.size() - 1)){
            min.add(val);
        } else{
            min.add(min.get(min.size() - 1));
        }
        stack.add(val);

    }

    public void pop() {
        min.remove(min.size() - 1);
        stack.remove(stack.size() - 1);
    }

    public int top() {
        return stack.get(stack.size() - 1);

    }

    public int getMin() {
        if(min.size()==0) return 0;
        return min.get(min.size() - 1);
    }
}
