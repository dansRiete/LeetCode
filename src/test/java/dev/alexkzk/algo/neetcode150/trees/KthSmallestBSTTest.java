package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class KthSmallestBSTTest {
    private final KthSmallestBST sol = new KthSmallestBST();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(1, null, new TreeNode(2)), new TreeNode(4));
        assertEquals(1, sol.kthSmallest(root, 1));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(5, new TreeNode(3, new TreeNode(2, new TreeNode(1), null), new TreeNode(4)), new TreeNode(6));
        assertEquals(3, sol.kthSmallest(root, 3));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.kthSmallest(new TreeNode(1), 1));
    }
}
