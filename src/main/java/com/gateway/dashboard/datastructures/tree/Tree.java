package com.gateway.dashboard.datastructures.tree;

import java.util.Random;

public class Tree {

    public Node invertTree(Node root) {
        if(root==null){
            return root;
        }

        // Recursively invert the left
        Node left = root.getLeft();
        invertTree(left);

        // Recursively invert the right
        Node right = root.getRight();
        invertTree(right);

        // Invert/Swap left and right on this node
        Node t = left;
        root.setLeft(right);
        root.setRight(t);

        return root;
    }

    public Node generateRandomTree() {
        Random random = new Random();
        random.nextBoolean();
        return new Node();
    }

}
