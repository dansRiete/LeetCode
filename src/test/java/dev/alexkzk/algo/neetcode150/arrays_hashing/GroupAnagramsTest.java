package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class GroupAnagramsTest {
    private final GroupAnagrams sol = new GroupAnagrams();

    @Test
    void example1() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
        assertEquals(3, result.size());
    }

    @Test
    void example2() {
        List<List<String>> result = sol.groupAnagrams(new String[]{""});
        assertEquals(1, result.size());
    }

    @Test
    void edgeCase() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"a"});
        assertEquals(1, result.size());
    }
}
