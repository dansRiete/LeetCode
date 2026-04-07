package dev.alexkzk.multithreading;

import java.util.LinkedList;
import java.util.Queue;

/**
 * BOUNDED QUEUE — producer/consumer with wait/notifyAll
 * ──────────────────────────────────────────────────────
 *
 * A queue with a fixed capacity. Producers block when full, consumers block
 * when empty. Coordination is done with the object's built-in monitor.
 *
 * THE MONITOR (one per object)
 * ────────────────────────────
 * Every Java object has one built-in lock called a monitor.
 * "synchronized" on a method means: acquire THIS object's monitor before
 * entering, release it on exit. Both put() and take() use the SAME monitor
 * because they belong to the same object instance.
 *
 *   BoundedQueue q = new BoundedQueue();
 *
 *   Thread A → q.put()   acquires q's monitor
 *   Thread B → q.take()  blocked — same monitor, already held by A
 *   Thread C → q.put()   blocked — same monitor, already held by A
 *
 * THE WAIT / NOTIFY CYCLE
 * ────────────────────────
 *
 *   wait()      → atomically: release monitor + park thread
 *   notifyAll() → wake all threads parked in wait() on this monitor
 *
 *   When a waiting thread is notified:
 *     1. Moves from "waiting" to "wants the lock"
 *     2. Competes to reacquire the monitor
 *     3. Once it wins, resumes from the line after wait()
 *
 * WHY while AND NOT if
 * ────────────────────
 *   notifyAll() wakes ALL waiting threads simultaneously.
 *   By the time your thread reacquires the lock, another thread may have
 *   already consumed the item or filled the slot.
 *   The while loop re-checks the condition — if it's still not met, parks again.
 *   Using if instead is a bug: the thread would proceed on a stale assumption.
 *
 * FULL TIMELINE (producer/consumer, limit=1)
 * ───────────────────────────────────────────
 *
 *   [Consumer]  take() → queue empty → wait()
 *                        releases monitor, parked
 *
 *   [Producer]  put("A") → acquires monitor (now free)
 *                          adds "A" → notifyAll()
 *                          releases monitor (exits put)
 *
 *   [Consumer]  woken → reacquires monitor
 *                       while loop: queue not empty → exits loop
 *                       poll() → returns "A"
 *                       notifyAll() → wakes any waiting producers
 *                       releases monitor
 */
public class BoundedQueue {

    private final Queue<String> queue = new LinkedList<>();
    private final int limit;

    public BoundedQueue(int limit) {
        if (limit < 1) throw new IllegalArgumentException("limit must be >= 1");
        this.limit = limit;
    }

    // -----------------------------------------------------------------------
    // put — blocks if full
    // -----------------------------------------------------------------------

    /**
     * Adds an item. Parks the calling thread if the queue is at capacity,
     * until a consumer takes an item and calls notifyAll().
     *
     * synchronized = acquire this object's monitor before entering.
     * wait()       = release monitor + park. Reacquires monitor when notified.
     * notifyAll()  = wake all threads waiting on this object's monitor.
     */
    public synchronized void put(String item) throws InterruptedException {
        // while — not if — because notifyAll wakes ALL threads.
        // By the time we reacquire the lock, another producer may have
        // already filled the slot. Re-check to be sure.
        while (queue.size() == limit) {
            wait();             // queue full: release monitor, park
        }
        queue.add(item);
        notifyAll();            // a slot was just filled — wake waiting consumers
    }

    // -----------------------------------------------------------------------
    // take — blocks if empty
    // -----------------------------------------------------------------------

    /**
     * Removes and returns the head item. Parks the calling thread if the queue
     * is empty, until a producer adds an item and calls notifyAll().
     */
    public synchronized String take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();             // queue empty: release monitor, park
        }
        String item = queue.poll();
        notifyAll();            // a slot just freed — wake waiting producers
        return item;
    }

    // -----------------------------------------------------------------------
    // Inspection (synchronized so reads are consistent)
    // -----------------------------------------------------------------------

    public synchronized int size()      { return queue.size(); }
    public synchronized boolean isEmpty() { return queue.isEmpty(); }
    public synchronized boolean isFull()  { return queue.size() == limit; }
}
