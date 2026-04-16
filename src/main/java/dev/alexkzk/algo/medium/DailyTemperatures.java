package dev.alexkzk.algo.medium;

import java.util.Arrays;
import java.util.Stack;

public class DailyTemperatures {

    /**
     * For each day, returns how many days until a warmer temperature.
     *
     * <p><b>Problem:</b> Given an array {@code temperatures} of daily temperatures, return an array
     * {@code answer} where {@code answer[i]} is the number of days you have to wait after day {@code i}
     * to get a warmer temperature. If no such future day exists, {@code answer[i] == 0}.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   temperatures = [73, 74, 75, 71, 69, 72, 76, 73]  →  [1, 1, 4, 2, 1, 1, 0, 0]
     *   temperatures = [30, 40, 50, 60]                   →  [1, 1, 1, 0]
     *   temperatures = [30, 60, 90]                        →  [1, 1, 0]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= temperatures.length <= 10^5}</li>
     *   <li>{@code 30 <= temperatures[i] <= 100}</li>
     * </ul>
     *
     *  ---
     *   Hint 1 — Reframe the problem
     *
     *   Instead of asking "for day i, find the next warmer day" (looking forward), ask "when I see a warm day, which previous days does it resolve?" (looking backward). This flips the loop direction in a useful way.
     *
     *   ---
     *   Hint 2 — What to store
     *
     *   You don't need to remember temperatures of unresolved days — you need their indices. The temperature is still accessible via temperatures[index].
     *
     *   ---
     *   Hint 3 — The data structure
     *
     *   Use a Stack<Integer> of indices. The invariant to maintain: temperatures at indices in the stack are always in decreasing order (monotonic decreasing stack). When you encounter temperatures[i], it may "resolve" several stacked days at once.
     *
     *   ---
     *   Hint 4 — The loop logic
     *
     *   for each index i:
     *       while stack is not empty
     *         AND temperatures[i] > temperatures[stack.peek()]:
     *           j = stack.pop()
     *           answer[j] = i - j      // i is the first warmer day after j
     *       push i onto the stack
     *
     *   // anything left in the stack has no warmer future day → answer stays 0
     *
     *   ---
     *   Hint 5 — Complexity
     *
     *   Each index is pushed once and popped at most once → O(n) time, O(n) space.
     *
     * @param temperatures array of daily temperatures
     * @return array where each element is the number of days to wait for a warmer day
     */
    public static int[] dailyTemperatures(int[] temperatures) { //  todo imrpove with O(n) use hints
        int[] daysToWarmer = new int[temperatures.length];
        for(int i = 0; i < temperatures.length; i++) {
            for(int j = i + 1; j < temperatures.length; j++) {
                if(temperatures[j] > temperatures[i]) {
                    daysToWarmer[i] = j - i;
                    break;
                }
            }
        }
        return daysToWarmer;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})));
        // expected: [1, 1, 4, 2, 1, 1, 0, 0]

        System.out.println(Arrays.toString(dailyTemperatures(new int[]{30, 40, 50, 60})));
        // expected: [1, 1, 1, 0]

        System.out.println(Arrays.toString(dailyTemperatures(new int[]{30, 60, 90})));
        // expected: [1, 1, 0]
    }
}
