package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.ArrayList;
import java.util.List;

public class EncodeDecodeStrings {
    /** LC #271 — Encode and Decode Strings [Medium] */
    public String encode(List<String> strs) {
        StringBuilder result = new StringBuilder();
        for(String s : strs) {
            result.append(s.length());
            result.append("#");
            result.append(s);
        }
        return result.toString();
    }

    /** LC #271 — Encode and Decode Strings [Medium] */
    public List<String> decode(String str) {
        List<String> strings = new ArrayList<>();
        StringBuilder lenBuff = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != '#') {
                lenBuff.append(c);
            }
            if (c == '#') {
                i++;
                int n = Integer.parseInt(lenBuff.toString());
                strings.add(str.substring(i, i + n));
                i += n -1;
                lenBuff.setLength(0);
            }
        }
        return strings;

    }
}
