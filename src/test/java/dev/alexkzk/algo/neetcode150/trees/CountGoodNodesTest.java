package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CountGoodNodesTest {
    private final CountGoodNodes sol = new CountGoodNodes();

    @Test
    void example1() {
        TreeNode root = new TreeNode(3, new TreeNode(1, new TreeNode(3), null), new TreeNode(4, new TreeNode(1), new TreeNode(5)));
        assertEquals(4, sol.goodNodes(root));
    }

    @Test
    void example2() {
        TreeNode root = new TreeNode(3, new TreeNode(3, new TreeNode(4), new TreeNode(2)), null);
        assertEquals(3, sol.goodNodes(root));
    }

    @Test
    void edgeCase() {
        assertEquals(1, sol.goodNodes(new TreeNode(1)));
    }
}
