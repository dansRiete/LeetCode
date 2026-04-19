package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class SerializeDeserializeBinaryTreeTest {
    private final SerializeDeserializeBinaryTree sol = new SerializeDeserializeBinaryTree();

    @Test
    void example1() {
        TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3, new TreeNode(4), new TreeNode(5)));
        TreeNode result = sol.deserialize(sol.serialize(root));
        assertEquals(1, result.val);
        assertEquals(2, result.left.val);
        assertEquals(3, result.right.val);
    }

    @Test
    void example2() {
        assertNull(sol.deserialize(sol.serialize(null)));
    }

    @Test
    void edgeCase() {
        TreeNode root = new TreeNode(1);
        assertEquals(1, sol.deserialize(sol.serialize(root)).val);
    }
}
