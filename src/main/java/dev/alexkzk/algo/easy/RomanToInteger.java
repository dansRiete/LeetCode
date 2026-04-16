package dev.alexkzk.algo.easy;

import java.util.HashMap;
import java.util.Map;

public class RomanToInteger {

    /**
     * LEETCODE #13 — ROMAN TO INTEGER  [Easy] | Expected: ~15 min ────────────────────────────────────────── Roman numerals are represented by seven symbols:
     * <pre>
     *   I = 1    V = 5    X = 10   L = 50
     *   C = 100  D = 500  M = 1000
     * </pre>
     *
     * <p>Numerals are usually written largest to smallest, left to right.
     * However, there are six subtraction cases:
     * <pre>
     *   IV =   4    IX =   9
     *   XL =  40    XC =  90
     *   CD = 400    CM = 900
     * </pre>
     *
     * <p>Given a roman numeral string {@code s}, convert it to an integer.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   "III"    →    3   (1+1+1)
     *   "IV"     →    4   (5-1)
     *   "IX"     →    9   (10-1)
     *   "LVIII"  →   58   (50+5+1+1+1)
     *   "MCMXCIV"→ 1994   (1000 + (1000-100) + (100-10) + (5-1))
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= s.length() <= 15}</li>
     *   <li>{@code s} contains only the characters {@code I, V, X, L, C, D, M}</li>
     *   <li>It is guaranteed that {@code s} is a valid roman numeral in the range [1, 3999].</li>
     * </ul>
     *
     * @param s roman numeral string
     * @return integer value
     */
    public static int romanToInt(String s) {
        Map<String, Integer> intMapping = new HashMap<>();
        intMapping.put("I", 1);
        intMapping.put("V", 5);
        intMapping.put("X", 10);
        intMapping.put("L", 50);
        intMapping.put("C", 100);
        intMapping.put("D", 500);
        intMapping.put("M", 1000);
        intMapping.put("IV", 4);
        intMapping.put("IX", 9);
        intMapping.put("XL", 40);
        intMapping.put("XC", 90);
        intMapping.put("CD", 400);
        intMapping.put("CM", 900);

        int summ = 0;

        for (int i = 0; i < s.length(); i++) {
            int result = 0;
            if (i < s.length() - 1 && intMapping.get(s.substring(i, i + 2)) != null) {
                String key = s.substring(i, i + 2);
                result += intMapping.get(key);
                i++;
            } else {
                String key = s.substring(i, i + 1);
                result += intMapping.get(key);
            }
            summ += result;
        }

        return summ;
    }

    public static void main(String[] args) {
        System.out.println("expected 3, was: " + romanToInt("III"));
        System.out.println("expected 4, was: " + romanToInt("IV"));
        System.out.println("expected 9, was: " + romanToInt("IX"));
        System.out.println("expected 58, was: " + romanToInt("LVIII"));
        System.out.println("expected 1994, was: " + romanToInt("MCMXCIV"));
    }
}
