package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BinaryTreeLevelOrderTest {
    private final BinaryTreeLevelOrder sol = new BinaryTreeLevelOrder();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        List<List<Integer>> result = sol.levelOrder(root);
        assertEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), result);
    }

    @Test
    void example2() {
        assertEquals(List.of(List.of(1)), sol.levelOrder(new TreeNode(1)));
    }

    @Test
    void edgeCase() {
        assertEquals(List.of(), sol.levelOrder(null));
    }
}
