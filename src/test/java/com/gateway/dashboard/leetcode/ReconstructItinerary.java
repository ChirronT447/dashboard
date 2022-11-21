package com.gateway.dashboard.leetcode;

import java.util.*;

/**
 * 332. Reconstruct Itinerary - Hard
 *https://leetcode.com/problems/reconstruct-itinerary/
 * You are given a list of airline tickets where tickets[i] = [from(i), to(i)] represent the departure and the
 *  arrival airports of one flight. Reconstruct the itinerary in order and return it.
 * All the tickets belong to a man who departs from "JFK", thus, the itinerary must begin with "JFK".
 * If there are multiple valid itineraries, you should return the itinerary that has the smallest
 *  lexical order when read as a single string.
 * For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
 * You may assume all tickets form at least one valid itinerary. You must use all the tickets once and only once.
 */
public class ReconstructItinerary {

    // All the airports are vertices and tickets are directed edges.
    // Then all these tickets form a directed graph.
    // We start from "JFK" & find a path in the graph which is a valid reconstruction.
    // Since the problem asks for the shortest lexical order, we use a PriorityQueue.
    // In this way, we always visit the smallest possible neighbor first in our trip.
    public static List<String> findItinerary(String[][] tickets) {
        final Map<String, PriorityQueue<String>> flights = new HashMap<>();
        final LinkedList<String> path = new LinkedList<>();
        for (String[] ticket : tickets) {
            flights.putIfAbsent(ticket[0], new PriorityQueue<>());
            flights.get(ticket[0]).add(ticket[1]);
        }
        dfs("JFK", flights, path);
        return path;
    }

    // NB: Comparator.reverseOrder() gives a comparator giving the reverse of the natural ordering.
    // If you want to reverse the ordering of an existing comparator, you can use Comparator.reversed().
    // eg. Stream.of(1, 4, 2, 5).sorted(Comparator.reverseOrder());
    // - The stream is now [5, 4, 2, 1]
    // Stream.of("foo", "test", "a").sorted(Comparator.comparingInt(String::length).reversed());
    // - The stream is now [test, foo, a], sorted by descending length
    private static void dfs(
            final String departure,
            final Map<String, PriorityQueue<String>> flights,
            final LinkedList<String> path
    ) {
        final PriorityQueue<String> arrivals = flights.get(departure);
        while (arrivals != null && !arrivals.isEmpty()) {
            dfs(arrivals.poll(), flights, path);
        }
        path.addFirst(departure);
    }

}
