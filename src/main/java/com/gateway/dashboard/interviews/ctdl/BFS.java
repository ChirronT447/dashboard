package com.gateway.dashboard.interviews.ctdl;

import java.util.*;
import java.util.function.Consumer;

/**
 * Provides a succinct, generic implementation of the Breadth-First Search (BFS) algorithm.
 */
public class BFS {

    /**
     * Performs a Breadth-First Search traversal on a graph starting from a given node.
     *
     * @param <T>       The type of the nodes in the graph.
     * @param graph     The graph represented as an adjacency list (Map of node to its neighbors).
     * @param startNode The node from which to begin the traversal.
     * @param onVisit   A {@link Consumer} action to be performed on each node as it is visited.
     */
    public static <T> void search(
            Map<T, List<T>> graph,
            T startNode,
            Consumer<T> onVisit
    ) {
        if (!graph.containsKey(startNode)) {
            return; // Or throw an exception if the start node must be in the graph.
        }

        var queue = new ArrayDeque<T>();
        var visited = new HashSet<T>();

        queue.offer(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            var currentNode = queue.poll();
            onVisit.accept(currentNode);

            // Safely get neighbors, handling nodes with no outgoing edges.
            var neighbors = graph.getOrDefault(currentNode, Collections.emptyList());
            for (var neighbor : neighbors) {
                // The add() method of Set returns true if the element was not already present.
                // This concisely checks for visited status and marks as visited in one step.
                if (visited.add(neighbor)) {
                    queue.offer(neighbor);
                }
            }
        }
    }

    /**
     * A main method to demonstrate the BFS algorithm.
     */
    public static void main(String[] args) {
        // Create a sample graph using an adjacency list.
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A", "D", "E"));
        graph.put("C", List.of("A", "F"));
        graph.put("D", List.of("B"));
        graph.put("E", List.of("B", "F"));
        graph.put("F", List.of("C", "E"));
        // "G" is a disconnected node
        graph.put("G", List.of());

        System.out.println("BFS traversal starting from node 'A':");
        // Use a method reference for the consumer action to print each visited node.
        var visitedNodes = new ArrayList<String>();
        BFS.search(graph, "A", visitedNodes::add);
        System.out.println(String.join(" -> ", visitedNodes));

        System.out.println("\nBFS traversal starting from node 'F':");
        BFS.search(graph, "F", node -> System.out.print(node + " "));
        System.out.println();
    }
}
