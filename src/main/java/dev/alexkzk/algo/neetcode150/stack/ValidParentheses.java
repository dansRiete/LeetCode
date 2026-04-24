package dev.alexkzk.algo.neetcode150.stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class ValidParentheses {
    public boolean isValidReference(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> map = Map.of(')', '(', '}', '{', ']', '[');
        for (char c : s.toCharArray()) {
            if (!map.containsKey(c)) {
                stack.push(c);
            } else if (stack.isEmpty() || !stack.peek().equals(map.get(c))) {
                return false;
            } else {
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    /** LC #20 — Valid Parentheses [Easy] */
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> parentMap = Map.of(')', '(', '}', '{', ']', '[');
        for(char c : s.toCharArray()) {
            if(stack.isEmpty()) {
                stack.push(c);
            } else {
                if(parentMap.get(c) != null && stack.peek() == parentMap.get(c)){
                    stack.pop();
                } else {
                    stack.push(c);
                }
            }
        }
        return stack.isEmpty();
    }
}
