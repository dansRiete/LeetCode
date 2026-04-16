package dev.alexkzk.algo.medium;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PalindromeByRemoval {

    /**
     * Given a string s, return a list of all indices where removing the character
     * at that index produces a palindrome.
     *
     * A palindrome reads the same forwards and backwards (e.g. "abba", "racecar").
     * An empty string is considered a palindrome.
     *
     * Multiple indices may be valid when the same character appears consecutively
     * (e.g. "abba" → removing index 1 or 2 both produce "aba").
     * If the string is already a palindrome, still check every index — return all
     * positions where removing that character keeps the result a palindrome.
     * Return an empty list if no single removal can produce a palindrome.
     *
     * Examples:
     *   "abcba"   → [2]         removing 'c' at index 2 → "abba"
     *   "abba"    → [1, 2]      removing either 'b' → "aba"
     *   "aaaa"    → [0,1,2,3]   any removal gives "aaa"
     *   "abcd"    → []          no removal produces a palindrome
     *   "a"       → [0]         removing the only char gives "" (palindrome)
     *   "aab"     → [2]         removing 'b' at index 2 → "aa"
     *   "baa"     → [0]         removing 'b' at index 0 → "aa"
     *   "racecar" → [3]         removing 'e' at index 3 → "raccar"
     *   "aabaa"   → [2]         removing 'b' at index 2 → "aaaa"
     */


    /**
     * O(n2)
     */
    public static List<Integer> findRemovalIndices2(String s) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if(isPalindrome(new StringBuilder(s).deleteCharAt(i).toString())) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static boolean isPalindrome(String s) {
        char[] strChars = s.toCharArray();
        int i = 0, j = strChars.length - 1;
        boolean isPalindrome = true;
        while(i <= j) {
            if(strChars[i] != strChars[j]) {
                isPalindrome = false;
                break;
            }
            i++; j--;
        }
        return isPalindrome;
    }

    /**
     * Claude O(n) ideal solution.
     *
     * Key insight: walk two pointers inward. At the first divergence (i, j),
     * only removing index i or index j can produce a palindrome — any removal
     * strictly inside (i,j) still leaves s[i] and s[j] as the outer boundary
     * mismatch. For whichever side works, also collect adjacent duplicates of
     * that same char (they produce the same result).
     */
    public static List<Integer> findRemovalIndices3(String s) {
        if (s.length() <= 1) return new ArrayList<>(List.of(0));

        int i = 0, j = s.length() - 1;
        while (i < j && s.charAt(i) == s.charAt(j)) { i++; j--; }

        if (i >= j) {
            // already a palindrome — collect the center run of the same char
            return collectCenter(s);
        }

        List<Integer> result = new ArrayList<>();
        // try removing index i
        if (isPalindromeRange(s, i + 1, j)) {
            result.add(i);
            int lo = i - 1;
            while (lo >= 0 && s.charAt(lo) == s.charAt(i)) result.add(lo--);
        }
        // try removing index j
        if (isPalindromeRange(s, i, j - 1)) {
            result.add(j);
            int hi = j + 1;
            while (hi < s.length() && s.charAt(hi) == s.charAt(j)) result.add(hi++);
        }
        return result;
    }

    private static List<Integer> collectCenter(String s) {
        // For a palindrome, only removing a char from the center run can produce another palindrome.
        // "racecar" → center char is 'e' at index 3 → returns [3]
        // "abba"    → center chars are 'b','b' at indices 1,2 → returns [1,2]
        // "aaabbaaa"→ center chars are 'b','b' at indices 3,4 → returns [3,4]
        // "aaaa"    → center run spans entire string → returns [0,1,2,3]
        int lo = s.length() % 2 == 0 ? s.length() / 2 - 1 : s.length() / 2;
        int hi = s.length() / 2;
        char c = s.charAt(lo);
        while (lo > 0 && s.charAt(lo - 1) == c) lo--;
        while (hi < s.length() - 1 && s.charAt(hi + 1) == c) hi++;
        List<Integer> result = new ArrayList<>();
        for (int k = lo; k <= hi; k++) result.add(k);
        return result;
    }

    private static boolean isPalindromeRange(String s, int lo, int hi) {
        while (lo < hi) {
            if (s.charAt(lo++) != s.charAt(hi--)) return false;
        }
        return true;
    }

    /**
     * O(n) — user's attempt
     */
    public static List<Integer> findRemovalIndices(String s) {
        if (s.length() == 0) {
            return Collections.emptyList();
        }
        if(s.length() == 1) {
            return List.of(0);
        }
        List<Integer> indices = new ArrayList<>();
        boolean alreadyPalindrome = isPalindrome(s);
        if (alreadyPalindrome) {
            if(s.length() % 2 == 0) {
                indices.add(s.length() / 2);
                indices.add(s.length() / 2 - 1);
            } else {
                indices.add(s.length() / 2);
            }
            return indices;
        } else if (isPalindrome(s.substring(0, s.length() - 1))) {
                return List.of(s.length() - 1);
        } else if(isPalindrome(s.substring(1))) {
            return List.of(0);
        }
        return new ArrayList<>();
    }
}
