package dev.alexkzk.algo.neetcode150.trees;

import dev.alexkzk.algo.neetcode150.TreeNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class BinaryTreeRightSideViewTest {
    private final BinaryTreeRightSideView sol = new BinaryTreeRightSideView();

    @Test
    void example1() {
        TreeNode root = new TreeNode(1, new TreeNode(2, null, new TreeNode(5)), new TreeNode(3, null, new TreeNode(4)));
        assertEquals(List.of(1, 3, 4), sol.rightSideView(root));
    }

    @Test
    void example2() {
        assertEquals(List.of(1, 3), sol.rightSideView(new TreeNode(1, null, new TreeNode(3))));
    }

    @Test
    void edgeCase() {
        assertEquals(List.of(), sol.rightSideView(null));
    }
}
