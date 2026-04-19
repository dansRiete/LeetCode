package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class InvertBinaryTreeTest {
    private final InvertBinaryTree sol = new InvertBinaryTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(4, new TreeNode(2, new TreeNode(1), new TreeNode(3)), new TreeNode(7, new TreeNode(6), new TreeNode(9)));
        TreeNode inverted = sol.invertTree(root);
        assertEquals(7, inverted.left.val);
        assertEquals(2, inverted.right.val);
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        TreeNode inverted = sol.invertTree(root);
        assertEquals(3, inverted.left.val);
        assertEquals(1, inverted.right.val);
    }

    @Test
    void edgeCase() {
        assertNull(sol.invertTree(null));
    }
}
