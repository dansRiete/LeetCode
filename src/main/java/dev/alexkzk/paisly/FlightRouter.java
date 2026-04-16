package dev.alexkzk.paisly;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Paisly coding assessment simulation — Flight Router.
 *
 * <p>You are given a list of direct flight segments. Each segment is a string in the format:
 * <pre>
 *   "ORIGIN->DESTINATION"
 * </pre>
 * Example: {@code "JFK->LAX"}
 *
 * <p>Each city appears as an origin at most once (i.e. no ambiguous routes).
 * The segments form one or more routes — your job is to reconstruct and work with them.
 */
public class FlightRouter {

    /**
     * Reconstructs the full itinerary as an ordered list of cities, from departure to final destination.
     *
     * <p>The segments may be given in any order. Find the starting city (the one that never
     * appears as a destination) and follow the chain to the end.
     *
     * <pre>
     *   segments = ["LAX->ORD", "JFK->LAX", "ORD->MIA"]
     *   → ["JFK", "LAX", "ORD", "MIA"]
     *
     *   segments = ["A->B"]
     *   → ["A", "B"]
     * </pre>
     *
     * @param segments list of flight segments in any order
     * @return ordered list of cities from start to end
     */
    public static List<String> reconstructItinerary(List<String> segments) {
        Map<String, String> segmentMap = new HashMap<>();
        segments.forEach(seg -> {
                    String[] segParams = seg.split("->");
                    segmentMap.put(segParams[0], segParams[1]);
                });
                String origin = segmentMap.keySet().stream().filter(ap -> !segmentMap.values().contains(ap)).findFirst().orElseThrow();
        List<String> itinerary = new ArrayList<>();
        itinerary.add(origin);
        do {
            String dest = segmentMap.get(origin);
            if(dest != null) {
                itinerary.add(dest);
                origin = dest;
            } else{
                break;
            }
        } while (origin != null);
        return itinerary;

    }

    public static void main(String[] args) {
//        System.out.println(reconstructItinerary(List.of("LAX->ORD", "JFK->LAX", "ORD->MIA")));
        System.out.println(countLayovers(List.of("LAX->ORD", "JFK->LAX", "ORD->MIA")));
    }

    /**
     * Returns the total number of stops (layovers) in the itinerary.
     * A stop is any city that is both a destination and an origin (i.e. not the final destination).
     *
     * <pre>
     *   segments = ["JFK->LAX", "LAX->ORD", "ORD->MIA"]
     *   → 2  (LAX and ORD are layovers)
     *
     *   segments = ["JFK->LAX"]
     *   → 0  (direct flight, no layovers)
     * </pre>
     *
     * @param segments list of flight segments in any order
     * @return number of layover cities
     */
    public static int countLayovers(List<String> segments) {
        Map<String, String> segmentMap = new HashMap<>();
        segments.forEach(seg -> {
                    String[] segParams = seg.split("->");
                    segmentMap.put(segParams[0], segParams[1]);
                });
                Set<String> origins = segmentMap.keySet();
        Set<String> destinations = new HashSet<>(segmentMap.values());
        Set<String> allAirports = Stream.concat(origins.stream(), destinations.stream()).collect(Collectors.toSet());
        int counter = 0;
        for(String airport : allAirports) {
            if(origins.contains(airport) && destinations.contains(airport)) {
                counter++;
            }
        }
        return counter;

    }

    /**
     * Returns true if there is a (direct or connecting) route from {@code origin} to {@code destination}.
     *
     * <pre>
     *   segments = ["JFK->LAX", "LAX->ORD", "ORD->MIA"]
     *   canReach("JFK", "MIA") → true
     *   canReach("LAX", "MIA") → true
     *   canReach("MIA", "JFK") → false  (routes are one-directional)
     *   canReach("JFK", "JFK") → false
     * </pre>
     *
     * @param segments    list of flight segments in any order
     * @param origin      departure city
     * @param destination target city
     * @return true if destination is reachable from origin
     */
    public static boolean canReach(List<String> segments, String origin, String destination) {
        Map<String, String> segmentMap = new HashMap<>();
        segments.forEach(seg -> {
                    String[] segParams = seg.split("->");
                    segmentMap.put(segParams[0], segParams[1]);
                });
                String nextStop = segmentMap.get(origin);
        while(nextStop != null) {
            if(nextStop.equals(destination)) {
                return true;
            }
            nextStop = segmentMap.get(nextStop);
        }
        return false;

    }

    /*public static void main(String[] args) {
        var segments = List.of("LAX->ORD", "JFK->LAX", "ORD->MIA");

        System.out.println(reconstructItinerary(segments));
        // expected: [JFK, LAX, ORD, MIA]

        System.out.println(countLayovers(segments));
        // expected: 2

        System.out.println(canReach(segments, "JFK", "MIA"));  // true
        System.out.println(canReach(segments, "LAX", "MIA"));  // true
        System.out.println(canReach(segments, "MIA", "JFK"));  // false
        System.out.println(canReach(segments, "JFK", "JFK"));  // false
    }*/
}
