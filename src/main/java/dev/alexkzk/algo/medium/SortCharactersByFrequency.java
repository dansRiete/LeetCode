package dev.alexkzk.algo.medium;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortCharactersByFrequency {

    /**
     * Returns the string with characters sorted by their frequency in descending order.
     *
     * <p><b>Problem (LeetCode 451 – Medium):</b> Given a string {@code s}, sort its characters
     * by frequency (most frequent first). If two characters have the same frequency, either order
     * is acceptable.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   "tree"        →  "eert"  or  "eetr"   (e×2, then t and r in any order)
     *   "cccaaa"      →  "aaaccc" or "cccaaa"  (both appear 3 times — either order valid)
     *   "Aabb"        →  "bbAa"  or  "bbAa"   (b×2, then A and a in any order)
     *   "Mississippi" →  "iiiissssppM"  (i×4, s×4, p×2, M×1)
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= s.length <= 5 * 10^5}</li>
     *   <li>{@code s} consists of uppercase and lowercase English letters and digits.</li>
     * </ul>
     *
     * @param s input string
     * @return a new string with characters sorted by descending frequency
     */
    public static String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        for(char c : chars) {
            if(map.get(c) == null) {
                map.put(c, 0);
            } else {
                map.put(c, map.get(c) + 1);
            }

        }
        StringBuilder result = new StringBuilder();
        map.keySet().stream().sorted((c1, c2) -> map.get(c2).compareTo(map.get(c1))).forEach(c -> {
            for(int i = 0; i < map.get(c) + 1; i++) {
                result.append(c);
            }
        });
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(frequencySort("Mississippi"));
    }
}
