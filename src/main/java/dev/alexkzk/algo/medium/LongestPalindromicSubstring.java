package dev.alexkzk.algo.medium;

public class LongestPalindromicSubstring {

    /**
     * LEETCODE #5 — LONGEST PALINDROMIC SUBSTRING  [Medium] | Expected: ~30 min
     * ──────────────────────────────
     * Given a string {@code s}, return the longest substring of {@code s}
     * that is a palindrome. A palindrome reads the same forwards and backwards.
     *
     * <p>If there are multiple answers of the same maximum length, return any one of them.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   "babad"   → "bab"  (or "aba" — both are valid)
     *   "cbbd"    → "bb"
     *   "a"       → "a"
     *   "racecar" → "racecar"
     *   "abcba"   → "abcba"
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= s.length() <= 1000}</li>
     *   <li>{@code s} consists of only digits and English letters.</li>
     * </ul>
     *
     * @param s input string
     * @return the longest palindromic substring
     */
    public static String longestPalindrome(String s) {  //  even string length doesn't work
        if (s.length() <= 1) {
            return s;
        }
        String longestPalyndrome = null;
        for (int i = 0; i < s.length(); i++) {
            String currentlPalyndrome = longestPalyndromeAtAPosition(i, s);
            if (longestPalyndrome == null || currentlPalyndrome.length() > longestPalyndrome.length()) {
                longestPalyndrome = currentlPalyndrome;
            }
        }
        return longestPalyndrome;
    }

    public static String longestPalyndromeAtAPosition(int pos, String s) {
        int start = pos - 1;
        int end = pos + 1;
        while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
            start--;
            end++;
        }
        return s.substring(start + 1, end);
    }


    public static void main(String[] args) {
//        System.out.println(longestPalyndromeAtAPosition(3, "racecar"));


        System.out.println("expected: bab, actual: " + longestPalindrome("babad"));
        System.out.println("expected: racecar, actual: " + longestPalindrome("racecar"));
    }
}
