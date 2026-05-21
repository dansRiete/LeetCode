package dev.alexkzk.algo.leetcode.medium;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {

    /**
     * LEETCODE #22 — GENERATE PARENTHESES  [Medium] | Expected: ~25 min
     * ────────────────────────────────────────────────────────────────────
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
        backTrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    private static void backTrack(List<String> result, StringBuilder curr, int open, int close, int n) {
        if (curr.length() == 2 * n) {
            result.add(curr.toString());
            return;
        }
        if(open < n) {
            curr.append("(");
            backTrack(result, curr, open + 1, close, n);
            curr.deleteCharAt(curr.length() - 1);
        }
        if(close < open) {
            curr.append(")");
            backTrack(result, curr, open, close + 1, n);
            curr.deleteCharAt(curr.length() - 1);
        }
    }

    public static void main(String[] args) {
//        System.out.println(generateParenthesis(1));
        // expected: [()]

        System.out.println(generateParenthesis(2));
        // expected: [((())), (()()), (())(), ()(()), ()()()]
    }
}
