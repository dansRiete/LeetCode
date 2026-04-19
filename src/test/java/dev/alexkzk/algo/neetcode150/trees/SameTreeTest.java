package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SameTreeTest {
    private final SameTree sol = new SameTree();

    @Test
    void example1() {
        assertTrue(sol.isSameTree(new TreeNode(1, new TreeNode(2), new TreeNode(3)), new TreeNode(1, new TreeNode(2), new TreeNode(3))));
    }

    @Test
    void example2() {
        assertFalse(sol.isSameTree(new TreeNode(1, new TreeNode(2), null), new TreeNode(1, null, new TreeNode(2))));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isSameTree(null, null));
    }
}
