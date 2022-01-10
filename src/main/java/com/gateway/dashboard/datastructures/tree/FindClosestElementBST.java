package com.gateway.dashboard.datastructures.tree;

public class FindClosestElementBST {

    static int min_diff = Integer.MAX_VALUE;
    static int result = Integer.MIN_VALUE;

    public static class Node {
        int key;
        Node left, right;
    }

    public static void  maxDiff(Node node, int value) {
        if (node == null) {
            return;
        }

        if(node.key == value) {
            result = value;
            return;
        }

        int nextValue = Math.abs(node.key - value);
        if(min_diff > nextValue) {
            min_diff = nextValue;
            result = node.key;
        }

        if(value < node.key) {
            maxDiff(node.left, value);
        } else {
            maxDiff(node.right, value);
        }
    }

}
