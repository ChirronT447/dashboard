package com.gateway.dashboard.tree;

import com.gateway.dashboard.datastructures.tree.Tree;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TreeTest {

    @Test
    void checkIsTree() {
        Tree.Node nodeOne = new Tree.Node(null);
        Tree.Node nodeTwo = new Tree.Node(null);
        Tree.Node nodeFour = new Tree.Node(null);
        Tree.Node nodeThree = new Tree.Node(List.of(nodeFour));
        Tree.Node rootNode = new Tree.Node(List.of(nodeOne, nodeTwo, nodeThree));
        Assertions.assertThat(Tree.isTree(rootNode)).isTrue();
    }

    @Test
    void checkIsNotTree() {
        Tree.Node nodeTwo = new Tree.Node(null);
        Tree.Node nodeFour = new Tree.Node(null);
        Tree.Node nodeOne = new Tree.Node(List.of(nodeTwo));
        Tree.Node nodeThree = new Tree.Node(List.of(nodeFour));
        Tree.Node rootNode = new Tree.Node(List.of(nodeOne, nodeTwo, nodeThree));
        Assertions.assertThat(Tree.isTree(rootNode)).isFalse();
    }


}