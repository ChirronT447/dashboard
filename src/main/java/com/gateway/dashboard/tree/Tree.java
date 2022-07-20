package com.gateway.dashboard.tree;

import java.util.*;

public class Tree {

    static class Node {
        private boolean visited = false;
        private final List<Node> childNodes = new ArrayList<>();
        public Node(List<Node> childNodes) {
            if(childNodes != null) {
                this.childNodes.addAll(childNodes);
            }
        }
        public void visit() {
            this.visited = true;
        }
    }

    public static boolean isTree(Node rootNode) {
        final Queue<Node> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(rootNode);
        while(!nodeQueue.isEmpty()) {
            Node node = nodeQueue.remove();
            if(node.visited) {
                return false;
            } else {
                node.visit();
            }
            nodeQueue.addAll(node.childNodes);
        }
        return true;
    }

}
