package com.gateway.dashboard.graph;

import com.gateway.dashboard.datastructures.graph.AStar;
import com.gateway.dashboard.datastructures.graph.Graph;
import com.gateway.dashboard.datastructures.graph.Heuristic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class AStarTest {

    private static Map<String, Heuristic> heuristic = new HashMap<>();

    @BeforeAll
    public static void setup() {
        setupHeuristics();
    }

    @Test
    void astar() {
        Graph<String> graph = new Graph<>(heuristic);
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("E");
        graph.addNode("F");

        graph.addEdge("A", "B",  10);
        graph.addEdge("A", "E", 100);
        graph.addEdge("B", "C", 10);
        graph.addEdge("C", "E", 10);
        graph.addEdge("C", "F", 30);
        graph.addEdge("E", "F", 10);

        AStar<String> aStar = new AStar<>(graph);

        for (String path : aStar.astar("A", "F")) {
            System.out.println(path);
            System.out.println("|");
        }
    }

    private static void setupHeuristics() {

        // map for A
        Heuristic mapA = new Heuristic()
                .put("A",  0.0)
                .put("B", 10.0)
                .put("C", 20.0)
                .put("E", 100.0)
                .put("F", 110.0);

        // map for B
        Heuristic mapB = new Heuristic()
                .put("A", 10.0)
                .put("B",  0.0)
                .put("C", 10.0)
                .put("E", 25.0)
                .put("F", 40.0);

        // map for C
        Heuristic mapC = new Heuristic()
                .put("A", 20.0)
                .put("B", 10.0)
                .put("C",  0.0)
                .put("E", 10.0)
                .put("F", 30.0);

        // map for X
        Heuristic mapX = new Heuristic()
                .put("A", 100.0)
                .put("B", 25.0)
                .put("C", 10.0)
                .put("E",  0.0)
                .put("F", 10.0);

        // map for X
        Heuristic mapZ = new Heuristic<>()
                .put("A", 110.0)
                .put("B",  40.0)
                .put("C",  30.0)
                .put("E", 10.0)
                .put("F",  0.0);

        heuristic.put("A", mapA);
        heuristic.put("B", mapB);
        heuristic.put("C", mapC);
        heuristic.put("E", mapX);
        heuristic.put("F", mapZ);
    }
}