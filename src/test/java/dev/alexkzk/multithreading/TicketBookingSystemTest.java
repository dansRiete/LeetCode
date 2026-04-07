package dev.alexkzk.multithreading;

import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TicketBookingSystemTest {

    // -----------------------------------------------------------------------
    // Basic correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("booking succeeds when seats are available")
    void bookingSucceeds() {
        TicketBookingSystem system = new TicketBookingSystem(5);
        assertEquals(TicketBookingSystem.BookingResult.BOOKED, system.book("Alice"));
        assertEquals(4, system.seatsRemaining());
        assertEquals(1, system.seatsBooked());
    }

    @Test
    @DisplayName("booking fails immediately when sold out")
    void bookingFailsWhenSoldOut() {
        TicketBookingSystem system = new TicketBookingSystem(2);
        system.book("Alice");
        system.book("Bob");

        assertEquals(TicketBookingSystem.BookingResult.SOLD_OUT, system.book("Charlie"));
        assertEquals(0, system.seatsRemaining());
        assertEquals(2, system.seatsBooked());       // only 2 booked, not 3
        assertEquals(1, system.rejectedCount());
    }

    @Test
    @DisplayName("cancel returns seat to pool — next user can book it")
    void cancelReturnsSeatToPool() throws InterruptedException {
        TicketBookingSystem system = new TicketBookingSystem(1);
        system.book("Alice");
        assertEquals(0, system.seatsRemaining());

        system.cancel("Alice");
        assertEquals(1, system.seatsRemaining());

        assertEquals(TicketBookingSystem.BookingResult.BOOKED, system.book("Bob"));
    }

    // -----------------------------------------------------------------------
    // THE CORE TEST: oversell is impossible under concurrency
    // -----------------------------------------------------------------------

    /**
     * 100 seats, 1000 concurrent users.
     *
     * Every user fires simultaneously via a CountDownLatch.
     * After all finish:
     *   - exactly 100 bookings must exist
     *   - exactly 900 rejections must exist
     *   - 100 + 900 = 1000 (no request lost)
     *   - queue is empty
     *
     * If oversell were possible, bookedCount would exceed 100.
     */
    @Test
    @DisplayName("exactly 100 seats booked out of 1000 concurrent requests — no oversell")
    void noOversellUnderConcurrency() throws InterruptedException {
        int seats = 100;
        int users  = 1000;

        TicketBookingSystem system = new TicketBookingSystem(seats);
        AtomicInteger booked   = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        CountDownLatch ready = new CountDownLatch(users);
        CountDownLatch go    = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(users);

        ExecutorService pool = Executors.newFixedThreadPool(50);

        for (int i = 0; i < users; i++) {
            final String user = "user-" + i;
            pool.submit(() -> {
                ready.countDown();
                try {
                    go.await();    // all threads wait here — then released simultaneously
                    TicketBookingSystem.BookingResult result = system.book(user);
                    if (result == TicketBookingSystem.BookingResult.BOOKED) booked.incrementAndGet();
                    else rejected.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();  // always counted down — even if task throws
                }
            });
        }

        ready.await();   // wait until all threads are lined up
        go.countDown();  // release all 1000 simultaneously — maximum contention
        assertTrue(done.await(10, TimeUnit.SECONDS), "Must finish in 10s — no deadlock");
        pool.shutdown();

        System.out.printf("  Booked: %d | Rejected: %d | Total: %d%n",
                booked.get(), rejected.get(), booked.get() + rejected.get());

        assertEquals(seats, booked.get(),          "Must book exactly " + seats + " seats — no oversell");
        assertEquals(users - seats, rejected.get(),"Must reject exactly " + (users - seats) + " requests");
        assertEquals(users, booked.get() + rejected.get(), "Every request must get a result");
        assertEquals(0, system.seatsRemaining(),   "Queue must be empty");
    }

    // -----------------------------------------------------------------------
    // poll() vs take() — non-blocking vs blocking
    // -----------------------------------------------------------------------

    /**
     * poll() returns null immediately on an empty queue.
     * The thread must NOT block — it must return within milliseconds.
     */
    @Test
    @DisplayName("poll() returns SOLD_OUT immediately — does not block")
    void pollDoesNotBlock() {
        TicketBookingSystem system = new TicketBookingSystem(1);
        system.book("Alice");   // take the only seat

        long start = System.nanoTime();
        TicketBookingSystem.BookingResult result = system.book("Bob");  // queue empty
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertEquals(TicketBookingSystem.BookingResult.SOLD_OUT, result);
        assertTrue(elapsedMs < 50, "poll() must return immediately, took " + elapsedMs + "ms");
    }

    /**
     * bookWithTimeout() waits up to N ms for a cancellation to free a seat.
     * We cancel an existing booking mid-wait — the waiting user should get it.
     */
    @Test
    @DisplayName("bookWithTimeout() gets a seat when a cancellation happens within the window")
    void timeoutBookingGetsReleasedSeat() throws InterruptedException {
        TicketBookingSystem system = new TicketBookingSystem(1);
        system.book("Alice");   // only seat taken

        CountDownLatch waitingStarted = new CountDownLatch(1);
        AtomicInteger bobResult = new AtomicInteger(-1); // 0=sold_out 1=booked

        // Bob waits up to 500ms for a seat
        Thread bob = new Thread(() -> {
            waitingStarted.countDown();
            try {
                TicketBookingSystem.BookingResult r =
                        system.bookWithTimeout("Bob", 500);
                bobResult.set(r == TicketBookingSystem.BookingResult.BOOKED ? 1 : 0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        bob.start();
        waitingStarted.await();
        Thread.sleep(50);              // let Bob enter poll(timeout)

        system.cancel("Alice");        // Alice cancels — seat token returned to queue
        bob.join(1000);

        assertEquals(1, bobResult.get(), "Bob must get the seat after Alice cancels");
        assertEquals(1, system.seatsBooked());
        assertEquals(0, system.seatsRemaining());
    }
}
