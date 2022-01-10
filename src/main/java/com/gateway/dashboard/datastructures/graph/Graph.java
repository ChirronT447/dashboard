package com.gateway.dashboard.datastructures.graph;

import java.util.*;

/**
 * The graph represents an undirected graph
 */
public final class Graph<T> implements Iterable<T> {

    /*
     * A map from the nodeId to outgoing edge.
     * An outgoing edge is represented as a tuple of Node and the edge length
     */
    private final Map<T, Map<Node<T>, Double>> graph;

    /*
     * A map of heuristic from a node to each other node in the graph.
     */
    private final Map<T, Heuristic> heuristicMap;

    /*
     * A map between nodeId and node.
     */
    private final Map<T, Node<T>> nodeIdNode;

    public Graph(Map<T, Heuristic> heuristicMap) {
        graph = new HashMap<>();
        nodeIdNode = new HashMap<>();
        this.heuristicMap = heuristicMap;
    }

    /**
     * Adds a new node to the graph.
     * Internally it creates the node and populates the heuristic map concerning input node into node data.
     * @param nodeId the node to be added
     */
    public void addNode(T nodeId) {
        graph.put(nodeId, new HashMap<>());
        nodeIdNode.put(
                nodeId,
                new Node<>(nodeId, heuristicMap.get(nodeId))
        );
    }

    /**
     * Adds an edge from source node to destination node.
     * There can only be a single edge from source to node.
     * Adding additional edge would overwrite the value
     * @param nodeIdFirst   the first node to be in the edge
     * @param nodeIdSecond  the second node to be second node in the edge
     * @param length        the length of the edge.
     */
    public void addEdge(T nodeIdFirst, T nodeIdSecond, double length) {
        graph.get(nodeIdFirst).put(nodeIdNode.get(nodeIdSecond), length);
        graph.get(nodeIdSecond).put(nodeIdNode.get(nodeIdFirst), length);
    }

    /**
     * Returns immutable view of the edges
     * @param nodeId    the nodeId whose outgoing edge needs to be returned
     * @return          An immutable view of edges leaving that node
     */
    public Map<Node<T>, Double> edgesFrom (T nodeId) {
        return Collections.unmodifiableMap(graph.get(nodeId));
    }

    /**
     * The node corresponding to the current nodeId.
     * @param nodeId    the nodeId to be returned
     * @return          the nodeData from the
     */
    public Node<T> getNode (T nodeId) {
        return nodeIdNode.get(nodeId);
    }

    /**
     * Returns an iterator that can traverse the nodes of the graph
     * @return an Iterator.
     */
    @Override public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}