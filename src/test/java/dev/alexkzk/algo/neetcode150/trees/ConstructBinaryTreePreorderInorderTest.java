package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ConstructBinaryTreePreorderInorderTest {
    private final ConstructBinaryTreePreorderInorder sol = new ConstructBinaryTreePreorderInorder();

    @Test
    void example1() {
        TreeNode root = sol.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        assertEquals(3, root.val);
        assertEquals(9, root.left.val);
        assertEquals(20, root.right.val);
    }

    @Test
    void example2() {
        TreeNode root = sol.buildTree(new int[]{-1}, new int[]{-1});
        assertEquals(-1, root.val);
    }

    @Test
    void edgeCase() {
        TreeNode root = sol.buildTree(new int[]{1, 2}, new int[]{2, 1});
        assertEquals(1, root.val);
        assertEquals(2, root.left.val);
    }
}
