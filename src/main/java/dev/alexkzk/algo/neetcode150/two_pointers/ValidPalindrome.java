package dev.alexkzk.algo.neetcode150.two_pointers;

public class ValidPalindrome {
    /** LC #125 — Valid Palindrome [Easy] */

    public boolean isPalindrome(String s) {
        char[] arr = s.toLowerCase().toCharArray();
        StringBuilder normalised = new StringBuilder();
        for(char c : arr) {
            if(Character.isLetter(c) || Character.isDigit(c)) {
                normalised.append(c);
            }
        }
        String normalisedString = normalised.toString();
        if(normalisedString.length() < 2) {
            return true;
        }
        //System.out.println(normalisedString);
        for(int i = 0, j = normalisedString.length()-1; j >= i; i++, j--) {
            //System.out.println(normalisedString.charAt(i) + " " + normalisedString.charAt(j));
            if(normalisedString.charAt(i) != normalisedString.charAt(j)) {
                return false;
            }
        }
        return true;
    }
}
