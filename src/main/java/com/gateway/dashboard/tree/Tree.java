package com.gateway.dashboard.tree;

import java.util.Random;

public class Tree {

    public TreeNode invertTree(TreeNode root) {
        if(root==null){
            return root;
        }

        // Recursively invert the left
        TreeNode left = root.getLeft();
        invertTree(left);

        // Recursively invert the right
        TreeNode right = root.getRight();
        invertTree(right);

        // Invert/Swap left and right on this node
        TreeNode t = left;
        root.setLeft(right);
        root.setRight(t);

        return root;
    }

    public TreeNode generateRandomTree() {
        Random random = new Random();
        random.nextBoolean();
        return new TreeNode();
    }

}
