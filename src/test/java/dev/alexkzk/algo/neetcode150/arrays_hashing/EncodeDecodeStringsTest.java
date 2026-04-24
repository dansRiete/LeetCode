package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EncodeDecodeStringsTest {
    private final EncodeDecodeStrings sol = new EncodeDecodeStrings();

    @Test
    void example1() {
        List<String> input = List.of("lint", "code", "love", "you");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void example2() {
        List<String> input = List.of("we", "say", ":", "yes");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void emptyStrings() {
        List<String> input = List.of("", "");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void emptyList() {
        List<String> input = List.of();
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void singleString() {
        List<String> input = List.of("hello");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void stringsWithSpacesAndNewlines() {
        List<String> input = List.of("hello world", "foo\nbar", "tab\there");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void stringsWithNumbers() {
        List<String> input = List.of("3#abc", "10#hello", "0#");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    @Test
    void decodeWithSeparateInstance() {
        // The encoded string must be self-sufficient — a fresh decoder instance
        // with no shared state should be able to decode it.
        EncodeDecodeStrings encoder = new EncodeDecodeStrings();
        String encoded = encoder.encode(List.of("hello", "world"));
        EncodeDecodeStrings decoder = new EncodeDecodeStrings();
        assertEquals(List.of("hello", "world"), decoder.decode(encoded));
    }
}
