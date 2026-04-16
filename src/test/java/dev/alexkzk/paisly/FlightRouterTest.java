package dev.alexkzk.paisly;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightRouterTest {

    // ── reconstructItinerary ─────────────────────────────────────────────────

    @Test
    void reconstruct_basicCase() {
        var segments = List.of("LAX->ORD", "JFK->LAX", "ORD->MIA");
        assertEquals(List.of("JFK", "LAX", "ORD", "MIA"), FlightRouter.reconstructItinerary(segments));
    }

    @Test
    void reconstruct_alreadyOrdered() {
        var segments = List.of("JFK->LAX", "LAX->ORD", "ORD->MIA");
        assertEquals(List.of("JFK", "LAX", "ORD", "MIA"), FlightRouter.reconstructItinerary(segments));
    }

    @Test
    void reconstruct_singleSegment() {
        assertEquals(List.of("A", "B"), FlightRouter.reconstructItinerary(List.of("A->B")));
    }

    @Test
    void reconstruct_twoSegments() {
        var segments = List.of("B->C", "A->B");
        assertEquals(List.of("A", "B", "C"), FlightRouter.reconstructItinerary(segments));
    }

    // ── countLayovers ────────────────────────────────────────────────────────

    @Test
    void layovers_basicCase() {
        var segments = List.of("JFK->LAX", "LAX->ORD", "ORD->MIA");
        assertEquals(2, FlightRouter.countLayovers(segments));
    }

    @Test
    void layovers_directFlight() {
        assertEquals(0, FlightRouter.countLayovers(List.of("JFK->LAX")));
    }

    @Test
    void layovers_twoSegments() {
        assertEquals(1, FlightRouter.countLayovers(List.of("JFK->LAX", "LAX->MIA")));
    }

    @Test
    void layovers_fourSegments() {
        var segments = List.of("A->B", "B->C", "C->D", "D->E");
        assertEquals(3, FlightRouter.countLayovers(segments));
    }

    // ── canReach ─────────────────────────────────────────────────────────────

    @Test
    void canReach_directConnection() {
        var segments = List.of("JFK->LAX", "LAX->ORD", "ORD->MIA");
        assertTrue(FlightRouter.canReach(segments, "JFK", "LAX"));
    }

    @Test
    void canReach_multipleHops() {
        var segments = List.of("JFK->LAX", "LAX->ORD", "ORD->MIA");
        assertTrue(FlightRouter.canReach(segments, "JFK", "MIA"));
        assertTrue(FlightRouter.canReach(segments, "LAX", "MIA"));
    }

    @Test
    void canReach_reverseDirection() {
        var segments = List.of("JFK->LAX", "LAX->ORD", "ORD->MIA");
        assertFalse(FlightRouter.canReach(segments, "MIA", "JFK"));
    }

    @Test
    void canReach_sameOriginAndDestination() {
        var segments = List.of("JFK->LAX", "LAX->ORD");
        assertFalse(FlightRouter.canReach(segments, "JFK", "JFK"));
    }

    @Test
    void canReach_cityNotInSegments() {
        var segments = List.of("JFK->LAX", "LAX->ORD");
        assertFalse(FlightRouter.canReach(segments, "JFK", "MIA"));
    }

    @Test
    void canReach_singleSegment() {
        assertTrue(FlightRouter.canReach(List.of("JFK->LAX"), "JFK", "LAX"));
        assertFalse(FlightRouter.canReach(List.of("JFK->LAX"), "LAX", "JFK"));
    }
}
