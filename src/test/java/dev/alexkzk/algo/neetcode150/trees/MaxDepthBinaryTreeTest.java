package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MaxDepthBinaryTreeTest {
    private final MaxDepthBinaryTree sol = new MaxDepthBinaryTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertEquals(3, sol.maxDepth(root));
    }

    @Test
    void example2() {
        assertEquals(2, sol.maxDepth(new TreeNode(1, null, new TreeNode(2))));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.maxDepth(null));
    }
}
