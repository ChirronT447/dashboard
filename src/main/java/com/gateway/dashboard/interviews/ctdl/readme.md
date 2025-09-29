
Here is a list of 25 problems organized by category.

#### Category 1: Graph/Tree Traversal (BFS & DFS)

1.  **Number of Islands (\#200):** Classic grid traversal using DFS to "sink" islands or BFS to explore them.
2.  **Rotting Oranges (\#994):** Multi-source BFS to find the minimum time to rot all fresh oranges.
3.  **Clone Graph (\#133):** Graph traversal (DFS or BFS) using a `HashMap` to map old nodes to their new clones to avoid cycles.
4.  **Course Schedule (\#207):** Directed graph cycle detection using DFS with a recursion state set (visiting, visited).
5.  **Word Ladder (\#127):** Classic unweighted shortest path problem, a perfect fit for BFS.
6.  **Pacific Atlantic Water Flow (\#417):** Start a traversal (DFS or BFS) inwards from the ocean cells to see which cells can reach both.
7.  **Validate Binary Search Tree (\#98):** In-order DFS traversal, ensuring that each node's value is greater than the previously visited node.
8.  **Lowest Common Ancestor of a Binary Tree (\#236):** A recursive DFS approach is most elegant; the LCA is where paths from the root to the two nodes diverge.
9.  **Serialize and Deserialize Binary Tree (\#297):** Pre-order DFS for serialization and a queue-based reconstruction for deserialization.
10. **Binary Tree Maximum Path Sum (\#124):** A post-order DFS where each node returns the max path sum downwards, while a global max tracks paths that go up and then down.
11. **All Nodes Distance K in Binary Tree (\#863):** Convert the tree into a graph (using a `HashMap`), then perform a BFS starting from the `target` node.

#### Category 2: Caching

12. **LRU Cache (\#146):** The quintessential caching problem. Requires a `HashMap` for $O(1)$ lookups and a Doubly Linked List for $O(1)$ eviction.
13. **LFU Cache (\#460):** A harder version of LRU. Requires a more complex structure, typically two `HashMap`s: one for key-value and another mapping frequencies to Doubly Linked Lists of items with that frequency.
14. **Design Hit Counter (\#362):** Caching with a time component. Can be solved efficiently with a queue or a circular array to track hits within a time window.
15. **Design In-Memory File System (\#588):** A problem that tests object-oriented design and caching principles, often solved with a Trie data structure representing the file path hierarchy.

#### Category 3: List & String Manipulation

16. **Reverse Nodes in k-Group (\#25):** The gold standard for complex linked-list pointer manipulation. Requires careful management of pointers for the previous group, current group, and next group.
17. **Copy List with Random Pointer (\#138):** A linked list problem where a `HashMap` is crucial to map original nodes to their copies, allowing for the correct assignment of `random` pointers.
18. **Merge Two Sorted Lists (\#21):** A foundational two-pointer problem, essential for understanding more complex list algorithms.
19. **Add Two Numbers (\#2):** Linked list manipulation that simulates arithmetic, requiring careful handling of pointers and a `carry` variable.
20. **Longest Substring Without Repeating Characters (\#3):** The classic sliding window problem, using a `HashSet` or `HashMap` to keep track of characters in the current window.
21. **Minimum Window Substring (\#76):** A hard sliding window problem requiring a `HashMap` to track character counts of the target string `t`.
22. **Basic Calculator (\#224):** A string parsing problem where a `Stack` is used to handle nested parentheses and signs.
23. **Decode String (\#394):** A recursive or stack-based string parsing problem, e.g., `3[a2[c]]` -\> `accaccacc`.
24. **Text Justification (\#68):** A challenging string manipulation problem with many edge cases for spacing words to fit a fixed width.
25. **Trapping Rain Water (\#42):** Can be solved with two pointers moving inwards, keeping track of the max height seen from the left and right.
