package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LowestCommonAncestorBSTTest {
    private final LowestCommonAncestorBST sol = new LowestCommonAncestorBST();

    @Test
    void example1() {
        TreeNode n2 = new TreeNode(2);
        TreeNode n8 = new TreeNode(8);
        TreeNode root = new TreeNode(6, new TreeNode(2, new TreeNode(0), new TreeNode(4, new TreeNode(3), new TreeNode(5))), new TreeNode(8));
        TreeNode p = root.left;
        TreeNode q = root.right;
        assertEquals(6, sol.lowestCommonAncestor(root, p, q).val);
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(6, new TreeNode(2, new TreeNode(0), new TreeNode(4, new TreeNode(3), new TreeNode(5))), new TreeNode(8));
        TreeNode p = root.left;
        TreeNode q = root.left.right;
        assertEquals(2, sol.lowestCommonAncestor(root, p, q).val);
    }

    @Test
    void edgeCase() {
        TreeNode root = new TreeNode(2, new TreeNode(1), null);
        assertEquals(2, sol.lowestCommonAncestor(root, root, root.left).val);
    }
}
