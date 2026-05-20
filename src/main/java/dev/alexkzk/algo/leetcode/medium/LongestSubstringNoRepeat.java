package dev.alexkzk.algo.leetcode.medium;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LongestSubstringNoRepeat {

    /**
     * LEETCODE #3 — LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS  [Medium] | Expected: ~20 min
     * ────────────────────────────────────────────────
     * Given a string {@code s}, find the length of the longest substring
     * that contains no duplicate characters.
     *
     * <p>A <em>substring</em> is a contiguous sequence of characters within the string.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   "abcabcbb" → 3   ("abc")
     *   "bbbbb"    → 1   ("b")
     *   "pwwkew"   → 3   ("wke")
     *   ""         → 0
     *   "au"       → 2   ("au")
     *   "dvdf"     → 3   ("vdf")
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 0 <= s.length() <= 5 * 10^4}</li>
     *   <li>{@code s} consists of English letters, digits, symbols, and spaces.</li>
     * </ul>
     *
     * @param s input string
     * @return length of the longest substring without repeating characters
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        Set<Character> set = new HashSet<>();
        set.add(s.charAt(0));
        int max = 1;
        for (int l = 0, r = 1; l < s.length() && r < s.length(); ) {
            char rChar = s.charAt(r);
            while (set.contains(rChar) && l != r) {
                set.remove(s.charAt(l));
                l++;
            }
            set.add(rChar);
            max = Math.max(max, r - l + 1);
            r++;
        }
        return max;
    }

    public static int lengthOfLongestSubstringShortened(String s) {
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


    public static int lengthOfLongestSubstringReference(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int max = 0, left = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (lastSeen.containsKey(c)) {
                left = Math.max(left, lastSeen.get(c) + 1); // never move left backwards
            }
            lastSeen.put(c, i);
            max = Math.max(max, i - left + 1);
        }
        return max;
    }

    public static void main(String[] args) {
//        System.out.println(lengthOfLongestSubstring("abcabcbb")); //3
//        System.out.println(lengthOfLongestSubstring("bbbbb"));  //1
//        System.out.println(lengthOfLongestSubstring("pwwkew")); //3
//        System.out.println(lengthOfLongestSubstring("")); //0
//        System.out.println(lengthOfLongestSubstring("au")); //2
        System.out.println(lengthOfLongestSubstring("dvdf")); //3
    }
}
