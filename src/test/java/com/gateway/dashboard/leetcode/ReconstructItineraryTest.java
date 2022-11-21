package com.gateway.dashboard.leetcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReconstructItineraryTest {

    @Test
    void findItinerary() {
        Assertions.assertThat(ReconstructItinerary.findItinerary(
                new String[][]{{"MUC","LHR"},{"JFK","MUC"},{"SFO","SJC"},{"LHR","SFO"}}
        )).isEqualTo(List.of("JFK","MUC","LHR","SFO","SJC"));

        Assertions.assertThat(ReconstructItinerary.findItinerary(
                new String[][]{{"JFK","SFO"},{"JFK","ATL"},{"SFO","ATL"},{"ATL","JFK"},{"ATL","SFO"}}
        )).isEqualTo(List.of("JFK","ATL","JFK","SFO","ATL","SFO"));
    }
}