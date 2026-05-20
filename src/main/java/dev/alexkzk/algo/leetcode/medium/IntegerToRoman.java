package dev.alexkzk.algo.leetcode.medium;

public class IntegerToRoman {

    /**
     * Converts an integer to its Roman numeral representation.
     *
     * <p><b>Problem (LeetCode #12 – Medium):</b> Given an integer {@code num}, convert it to a
     * Roman numeral string.
     *
     * <p>Roman numeral symbols and their values:
     * <pre>
     *   I=1, V=5, X=10, L=50, C=100, D=500, M=1000
     * </pre>
     *
     * Subtractive notation is used in six cases:
     * <pre>
     *   IV=4, IX=9, XL=40, XC=90, CD=400, CM=900
     * </pre>
     *
     * <p><b>Examples:</b>
     * <pre>
     *   3749  →  "MMMDCCXLIX"
     *   58    →  "LVIII"
     *   1994  →  "MCMXCIV"
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 1 <= num <= 3999}</li>
     * </ul>
     *
     * @param num integer to convert (1–3999)
     * @return Roman numeral string representation of {@code num}
     */
    public static String intToRoman(int num) {
        int[] intArr = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] roman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        String result = "";
        for (int i = 0; i < intArr.length; i++) {
            if(num >= intArr[i]){
                int count = num / intArr[i];
                num -= intArr[i]*count;
                result += roman[i].repeat(count);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(intToRoman(3749));
    }
}
