package com.gateway.dashboard.datastructures.graph;

/**
 * where n is the next node on the path,
 * g(n) is the cost of the path from the start node to n
 * h(n) is a heuristic function that estimates the cost of the cheapest path from n to the goal.
 * @param <T>
 */
public class Node<T> {

    private final T nodeId;
    private final Heuristic heuristic;

    private double distanceFromSource;          // g is distance from the source
    private double heuristicOfDestination;      // h is the heuristic of destination.
    private double weight;                      // f = g + h

    public Node (T nodeId, Heuristic heuristic) {
        this.nodeId = nodeId;
        this.distanceFromSource = Double.MAX_VALUE;
        this.heuristic = heuristic;
    }

    /**
     * Any call to setG should update dependent fields.
     * @param distanceFromSource
     * @param destination
     */
    public Node setDistance(double distanceFromSource, T destination) {
        this.distanceFromSource = distanceFromSource;
        calculateWeight(destination);
        return this;
    }

    private void calculateWeight(T destination) {
        this.heuristicOfDestination = heuristic.get(destination);
        this.weight = distanceFromSource + heuristicOfDestination;
    }

    public T getNodeId() {
        return nodeId;
    }

    public double getDistanceFromSource() {
        return distanceFromSource;
    }

    public double getHeuristicOfDestination() {
        return heuristicOfDestination;
    }

    public double getWeight() {
        return weight;
    }

}
