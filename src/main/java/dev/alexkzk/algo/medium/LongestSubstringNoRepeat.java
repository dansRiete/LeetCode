package dev.alexkzk.algo.medium;

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
    public static int lengthOfLongestSubstring2(String s) {
        char[] chars = s.toCharArray();
        Set<Character> recentChars = new HashSet();
        boolean duplicate = false;
        int maxLength = 0;
        int lastSubstringIndex = 0;
        for(int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            if (!recentChars.contains(currentChar)) {
                recentChars.add(currentChar);
            } else {
                duplicate = true;
                maxLength = (i - lastSubstringIndex) > maxLength ? (i - lastSubstringIndex) : maxLength;
                System.out.println(currentChar + " repeated char at index " + i + ", lastSubstringIndex " + lastSubstringIndex);
                lastSubstringIndex = i;
                recentChars = new HashSet();
                recentChars.add(currentChar);
            }
        }
        return duplicate == false ? chars.length : maxLength;
    }

    public static int lengthOfLongestSubstring(String s) {
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
