package dev.alexkzk.algo.medium;

import java.util.ArrayList;
import java.util.List;

public class RemoveCharString {

    public static void main(String[] args) {
        String str1 = "abcccdde";
        String str2 = "abccdde";
        System.out.println(findDeletionIndices(str1, str2));
    }

    /**
     * Mine version, should be O(n)
     */
    public static List<Integer> findDeletionIndices2(String str1, String str2) {
        if (str1.equals(str2) || str2.length() > str1.length() || Math.abs(str1.length() - str2.length()) > 1) {
            throw new IllegalArgumentException();
        }
        char[] str1Arr = str1.toCharArray();
        char[] str2Arr = str2.toCharArray();
        int charToDeletePosition = -1;
        List<Integer> charToDeleteIndices = new ArrayList<>();
        Character charToDelete = null;
        for(int i = 0; i < str1Arr.length; i++) {
            if (i > str2Arr.length - 1 || str1Arr[i] != str2Arr[i]) {
                charToDeletePosition = i;
                charToDeleteIndices.add(charToDeletePosition);
                charToDelete = str1Arr[i];
                break;
            }
        }

        if (charToDeletePosition != -1) {
            for(int i = charToDeletePosition + 1; i < str1Arr.length; i++) {
                if (str1Arr[i] == charToDelete) {
                   charToDeleteIndices.add(i);
                } else {
                    break;
                }
            }
            for(int i = charToDeletePosition - 1; i >= 0; i--) {
                if (str1Arr[i] == charToDelete) {
                    charToDeleteIndices.add(i);
                } else {
                    break;
                }
            }
        }
        for(int i = 0; i < charToDeleteIndices.size(); i++) {
            if(!new StringBuilder(str1).deleteCharAt(charToDeleteIndices.get(i)).toString().equals(str2)) {
                charToDeleteIndices.remove(i);
            }
        }

        return charToDeleteIndices;
    }

    /**
     * Claude O(n) version
     */
    public static List<Integer> findDeletionIndices(String str1, String str2) {
        if (str1.length() - str2.length() != 1) {
            throw new IllegalArgumentException();
        }
        // single pass: two pointers, skip one char in str1 on first mismatch
        int i = 0, j = 0, skipped = -1;
        while (i < str1.length() && j < str2.length()) {
            if (str1.charAt(i) == str2.charAt(j)) {
                i++; j++;
            } else if (skipped == -1) {
                skipped = i++;
            } else {
                return new ArrayList<>();
            }
        }
        if (skipped == -1) skipped = i; // divergence was at the last char

        // collect skipped index and any adjacent duplicates
        List<Integer> result = new ArrayList<>();
        result.add(skipped);
        char c = str1.charAt(skipped);
        int lo = skipped - 1, hi = skipped + 1;
        while (lo >= 0 && str1.charAt(lo) == c) result.add(lo--);
        while (hi < str1.length() && str1.charAt(hi) == c) result.add(hi++);
        return result;
    }

    /**
     * Claude example O(n2)
     */
    public static List<Integer> findDeletionIndices3(String str1, String str2) { // Claude n2
        if (str1.length() - str2.length() != 1) {
            throw new IllegalArgumentException();
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < str1.length(); i++) {
            if ((str1.substring(0, i) + str1.substring(i + 1)).equals(str2)) {
                result.add(i);
            }
        }
        return result;
    }
}
