package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.*;
import java.util.stream.Collectors;

public class GroupAnagrams {
    /** LC #49 — Group Anagrams [Medium] */
    public List<List<String>> groupAnagrams(String[] strs) {
        return new ArrayList<>(Arrays.stream(strs).collect(Collectors.groupingBy(this::sortedString)).values());
    }

    private Map<Character, Integer> sortedString(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for(char c : s.toCharArray()) {
            map.merge(c, 1, Integer::sum);
        }
        return map;

    }
}
