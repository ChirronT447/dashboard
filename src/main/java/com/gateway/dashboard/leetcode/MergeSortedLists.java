package com.gateway.dashboard.leetcode;

import java.util.*;

/**
 * 23. Merge k Sorted Lists - Hard
 * https://leetcode.com/problems/merge-k-sorted-lists/
 * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
 * Merge all the linked-lists into one sorted linked-list and return it.
 */
public class MergeSortedLists {

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0 || Arrays.stream(lists).anyMatch(Objects::isNull))
            return new ListNode(0, null);

        // For ordering, based on value:
        final Queue<ListNode> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.val));
        nodeQueue.addAll(Arrays.asList(lists));

        final ListNode head = new ListNode(0, null);
        ListNode point = head;
        while(!nodeQueue.isEmpty()){
            point.next = nodeQueue.poll();
            point = point.next;
            final ListNode next = point.next;
            if(next!=null){
                nodeQueue.add(next);
            }
        }
        return head.next;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
