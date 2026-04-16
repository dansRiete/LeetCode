package dev.alexkzk.algo.easy;

public class ValidPalindrome {

    /**
     * LEETCODE #125 — VALID PALINDROME  [Easy] | Expected: ~10 min
     * ─────────────────
     * A phrase is a palindrome if, after converting all uppercase letters to
     * lowercase and removing all non-alphanumeric characters, it reads the
     * same forward and backward. Alphanumeric characters include letters and digits.
     *
     * <p>Given a string {@code s}, return {@code true} if it is a palindrome,
     * or {@code false} otherwise.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   "A man, a plan, a canal: Panama"  → true   ("amanaplanacanalpanama")
     *   "race a car"                      → false  ("raceacar")
     *   " "                               → true   (empty after filtering)
     *   "0P"                              → false
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= s.length() <= 2 * 10^5}</li>
     *   <li>{@code s} consists only of printable ASCII characters.</li>
     * </ul>
     *
     * @param s input string
     * @return {@code true} if {@code s} is a valid palindrome, {@code false} otherwise
     */
    public static boolean isPalindrome(String s) {
        if (s.length() <= 1) {
            return true;
        }
        s = s.toLowerCase();
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i)) || Character.isDigit(s.charAt(i)) || Character.isSpaceChar(s.charAt(i))) {
                s = new StringBuilder(s).deleteCharAt(i).toString();
                i--;
            }
        }
        int start = 0;
        int end = s.length() - 1;
        while(start >= 0 && end < s.length() && start <= end && s.charAt(start) == s.charAt(end)) {
            end--;
            start++;
        }
        System.out.println(s + ", start: " + start + ", end: " + end);
        if(start > end) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
//        System.out.println("expected false, was: " + isPalindrome("ABC4C BA21s"));
        System.out.println("expected true, was: " + isPalindrome("A man, a plan, a canal: Panama"));
    }
}
