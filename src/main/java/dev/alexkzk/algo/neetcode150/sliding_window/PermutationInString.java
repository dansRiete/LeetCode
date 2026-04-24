package dev.alexkzk.algo.neetcode150.sliding_window;

import dev.alexkzk.algo.medium.SortCharactersByFrequency;

import java.util.Arrays;

public class PermutationInString {
    /** LC #567 — Permutation in String [Medium] */
    public boolean checkInclusion2(String s1, String s2) {
        if(s1.equals(s2)) {
            return true;
        }
        if(s1.length() > s2.length()) {
            return false;
        }
        int[] s1Arr = stringToArray(s1);
        int[] s2Arr = stringToArray(s2.substring(0, s1.length()));
        if(arrEquals(s1Arr, s2Arr)) {
            return true;
        }
        for(int l = 1, r = l + s1.length() ; r < s2.length()+1; r++, l++) {
            s2Arr[s2.charAt(l - 1) - 'a']--;
            s2Arr[s2.charAt(r - 1) - 'a']++;
            if(arrEquals(s1Arr, s2Arr)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;
        int[] s1Arr = new int[26], s2Arr = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1Arr[s1.charAt(i) - 'a']++;
            s2Arr[s2.charAt(i) - 'a']++;
        }
        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (s1Arr[i] == s2Arr[i]) matches++;
        }
        for (int l = 0, r = s1.length(); r < s2.length(); r++, l++) {
            if (matches == 26) return true;
            int in = s2.charAt(r) - 'a';
            s2Arr[in]++;
            if (s2Arr[in] == s1Arr[in]){
                matches++;
            } else if (s2Arr[in] - 1 == s1Arr[in]){
                matches--;
            }

            int out = s2.charAt(l) - 'a';
            s2Arr[out]--;
            if (s2Arr[out] == s1Arr[out]){
                matches++;
            } else if (s2Arr[out] + 1 == s1Arr[out]){
                matches--;
            }
        }
        return matches == 26;
    }

    private boolean arrEquals(int[] a, int[] b) {
        if(a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if(a[i] != b[i]) return false;
        }
        return true;
    }

    private int[] stringToArray(String s) {
        int[] arr = new int[26];
        for(char c : s.toCharArray()) {
            arr[c - 'a']++;
        }
        return arr;
    }

    public static void main(String[] args) {
        PermutationInString permutationInString = new PermutationInString();
//        permutationInString.checkInclusionReference("abc", "lecabee");
    }
}
