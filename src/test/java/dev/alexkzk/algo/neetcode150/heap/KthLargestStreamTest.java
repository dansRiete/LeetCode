package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class KthLargestStreamTest {
    @Test
    void example1() {
        KthLargestStream kth = new KthLargestStream(3, new int[]{4, 5, 8, 2});
        assertEquals(4, kth.add(3));
        assertEquals(5, kth.add(5));
        assertEquals(5, kth.add(10));
        assertEquals(8, kth.add(9));
        assertEquals(8, kth.add(4));
    }

    @Test
    void example2() {
        KthLargestStream kth = new KthLargestStream(1, new int[]{});
        assertEquals(-3, kth.add(-3));
        assertEquals(-2, kth.add(-2));
    }

    @Test
    void edgeCase() {
        KthLargestStream kth = new KthLargestStream(2, new int[]{1});
        assertEquals(1, kth.add(2));
    }
}
