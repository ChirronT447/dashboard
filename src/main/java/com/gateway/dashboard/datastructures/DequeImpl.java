package com.gateway.dashboard.datastructures;

public class DequeImpl {

        // Node of a doubly linked list
        private static class Node {
            private String data;
            private Node prev;
            private Node next;

            // Function to get a new node
            public static Node getnode(String data) {
                Node newNode = new Node();
                newNode.data = data;
                newNode.prev = newNode.next = null;
                return newNode;
            }
        }

        // A structure to represent a deque
        public static class Deque {
            private Node front;
            private Node rear;
            private int size;

            Deque() {
                front = null;
                rear = null;
                size = 0;
            }

            public static Deque getInstance() {
                return new Deque();
            }

            public boolean isEmpty() {
                return (size == 0);
            }

            public int getSize() {
                return this.size;
            }

            public void addFirst(String data) {
                Node newNode = Node.getnode(data);
                if (isEmpty()) { // If deque is empty
                    rear = front = newNode;
                }
                else { // Inserts node at the front end
                    newNode.next = front;
                    front.prev = newNode;
                    front = newNode;
                }
                size++; // Increments count of elements by 1
            }

            // Function to insert an element at the rear end
            public void addLast(String data) {
                Node newNode = Node.getnode(data);
                if (isEmpty()) { // If deque is empty
                    front = rear = newNode;
                }
                else { // Inserts node at the rear end
                    newNode.prev = rear;
                    rear.next = newNode;
                    rear = newNode;
                }
                size++;
            }

            // Function to delete the element from the front end
            public void removeFirst() {
                if (!isEmpty()) {
                    front = front.next;
                    // If only one element was present
                    if (front == null) {
                        rear = null;
                    } else {
                        front.prev = null;
                    }
                    size--;
                }
            }
        }
}