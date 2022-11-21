package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VerticalTraversalTest {

    @Test
    void verticalTraversal() {
        // Input: root = [3,9,20,null,null,15,7]
        //Output: [[9],[3,15],[20],[7]]
        //Explanation:
        //Column -1: Only node 9 is in this column.
        //Column 0: Nodes 3 and 15 are in this column in that order from top to bottom.
        //Column 1: Only node 20 is in this column.
        //Column 2: Only node 7 is in this column.
        Assertions.assertThat(
                VerticalTraversal.verticalTraversal(
                        new VerticalTraversal.TreeNode()
                )
        ).isEqualTo(List.of(
                List.of(9),List.of(3,15),List.of(20),List.of(7)
        ));

        // Input: root = [1,2,3,4,5,6,7]
        //Output: [[4],[2],[1,5,6],[3],[7]]

        // Input: root = [1,2,3,4,6,5,7]
        //Output: [[4],[2],[1,5,6],[3],[7]]


    }
}