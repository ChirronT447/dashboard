package com.gateway.dashboard.interviews.ctdl;

/**
 * A node in a singly-linked list.
 */
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }
}

public class CycleDetection {

    /**
     * Detects if a singly-linked list has a cycle.
     *
     * @param head The head of the linked list.
     * @return true if the list has a cycle (i.e., the tail points to another node),
     * false otherwise.
     */
    public boolean hasCycle(ListNode head) {
        // An empty list or a list with a single node cannot have a cycle.
        if (head == null || head.next == null) {
            return false;
        }

        // Initialize two pointers, Tortoise (slow) and Hare (fast).
        ListNode slow = head;
        ListNode fast = head;

        // Traverse the list. The loop terminates if the fast pointer reaches the end.
        while (fast != null && fast.next != null) {
            // Slow pointer moves one step.
            slow = slow.next;
            // Fast pointer moves two steps.
            fast = fast.next.next;

            // If the pointers meet, a cycle is detected.
            if (slow == fast) {
                return true;
            }
        }

        // If the loop completes, the fast pointer reached the end, so no cycle exists.
        return false;
    }
}