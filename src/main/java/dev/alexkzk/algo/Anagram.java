package dev.alexkzk.algo;

import java.util.*;
import java.util.stream.Collectors;

public class Anagram {

    public static void main(String[] args) {
        System.out.println(groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"}));
        System.out.println(referenceGroupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"}));
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<Map<Character, Long>, List<String>> anagrams = new HashMap<>();
        for(int i = 0; i < strs.length; i++) {
            String currentWord = strs[i];
            Map<Character, Long> currentWordCharSet = countChars(currentWord);
            List<String> currentAnagrams = anagrams.get(currentWordCharSet);
            if (currentAnagrams == null) {
                anagrams.put(currentWordCharSet, new ArrayList(List.of(currentWord)));
            } else {
                currentAnagrams.add(currentWord);
            }
        }
        return new ArrayList<>(anagrams.values());
    }

    public static List<List<String>> referenceGroupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            // 1. Превращаем строку в массив и сортируем (O(K log K))
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = String.valueOf(chars); // "eat" -> "aet"

            // 2. Группируем (используем современный метод Java 8+)
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }

    public static Map<Character, Long> countChars(String input) {
        if (input == null) {
            return Map.of();
        }

        return input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        c -> c,
                        Collectors.counting()
                ));
    }
}
