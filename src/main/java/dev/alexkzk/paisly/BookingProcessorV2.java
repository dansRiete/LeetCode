package dev.alexkzk.paisly;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * V2: processes records as a stream without materializing an intermediate list.
 *
 * <p>Key difference from V1: each method parses and processes in a single pipeline pass.
 * No intermediate {@code List<Flight>} is created — records are parsed and consumed on the fly,
 * which is important when input is large (e.g. streaming from a file or network).
 *
 * <p>Trade-off: if you need to call all three methods on the same input, you still parse
 * three times. The next step would be to accept a {@code Stream<String>} from the caller
 * and let the caller control the source (file, network, in-memory).
 */
public class BookingProcessorV2 {

    record Flight(String name, String origin, String destination, int price) {}

    private static Flight parse(String record) {
        String[] parts = record.split(",");
        return new Flight(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
    }

    private static Stream<Flight> toFlights(List<String> records) {
        return records.stream()
                .map(r -> {
                    try {
                        return parse(r);
                    } catch (Exception e) {
                        // log in production
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }

    /**
     * Single-pass max — no intermediate list, O(n) time, O(1) extra space.
     */
    public static String mostExpensivePassenger(List<String> records) {
        if (records == null || records.isEmpty()) return null;
        return toFlights(records)
                .max(Comparator.comparingInt(Flight::price))
                .map(Flight::name)
                .orElse(null);
    }

    /**
     * Groups in a single pass using {@code Collectors.groupingBy}.
     * The map is unavoidable (we need all groups), but no intermediate flight list is created.
     */
    public static Map<String, List<String>> passengersByDestination(List<String> records) {
        if (records == null || records.isEmpty()) return Map.of();
        return toFlights(records)
                .collect(Collectors.groupingBy(
                        Flight::destination,
                        Collectors.mapping(Flight::name, Collectors.toList())
                ));
    }

    /**
     * Sums prices in a single pass — no intermediate list, O(n) time, O(1) extra space.
     */
    public static int totalRevenue(List<String> records) {
        if (records == null || records.isEmpty()) return 0;
        return toFlights(records)
                .mapToInt(Flight::price)
                .sum();
    }
}
