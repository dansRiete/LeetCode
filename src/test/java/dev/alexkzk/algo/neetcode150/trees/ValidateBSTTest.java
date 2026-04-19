package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ValidateBSTTest {
    private final ValidateBST sol = new ValidateBST();

    @Test
    void example1() {
        TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        assertTrue(sol.isValidBST(root));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(5, new TreeNode(1), new TreeNode(4, new TreeNode(3), new TreeNode(6)));
        assertFalse(sol.isValidBST(root));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.isValidBST(new TreeNode(1)));
    }
}
