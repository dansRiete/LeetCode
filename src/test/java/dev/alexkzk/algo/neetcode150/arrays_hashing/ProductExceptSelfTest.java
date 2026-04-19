package dev.alexkzk.algo.neetcode150.arrays_hashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ProductExceptSelfTest {
    private final ProductExceptSelf sol = new ProductExceptSelf();

    @Test
    void example1() {
        assertArrayEquals(new int[]{24, 12, 8, 6}, sol.productExceptSelf(new int[]{1, 2, 3, 4}));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{0, 0, 9, 0, 0}, sol.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{1, 1}, sol.productExceptSelf(new int[]{1, 1}));
    }
}
