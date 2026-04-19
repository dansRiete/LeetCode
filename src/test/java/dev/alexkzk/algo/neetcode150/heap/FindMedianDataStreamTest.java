package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class FindMedianDataStreamTest {
    @Test
    void example1() {
        FindMedianDataStream mf = new FindMedianDataStream();
        mf.addNum(1);
        mf.addNum(2);
        assertEquals(1.5, mf.findMedian());
        mf.addNum(3);
        assertEquals(2.0, mf.findMedian());
    }

    @Test
    void example2() {
        FindMedianDataStream mf = new FindMedianDataStream();
        mf.addNum(6);
        assertEquals(6.0, mf.findMedian());
    }

    @Test
    void edgeCase() {
        FindMedianDataStream mf = new FindMedianDataStream();
        mf.addNum(1);
        mf.addNum(1);
        assertEquals(1.0, mf.findMedian());
    }
}
