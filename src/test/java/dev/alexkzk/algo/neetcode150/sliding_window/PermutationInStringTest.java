package dev.alexkzk.algo.neetcode150.sliding_window;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class PermutationInStringTest {
    private final PermutationInString sol = new PermutationInString();

    @Test
    void example1() {
        assertTrue(sol.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void example2() {
        assertFalse(sol.checkInclusion("ab", "eidboaoo"));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.checkInclusion("a", "a"));
    }

    @Test
    void permutationAtStart() {
        assertTrue(sol.checkInclusion("abc", "cbaxyz"));
    }

    @Test
    void permutationAtEnd() {
        assertTrue(sol.checkInclusion("abc", "xyzacb"));
    }

    @Test
    void s1LongerThanS2() {
        assertFalse(sol.checkInclusion("abcd", "abc"));
    }

    @Test
    void s1SameLengthAsS2Match() {
        assertTrue(sol.checkInclusion("abcc", "cabc"));
    }

    @Test
    void s1SameLengthAsS2NoMatch() {
        assertFalse(sol.checkInclusion("abc", "def"));
    }

    @Test
    void duplicateCharsMatch() {
        assertTrue(sol.checkInclusion("aab", "eidbaabooo"));
    }

    @Test
    void duplicateCharsNoMatch() {
        assertFalse(sol.checkInclusion("aab", "eidbabooo"));
    }

    @Test
    void singleCharNotPresent() {
        assertFalse(sol.checkInclusion("z", "abcdefgh"));
    }

    @Test
    void allSameCharsMatch() {
        assertTrue(sol.checkInclusion("aaa", "aaabc"));
    }

    @Test
    void allSameCharsNoMatch() {
        assertFalse(sol.checkInclusion("aaaa", "aaa"));
    }
}
