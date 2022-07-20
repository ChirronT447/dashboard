package com.gateway.dashboard.datastructures.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FindClosestElementBSTTest {

    static FindClosestElementBST.Node newNode(int key) {
        FindClosestElementBST.Node node = new FindClosestElementBST.Node();
        node.key = key;
        return node;
    }

    @Test
    void maxDiff() {
        FindClosestElementBST.Node root = newNode(9);
        root.left = newNode(4);
        root.left.left = newNode(3);
        root.left.right = newNode(6);
        root.left.right.left = newNode(5);
        root.left.right.right = newNode(7);
        root.right = newNode(17);
        root.right.right = newNode(22);
        root.right.right.left = newNode(20);

        int k = 18;
        FindClosestElementBST.maxDiff(root, k);
        Assertions.assertEquals(FindClosestElementBST.result, 17);
    }
}