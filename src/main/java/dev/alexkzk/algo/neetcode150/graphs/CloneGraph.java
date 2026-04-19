package dev.alexkzk.algo.neetcode150.graphs;

import java.util.List;

public class CloneGraph {

    public static class Node {
        public int val;
        public List<Node> neighbors;

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, List<Node> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    /** LC #133 — Clone Graph [Medium] */
    public Node cloneGraph(Node node) {
        throw new UnsupportedOperationException("TODO");
    }
}
