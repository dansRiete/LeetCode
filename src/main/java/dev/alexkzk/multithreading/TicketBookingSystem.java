package dev.alexkzk.multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TICKET BOOKING SYSTEM
 * ──────────────────────
 * Demonstrates LinkedBlockingQueue solving the oversell problem.
 *
 * THE PROBLEM
 * ───────────
 * 100 seats available. 1000 users click "Book" simultaneously.
 * Without coordination: multiple threads read "seats = 1", all think they
 * can book, all write "seats = 0" — but sold 10 tickets for the last seat.
 *
 * THE SOLUTION
 * ────────────
 * Pre-load exactly N seat tokens into a LinkedBlockingQueue at startup.
 * Each booking request calls poll() — takes one token out.
 * If the queue is empty, poll() returns null immediately — request rejected.
 * No token = no seat. Oversell is structurally impossible.
 *
 * WHY LinkedBlockingQueue AND NOT BoundedQueue
 * ─────────────────────────────────────────────
 * LinkedBlockingQueue is the production-grade version of our BoundedQueue.
 * Same wait()/notifyAll() mechanism underneath, but:
 *   - Uses two separate locks: one for put(), one for take()
 *     → producers and consumers never block each other unnecessarily
 *   - Fully thread-safe, battle-tested, no bugs in edge cases
 *   - Supports timeout: poll(1, SECONDS) — wait up to 1s, then give up
 *
 * BoundedQueue (ours)          LinkedBlockingQueue (JDK)
 * ─────────────────────────    ─────────────────────────
 * one monitor for everything   separate head/tail locks
 * put() blocks take()          put() and take() run concurrently
 * educational                  production use
 *
 * STRUCTURE
 * ─────────
 *
 *   [startup]  queue = [S1, S2, S3, ... S100]   ← 100 seat tokens
 *
 *   [user 1]   poll() → takes S1  → booked ✓
 *   [user 2]   poll() → takes S2  → booked ✓
 *   ...
 *   [user 100] poll() → takes S100 → booked ✓
 *   [user 101] poll() → null       → rejected ✗  (queue empty)
 *   [user 102] poll() → null       → rejected ✗
 */
public class TicketBookingSystem {

    public enum BookingResult { BOOKED, SOLD_OUT }

    private final LinkedBlockingQueue<String> availableSeats;
    private final List<String> bookedSeats = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger rejectedCount = new AtomicInteger();

    /**
     * Pre-loads exactly totalSeats tokens into the queue.
     * This is the capacity guarantee — no more tokens can ever exist.
     */
    public TicketBookingSystem(int totalSeats) {
        availableSeats = new LinkedBlockingQueue<>(totalSeats);
        for (int i = 1; i <= totalSeats; i++) {
            availableSeats.offer("SEAT-" + i);    // pre-load all seat tokens
        }
    }

    // -----------------------------------------------------------------------
    // Book — try to claim a seat token
    // -----------------------------------------------------------------------

    /**
     * poll() — non-blocking take:
     *   returns a seat token immediately if one is available
     *   returns null immediately if the queue is empty (sold out)
     *
     * This is the key difference from take():
     *   take()  → blocks until a token is available (used in worker pools)
     *   poll()  → returns null immediately if empty (used for bounded resources)
     *
     * We use poll() here because "sold out" is a valid answer to return to the
     * user — we don't want them waiting indefinitely for a seat that may never
     * come back.
     */
    public BookingResult book(String userName) {
        String seat = availableSeats.poll();    // take a token — or null if empty

        if (seat == null) {
            rejectedCount.incrementAndGet();
            return BookingResult.SOLD_OUT;
        }

        bookedSeats.add(userName + ":" + seat);
        return BookingResult.BOOKED;
    }

    /**
     * poll(timeout) variant — wait up to N milliseconds for a seat.
     * Useful for a "retry window": give the user a brief chance in case
     * someone cancels their booking within the timeout.
     */
    public BookingResult bookWithTimeout(String userName, long timeoutMs)
            throws InterruptedException {
        String seat = availableSeats.poll(timeoutMs, TimeUnit.MILLISECONDS);

        if (seat == null) {
            rejectedCount.incrementAndGet();
            return BookingResult.SOLD_OUT;
        }

        bookedSeats.add(userName + ":" + seat);
        return BookingResult.BOOKED;
    }

    /**
     * Cancel — return the seat token back to the queue.
     * put() blocks if the queue is full, but that can't happen here because
     * we only ever return tokens we took out.
     */
    public void cancel(String userName) throws InterruptedException {
        // Find and remove the booking
        String booking = bookedSeats.stream()
                .filter(b -> b.startsWith(userName + ":"))
                .findFirst()
                .orElse(null);

        if (booking != null) {
            bookedSeats.remove(booking);
            String seat = booking.split(":")[1];
            availableSeats.put(seat);    // return token to queue
        }
    }

    // -----------------------------------------------------------------------
    // Inspection
    // -----------------------------------------------------------------------

    public int seatsRemaining()  { return availableSeats.size(); }
    public int seatsBooked()     { return bookedSeats.size(); }
    public int rejectedCount()   { return rejectedCount.get(); }
    public List<String> bookedSeats() { return Collections.unmodifiableList(bookedSeats); }
}
