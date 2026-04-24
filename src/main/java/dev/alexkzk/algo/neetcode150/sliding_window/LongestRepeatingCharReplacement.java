package dev.alexkzk.algo.neetcode150.sliding_window;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LongestRepeatingCharReplacement {
    /** LC #424 — Longest Repeating Character Replacement [Medium] */
    public int characterReplacement(String s, int k) {
        int[] charArr = new int[26];
        //int max = 0;
        int maxL = 0;
        for(int l = 0, r = 0; r < s.length();) {
            charArr[s.charAt(r) - 'A']++;
            //max = Math.max(max, charArr[s.charAt(r) - 'A']);
            if(r - l + 1 - getMax(charArr) > k) {
                charArr[s.charAt(l) - 'A']--;
                l++;
                r++;
                continue;
            } else {
                r++;
            }
            maxL = Math.max(maxL, r - l);
            System.out.println(String.format("l: %d, r: %d, max: %d, arr: %s", l, r, getMax(charArr), Arrays.toString(charArr)));
        }
        return maxL;
    }

    public int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for(int i : arr) {
            if(i > max) {
                max = i;
            }
        }
        return max;
    }
}
