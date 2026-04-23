package dev.alexkzk.algo.neetcode150.sliding_window;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LongestRepeatingCharReplacement {
    /** LC #424 — Longest Repeating Character Replacement [Medium] */
    public int characterReplacement(String s, int k) {
        int[] freqArr = new int[26];
        int maxFreq = 0;
        int maxLength = 0;
        for(int l=0, r=0; r < s.length();) {
            int arrIndex = s.charAt(r) - 'A';
            freqArr[arrIndex]++;
            int currFreq = freqArr[arrIndex];
            if(currFreq > maxFreq) {
                maxFreq = currFreq;
            }
            if(r - l + 1 - maxFreq <= k) {
                maxLength = Math.max(maxLength, r - l + 1);
                r++;
            } else {
                freqArr[s.charAt(l) - 'A']--;
                l++;
                r++;
            }
        }
        return maxLength;
    }
}
