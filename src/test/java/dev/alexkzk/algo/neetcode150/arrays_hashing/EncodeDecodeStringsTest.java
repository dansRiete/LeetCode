package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
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
    void edgeCase() {
        List<String> input = List.of("", "");
        assertEquals(input, sol.decode(sol.encode(input)));
    }
}
