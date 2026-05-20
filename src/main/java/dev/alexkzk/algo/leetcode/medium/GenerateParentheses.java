package dev.alexkzk.algo.leetcode.medium;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {

    /**
     * Returns all combinations of well-formed parentheses for n pairs.
     *
     * <p><b>Problem:</b> Given {@code n} pairs of parentheses, generate all combinations of
     * well-formed (valid) parentheses strings.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   n = 1  →  ["()"]
     *   n = 3  →  ["((()))","(()())","(())()","()(())","()()()"]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= n <= 8}</li>
     * </ul>
     *
     * @param n number of parenthesis pairs
     * @return list of all valid parentheses combinations
     */
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    private static void backtrack(List<String> result, StringBuilder current, int open, int close, int n) {
        if (current.length() == 2 * n) {
            result.add(current.toString());
            return;
        }
        if (open < n) {
            current.append('(');
            backtrack(result, current, open + 1, close, n);
            current.deleteCharAt(current.length() - 1);
        }
        if (close < open) {
            current.append(')');
            backtrack(result, current, open, close + 1, n);
            current.deleteCharAt(current.length() - 1);
        }
    }

    public static void main(String[] args) {
//        System.out.println(generateParenthesis(1));
        // expected: [()]

        System.out.println(generateParenthesis(2));
        // expected: [((())), (()()), (())(), ()(()), ()()()]
    }
}
