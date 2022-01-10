package com.gateway.dashboard.datastructures.tree;

public class Node {

    private Node left;
    private Node right;

    public Node() {}

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }

    public Node setLeft(Node left) {
        this.left = left;
        return this;
    }

    public Node setRight(Node right) {
        this.right = right;
        return this;
    }

}