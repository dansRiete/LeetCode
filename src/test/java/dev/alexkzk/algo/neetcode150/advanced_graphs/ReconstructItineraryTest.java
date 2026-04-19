package dev.alexkzk.algo.neetcode150.advanced_graphs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ReconstructItineraryTest {
    private final ReconstructItinerary sol = new ReconstructItinerary();

    @Test
    void example1() {
        List<String> result = sol.findItinerary(List.of(List.of("MUC","LHR"),List.of("JFK","MUC"),List.of("SFO","SJC"),List.of("LHR","SFO")));
        assertEquals(List.of("JFK","MUC","LHR","SFO","SJC"), result);
    }

    @Test
    void example2() {
        List<String> result = sol.findItinerary(List.of(List.of("JFK","SFO"),List.of("JFK","ATL"),List.of("SFO","ATL"),List.of("ATL","JFK"),List.of("ATL","SFO")));
        assertEquals(List.of("JFK","ATL","JFK","SFO","ATL","SFO"), result);
    }

    @Test
    void edgeCase() {
        List<String> result = sol.findItinerary(List.of(List.of("JFK","KUL"),List.of("JFK","NRT"),List.of("NRT","JFK")));
        assertEquals(List.of("JFK","NRT","JFK","KUL"), result);
    }
}
