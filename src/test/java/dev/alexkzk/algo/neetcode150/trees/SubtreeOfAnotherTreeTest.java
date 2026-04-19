package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SubtreeOfAnotherTreeTest {
    private final SubtreeOfAnotherTree sol = new SubtreeOfAnotherTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(4, new TreeNode(1), new TreeNode(2)), new TreeNode(5));
        TreeNode sub = new TreeNode(4, new TreeNode(1), new TreeNode(2));
        assertTrue(sol.isSubtree(root, sub));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(3, new TreeNode(4, new TreeNode(1), new TreeNode(2, new TreeNode(0), null)), new TreeNode(5));
        TreeNode sub = new TreeNode(4, new TreeNode(1), new TreeNode(2));
        assertFalse(sol.isSubtree(root, sub));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isSubtree(new TreeNode(1), new TreeNode(1)));
    }
}
