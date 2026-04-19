package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BalancedBinaryTreeTest {
    private final BalancedBinaryTree sol = new BalancedBinaryTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertTrue(sol.isBalanced(root));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3, new TreeNode(4), null), null), null);
        assertFalse(sol.isBalanced(root));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isBalanced(null));
    }
}
