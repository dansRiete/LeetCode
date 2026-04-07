package dev.alexkzk.multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread.join() makes the CALLING thread wait until the target thread finishes.
 *
 * Timeline:
 *   main:     ---[start t]---[join: blocked...]---[resumes after t done]--->
 *   thread t:               ---[running...]---[done]
 */
public class JoinDemo {

    // --- Basic usage ---
    static void basicJoin() throws InterruptedException {
        Thread t = new Thread(() -> System.out.println("Worker running..."));

        t.start();
        t.join(); // main blocks here until t finishes

        System.out.println("Worker done, continuing main thread");
    }

    // --- Join with timeout ---
    static void joinWithTimeout() throws InterruptedException {
        Thread t = new Thread(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        t.start();
        t.join(1000); // wait at most 1 second

        System.out.println("Continuing regardless of whether t finished. t alive: " + t.isAlive());
    }

    // --- Production scenario: parallel fetch + aggregation ---
    // Without join(), buildResponse() would run with empty/partial results — a race condition
    // that only surfaces intermittently in production.
    //
    // Sequential:  ~600ms (3 x 200ms)
    // Parallel:    ~200ms (3x faster)
    static void parallelFetch() throws InterruptedException {
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        Thread fetchOrders   = new Thread(() -> results.add(fetchFromOrderService()));
        Thread fetchProfile  = new Thread(() -> results.add(fetchFromProfileService()));
        Thread fetchPayments = new Thread(() -> results.add(fetchFromPaymentService()));

        fetchOrders.start();
        fetchProfile.start();
        fetchPayments.start();

        // Must wait for ALL three before aggregating
        fetchOrders.join();
        fetchProfile.join();
        fetchPayments.join();

        System.out.println("All data fetched: " + results);
        System.out.println("Response: " + buildResponse(results));
    }

    // Simulated service calls (each takes ~200ms in real life)
    private static String fetchFromOrderService()   { return "orders:[{id:1}]"; }
    private static String fetchFromProfileService() { return "profile:{name:Alex}"; }
    private static String fetchFromPaymentService() { return "payments:[{amount:99}]"; }
    private static String buildResponse(List<String> parts) { return String.join(", ", parts); }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Basic Join ===");
        basicJoin();

        System.out.println("\n=== Join With Timeout ===");
        joinWithTimeout();

        System.out.println("\n=== Parallel Fetch (Production Pattern) ===");
        parallelFetch();
    }
}
