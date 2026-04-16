package dev.alexkzk.algo.medium;

import java.util.*;

public class GroupAnagrams {

    /**
     * LEETCODE #49 — GROUP ANAGRAMS  [Medium] | Expected: ~25 min
     * ──────────────────────────────────────────
     * Given an array of strings {@code strs}, group the anagrams together.
     * You can return the answer in any order.
     *
     * <p>Two strings are anagrams of each other if one can be formed by
     * rearranging the letters of the other (using all original letters exactly once).
     *
     * <p><b>Examples:</b>
     * <pre>
     *   ["eat","tea","tan","ate","nat","bat"]  →  [["bat"],["nat","tan"],["ate","eat","tea"]]
     *   [""]                                   →  [[""]]
     *   ["a"]                                  →  [["a"]]
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= strs.length <= 10^4}</li>
     *   <li>{@code 0 <= strs[i].length <= 100}</li>
     *   <li>{@code strs[i]} consists of lowercase English letters only.</li>
     * </ul>
     *
     * @param strs array of strings
     * @return list of anagram groups, each group in any order
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, Set<String>> anagramMap = new HashMap<>();
        for (String str : strs) {
            String key = sortString(str);
            anagramMap.computeIfAbsent(key, k -> new HashSet<>()).add(str);
        }
        return new ArrayList<>(anagramMap.values().stream().map(ArrayList::new).toList());
    }

    public static String sortString(String s) {
        if (s == null) {
            return "";
        }
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static void main(String[] args) {
//        System.out.println(sortString("dcba"));
        System.out.println(groupAnagrams(new String[]{"a"}));
    }
}
