package dev.alexkzk.algo.codility;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BoundedSlicesTest {
    private final BoundedSlices sol = new BoundedSlices();

    @Test
    void example() {
        assertEquals(9, sol.solution(2, new int[]{3, 5, 7, 6, 3}));
    }

    @Test
    void allSame() {
        assertEquals(6, sol.solution(2, new int[]{1, 1, 1}));
    }

    @Test
    void allSameFour() {
        assertEquals(10, sol.solution(2, new int[]{1, 1, 1, 1}));
    }

    @Test
    void twoSame() {
        assertEquals(3, sol.solution(2, new int[]{1, 1}));
    }

    @Test
    void noAdjacentBounded() {
        assertEquals(5, sol.solution(2, new int[]{3, 6, 9, 12, 15}));
    }

    @Test
    void negativeValues() {
        assertEquals(5, sol.solution(2, new int[]{-101, 111, 201, 201}));
    }

    @Test
    void singleElement() {
        assertEquals(1, sol.solution(0, new int[]{42}));
    }

    @Test
    void kZeroWithDuplicates() {
        // only (0,0),(1,1),(2,2),(0,1) are bounded — (1,2) has max-min=1 > 0
        assertEquals(4, sol.solution(0, new int[]{1, 1, 2}));
    }

    @Test
    void allBounded() {
        // K=5 covers entire range, all n*(n+1)/2 = 6 slices valid
        assertEquals(6, sol.solution(5, new int[]{1, 2, 3}));
    }

    @Test
    void negativeSpanningZero() {
        // all pairs: max-min <= 2, all 6 slices bounded
        assertEquals(6, sol.solution(2, new int[]{-1, 0, 1}));
    }

    /*@Test
    void capAtOneBillion() {
        // 100,000 identical elements → 100000*100001/2 > 1,000,000,000
        int[] A = new int[100_000];
        assertEquals(1_000_000_000, sol.solution(0, A));
    }*/
}
