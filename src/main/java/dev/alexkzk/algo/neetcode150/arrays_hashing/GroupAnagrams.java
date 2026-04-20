package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.*;
import java.util.stream.Collectors;

public class GroupAnagrams {
    /** LC #49 — Group Anagrams [Medium] */
    public List<List<String>> groupAnagrams(String[] strs) {
        return new ArrayList<>(Arrays.stream(strs).collect(Collectors.groupingBy(this::getKey)).values());
    }

    private String getKey(String s) {
        int[] fingerprint = new int[26];
        for(char c : s.toCharArray()) {
            fingerprint[c - 'a']++;
        }
        return Arrays.toString(fingerprint);

    }
}
