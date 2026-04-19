package dev.alexkzk.algo.neetcode150.graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CloneGraphTest {
    private final CloneGraph sol = new CloneGraph();

    @Test
    void example1() {
        CloneGraph.Node n1 = new CloneGraph.Node(1, new ArrayList<>());
        CloneGraph.Node n2 = new CloneGraph.Node(2, new ArrayList<>());
        n1.neighbors.add(n2);
        n2.neighbors.add(n1);
        CloneGraph.Node clone = sol.cloneGraph(n1);
        assertNotSame(n1, clone);
        assertEquals(1, clone.val);
        assertEquals(2, clone.neighbors.get(0).val);
    }

    @Test
    void example2() {
        assertNull(sol.cloneGraph(null));
    }

    @Test
    void edgeCase() {
        CloneGraph.Node n = new CloneGraph.Node(1, new ArrayList<>());
        CloneGraph.Node clone = sol.cloneGraph(n);
        assertNotSame(n, clone);
        assertEquals(1, clone.val);
    }
}
