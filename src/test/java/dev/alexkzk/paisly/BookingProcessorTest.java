package dev.alexkzk.paisly;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookingProcessorTest {

    // ── mostExpensivePassenger ────────────────────────────────────────────────

    @Test
    void mostExpensive_basicCase() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,LAX,ORD,200", "Carol,JFK,MIA,500");
        assertEquals("Carol", BookingProcessor.mostExpensivePassenger(records));
    }

    @Test
    void mostExpensive_singleRecord() {
        var records = List.of("Alice,JFK,LAX,350");
        assertEquals("Alice", BookingProcessor.mostExpensivePassenger(records));
    }

    @Test
    void incorrectInputTest() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,ORD,700");
        assertEquals("Alice", BookingProcessor.mostExpensivePassenger(records));
    }

    @Test
    void mostExpensive_firstRecordIsHighest() {
        var records = List.of("Bob,ORD,DEN,999", "Alice,JFK,LAX,350", "Carol,JFK,MIA,500");
        assertEquals("Bob", BookingProcessor.mostExpensivePassenger(records));
    }

    @Test
    void mostExpensive_allSamePrice() {
        var records = List.of("Alice,JFK,LAX,100", "Bob,LAX,ORD,100", "Carol,JFK,MIA,100");
        // any of the three names is acceptable
        assertTrue(List.of("Alice", "Bob", "Carol")
                .contains(BookingProcessor.mostExpensivePassenger(records)));
    }

    @Test
    void emptyInputTest() {
        List<String> records = List.of();
        assertNull(BookingProcessor.mostExpensivePassenger(records));
    }

    @Test
    void nullInputTest() {
        assertNull(BookingProcessor.mostExpensivePassenger(null));
    }

    // ── passengersByDestination ───────────────────────────────────────────────

    @Test
    void byDestination_basicCase() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,LAX,LAX,200", "Carol,JFK,MIA,500");
        var result = BookingProcessor.passengersByDestination(records);

        assertEquals(2, result.size());
        assertTrue(result.get("LAX").containsAll(List.of("Alice", "Bob")));
        assertEquals(List.of("Carol"), result.get("MIA"));
    }

    @Test
    void byDestination_singleDestination() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,ORD,LAX,200");
        var result = BookingProcessor.passengersByDestination(records);

        assertEquals(1, result.size());
        assertTrue(result.get("LAX").containsAll(List.of("Alice", "Bob")));
    }

    @Test
    void byDestination_eachPassengerDifferentDestination() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,LAX,ORD,200", "Carol,JFK,MIA,500");
        var result = BookingProcessor.passengersByDestination(records);

        assertEquals(3, result.size());
        assertEquals(List.of("Alice"), result.get("LAX"));
        assertEquals(List.of("Bob"), result.get("ORD"));
        assertEquals(List.of("Carol"), result.get("MIA"));
    }

    @Test
    void byDestination_singleRecord() {
        var records = List.of("Alice,JFK,LAX,350");
        var result = BookingProcessor.passengersByDestination(records);

        assertEquals(Map.of("LAX", List.of("Alice")), result);
    }

    // ── totalRevenue ──────────────────────────────────────────────────────────

    @Test
    void totalRevenue_basicCase() {
        var records = List.of("Alice,JFK,LAX,350", "Bob,LAX,ORD,200", "Carol,JFK,MIA,500");
        assertEquals(1050, BookingProcessor.totalRevenue(records));
    }

    @Test
    void totalRevenue_singleRecord() {
        assertEquals(350, BookingProcessor.totalRevenue(List.of("Alice,JFK,LAX,350")));
    }

    @Test
    void totalRevenue_allSamePrice() {
        var records = List.of("Alice,JFK,LAX,100", "Bob,LAX,ORD,100", "Carol,JFK,MIA,100");
        assertEquals(300, BookingProcessor.totalRevenue(records));
    }

    @Test
    void totalRevenue_emptyList() {
        assertEquals(0, BookingProcessor.totalRevenue(List.of()));
    }
}
