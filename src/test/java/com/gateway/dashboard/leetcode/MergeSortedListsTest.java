package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortedListsTest {

    @Test
    void testMergeSortedLists() {
        //Input: lists = []
        //Output: []
        MergeSortedLists.ListNode[] lists = {};
        MergeSortedLists.ListNode expected = new MergeSortedLists.ListNode(0, null);
        Assertions.assertThat(MergeSortedLists.mergeKLists(lists)).isNotNull();
        //Input: lists = [[]]
        //Output: []
        MergeSortedLists.ListNode[] lists2 = {null};
        Assertions.assertThat(MergeSortedLists.mergeKLists(lists2)).isNotNull();
        // Example 1:
        //
        //Input: lists = [[1,4,5],[1,3,4],[2,6]]
        //Output: [1,1,2,3,4,4,5,6]
        //Explanation: The linked-lists are:
        //[
        //  1->4->5,
        //  1->3->4,
        //  2->6
        //]
        //merging them into one sorted list:
        //1->1->2->3->4->4->5->6

    }
}