package dev.alexkzk.algo.neetcode150.sliding_window;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstringNoRepeat {
    /** LC #3 — Longest Substring Without Repeating Characters [Medium] */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> charSet = new HashSet<>();
        int max = 0;
        for(int l = 0, r = 0; r < s.length();) {
            if(!charSet.contains(s.charAt(r))) {
                charSet.add(s.charAt(r));
                max = Math.max(max, charSet.size());
                r++;
            } else {
                charSet.remove(s.charAt(l));
                l++;
            }
        }
        return max;
    }
}
