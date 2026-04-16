package dev.alexkzk.paisly;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Paisly coding assessment simulation.
 *
 * <p>You are building a connector that receives raw booking records from a partner system.
 * Each record is a comma-separated string in the format:
 * <pre>
 *   "PASSENGER_NAME,ORIGIN,DESTINATION,PRICE"
 * </pre>
 * Example: {@code "Alice,JFK,LAX,350"}
 *
 * <p>Implement the three methods below. Assume input is well-formed (no null/empty lines).
 * Prices are positive integers.
 */
public class BookingProcessor {

    private record Flight(String name, String origin, String destination, int price) {}

    /**
     * Parses raw records and returns the name of the passenger with the most expensive booking.
     *
     * <p>If multiple bookings share the highest price, return any one of their passenger names.
     *
     * <pre>
     *   records = ["Alice,JFK,LAX,350", "Bob,LAX,ORD,200", "Carol,JFK,MIA,500"]
     *   → "Carol"
     * </pre>
     *
     * @param records list of raw booking strings
     * @return passenger name of the most expensive booking
     */
    public static String mostExpensivePassenger(List<String> records) {
        if (records == null || records.isEmpty()) {
            return null;
        }
        List<Flight> flights = records.stream().map(flightString -> {
            try {
                return getFlight(flightString);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toList();

        return flights.stream().max(Comparator.comparing(Flight::price)).map(Flight::name).orElse(null);
    }

    private static Flight getFlight(String flightString) {
        String[] split = flightString.split(",");
        return new Flight(split[0], split[1], split[2], Integer.parseInt(split[3]));
    }

    /**
     * Parses raw records and groups passenger names by their destination.
     *
     * <pre>
     *   records = ["Alice,JFK,LAX,350", "Bob,LAX,LAX,200", "Carol,JFK,MIA,500"]
     *   → { "LAX" → ["Alice", "Bob"], "MIA" → ["Carol"] }
     * </pre>
     *
     * @param records list of raw booking strings
     * @return map of destination → list of passenger names flying there
     */
    public static Map<String, List<String>> passengersByDestination(List<String> records) {
        return records.stream()
                .map(BookingProcessor::getFlight)
                .collect(Collectors.groupingBy(
                        Flight::destination,
                        Collectors.mapping(Flight::name, Collectors.toList())
                ));
    }

    /**
     * Parses raw records and returns the total revenue (sum of all prices).
     *
     * <pre>
     *   records = ["Alice,JFK,LAX,350", "Bob,LAX,ORD,200", "Carol,JFK,MIA,500"]
     *   → 1050
     * </pre>
     *
     * @param records list of raw booking strings
     * @return sum of all booking prices
     */
    public static int totalRevenue(List<String> records) {
        return records.stream().map(BookingProcessor::getFlight).mapToInt(Flight::price).sum();
    }

}
