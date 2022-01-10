package com.gateway.dashboard.datastructures.graph;

import java.util.*;

public class AStar<T> {

    private final Graph<T> graph;

    public AStar (Graph<T> graph) {
        this.graph = graph;
    }

    /**
     * Implements the A-star algorithm and returns the path from source to destination
     * @param source        the source nodeId
     * @param destination   the destination nodeId
     * @return              the path from source to destination
     */
    public List<T> astar(T source, T destination) {

        // The set of discovered nodes
        final Queue<Node<T>> openQueue = new PriorityQueue<>(new NodeComparator());

        // already visited
        final Set<Node<T>> closedList = new HashSet<>();

        // For node n, path[n] is the node immediately preceding it on the currently known cheapest path from start - n
        final Map<T, T> path = new HashMap<>();

        // Initially, only the start node is known.
        Node<T> sourceNode = graph.getNode(source).setDistance(0, destination);
        openQueue.add(sourceNode);

        while (!openQueue.isEmpty()) {
            final Node<T> currentNode = openQueue.poll();

            // if we've reached our destination lets walk back the path
            if (currentNode.getNodeId().equals(destination)) {
                return reconstructPath(path, destination);
            }

            closedList.add(currentNode);
            // for each neighbor of the current node
            for (Map.Entry<Node<T>, Double> neighbourEntry : graph.edgesFrom(currentNode.getNodeId()).entrySet()) {

                Node<T> neighbour = neighbourEntry.getKey();
                if (closedList.contains(neighbour)) continue;

                double distanceBetweenTwoNodes = neighbourEntry.getValue();
                double tentativeDistance = distanceBetweenTwoNodes + currentNode.getDistanceFromSource();

                if (tentativeDistance < neighbour.getDistanceFromSource()) {
                    neighbour.setDistance(tentativeDistance, destination);
                    path.put(neighbour.getNodeId(), currentNode.getNodeId());
                    if (!openQueue.contains(neighbour)) {
                        openQueue.add(neighbour);
                    }
                }
            }
        }
        return List.of();
    }

    private List<T> reconstructPath(Map<T, T> path, T destination) {
        final List<T> pathList = new ArrayList<>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }

    private class NodeComparator implements Comparator<Node<T>> {
        public int compare(Node<T> node, Node<T> node2) {
            if (node.getWeight() > node2.getWeight()) return 1;
            if (node2.getWeight() > node.getWeight()) return -1;
            return 0;
        }
    }

}