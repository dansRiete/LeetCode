package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DiameterBinaryTreeTest {
    private final DiameterBinaryTree sol = new DiameterBinaryTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(4), new TreeNode(5)), new TreeNode(3));
        assertEquals(3, sol.diameterOfBinaryTree(root));
    }

    @Test
    void example2() {
        assertEquals(1, sol.diameterOfBinaryTree(new TreeNode(1, new TreeNode(2), null)));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.diameterOfBinaryTree(new TreeNode(1)));
    }
}
