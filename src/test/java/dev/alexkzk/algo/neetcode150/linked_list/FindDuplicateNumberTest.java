package dev.alexkzk.algo.neetcode150.linked_list;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class FindDuplicateNumberTest {
    private final FindDuplicateNumber sol = new FindDuplicateNumber();

    @Test
    void example1() {
        assertEquals(2, sol.findDuplicate(new int[]{1, 3, 4, 2, 2}));
    }

    @Test
    void example2() {
        assertEquals(3, sol.findDuplicate(new int[]{3, 1, 3, 4, 2}));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.findDuplicate(new int[]{1, 1}));
    }
}
