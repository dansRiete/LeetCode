package dev.alexkzk.algo.neetcode150.sliding_window;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstringNoRepeat {
    /** LC #3 — Longest Substring Without Repeating Characters [Medium] */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int max = 0;
        for (int l = 0, r = 0; r < s.length(); ) {
            if (!set.contains(s.charAt(r))) {
                set.add(s.charAt(r));
                max = Math.max(max, r - l + 1);
                r++;
            } else {
                set.remove(s.charAt(l));
                l++;
            }
        }
        return max;
    }
}
