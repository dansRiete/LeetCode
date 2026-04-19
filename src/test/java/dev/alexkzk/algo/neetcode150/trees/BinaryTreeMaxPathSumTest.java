package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BinaryTreeMaxPathSumTest {
    private final BinaryTreeMaxPathSum sol = new BinaryTreeMaxPathSum();

    @Test
    void example1() {
        assertEquals(6, sol.maxPathSum(new TreeNode(1, new TreeNode(2), new TreeNode(3))));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(-10, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertEquals(42, sol.maxPathSum(root));
    }

    @Test
    void edgeCase() {
        assertEquals(-3, sol.maxPathSum(new TreeNode(-3)));
    }
}
