# Java Multithreading Interview Questions

## Question 1: What specifically breaks if you use a plain HashMap concurrently instead of a thread-safe alternative?

### Answer
- **Lost Updates (Check-then-act):** Multiple threads attempting to insert or update entries can overwrite each other's changes if they compute the same hash bucket concurrently.
- **Corrupted Data Structures / Infinite Loops:** Prior to Java 8, concurrent resizing operations could create circular linked lists in a bucket, causing `get()` operations to hang in an infinite loop. In Java 8+, while infinite loops are less likely due to treeification, the tree/list structure can still be corrupted, resulting in lost elements or incorrect traversal.
- **Stale Reads / Memory Visibility:** Without proper synchronization or memory barriers, one thread might not see the updates or structural modifications (like rehashing) made by another thread, leading to `NullPointerException`s, unexpected `null` returns, or `ConcurrentModificationException`s during iteration.

---

## Question 2: When are the only times you would choose Collections.synchronizedMap() over ConcurrentHashMap?

### Answer
- **Strict Data Consistency / Atomic Compound Operations on the Entire Map:** When you need to lock the entire map to perform complex check-then-act operations (like iterating over the whole map to derive a value without the map changing, or acquiring a global lock). `ConcurrentHashMap` uses lock striping / fine-grained locking, so locking the entire map is not possible.
- **Specific Map Implementations:** If you need specific behaviors provided by other maps, such as `LinkedHashMap` for predictable insertion or access order.
- **Null Keys/Values:** `ConcurrentHashMap` does not allow `null` keys or `null` values. If your domain strictly requires them, `Collections.synchronizedMap(new HashMap<>())` supports them (though rewriting to avoid `null` is usually preferable).

---

## Question 3: Is size() reliable on ConcurrentHashMap?

### Answer
No, `size()` on `ConcurrentHashMap` is not strongly consistent or strictly reliable for synchronization purposes.

**Reason:** `ConcurrentHashMap` is designed for high concurrency, meaning modifications can happen while `size()` is being calculated. It estimates the size by aggregating values from internal base counters and cells (Striped64 mechanism) to avoid global contention. By the time `size()` returns, the actual number of elements may have already changed.

*Note:* In Java 8+, `mappingCount()` is recommended over `size()` as it returns a `long`, which is necessary because the map can hold more than `Integer.MAX_VALUE` elements.

---

## Question 4: What exactly is CAS (Compare-And-Swap), and how does it work mechanically?

### Answer
**Definition:** CAS is an optimistic concurrency control mechanism used to achieve synchronization without using traditional locks (lock-free).

**Mechanism:** It operates atomically at the hardware level (e.g., using the `CMPXCHG` instruction on x86). It takes three operands: a memory location `V`, an expected value `A`, and a new value `B`.

**Execution:**
1. It reads the current value from memory `V`.
2. It compares it to the expected value `A`.
3. If `V == A`, it updates `V` to `B` and returns true.
4. If `V != A`, it does nothing and returns false.

**Usage:** In Java, CAS is heavily used in `java.util.concurrent.atomic` classes (like `AtomicInteger`). It is typically wrapped in a retry loop (spin-loop) that re-reads `V`, recalculates `B`, and retries the CAS operation if it fails until it succeeds.

---

## Question 5: What is the primary difference between thenApply() and thenAccept() in a CompletableFuture chain?

### Answer
- **`thenApply()`:** Takes a `Function<T, R>`. It receives the result of the previous stage, processes it, and **returns a new value**. This creates a `CompletableFuture<R>` representing the new result, allowing you to chain further data transformations.
- **`thenAccept()`:** Takes a `Consumer<T>`. It receives the result of the previous stage, processes it, but **returns nothing** (`void`). It creates a `CompletableFuture<Void>`, typically used at the end of a chain for side effects (like logging or saving to a database).

---

## Question 6: What is the difference between thenApply() and thenCompose()?

### Answer
- **`thenApply()`:** Used for synchronous mapping. It takes a `Function<T, R>` that returns a raw value `R`. If the function itself returns a `CompletableFuture<R>`, `thenApply()` will return a nested future: `CompletableFuture<CompletableFuture<R>>`.
- **`thenCompose()`:** Used for asynchronous mapping (similar to `flatMap` in Streams). It takes a `Function<T, CompletableFuture<R>>`. Instead of nesting futures, it **flattens** them, returning a single `CompletableFuture<R>`. You use `thenCompose()` when the operation you want to chain inherently returns another `CompletableFuture`.

---

## Question 7: Imagine you are building a high-throughput microservice that uses CompletableFuture to make parallel downstream API calls. If you use the default ForkJoinPool, what issues might you encounter under heavy load, and how would you fix it?

### Answer
**Issues:**
- The default `ForkJoinPool.commonPool()` is shared across the entire JVM. Its size is typically equal to `Runtime.getRuntime().availableProcessors() - 1`.
- If you use it for **blocking I/O operations** (like downstream API calls), the threads in the common pool will quickly become blocked waiting for network responses.
- This leads to **Thread Starvation**, bringing down other parallel streams or asynchronous tasks in the application that rely on the common pool.

**Fix:**
- Provide a dedicated, custom `ExecutorService` (like a `ThreadPoolExecutor`) specifically tuned for blocking I/O tasks.
- Pass this executor as the second argument to `CompletableFuture` methods (e.g., `supplyAsync(task, customExecutor)` or `thenApplyAsync(task, customExecutor)`).
- Tune the custom thread pool size appropriately based on the expected I/O latency and throughput.

---

## Question 8: In a complex CompletableFuture chain orchestrating several external calls, how do you handle failures gracefully? For example, if one downstream service fails, how do you provide a fallback value so the rest of the chain can continue?

### Answer
You handle failures gracefully using methods like `exceptionally()` or `handle()`.

- **`exceptionally(Function<Throwable, T> fallback)`:** This acts like a catch block. It only executes if the pipeline completed exceptionally. It allows you to log the error and return a default/fallback value of the expected type `T`, effectively recovering the pipeline so subsequent stages can continue normally.
- **`handle(BiFunction<T, Throwable, R> handler)`:** This acts like a finally block. It executes regardless of whether the previous stage completed successfully or exceptionally. You must check if the `Throwable` is null and branch your logic to either process the success value or return a fallback for the error.

```java
CompletableFuture.supplyAsync(() -> callUnreliableService())
    .exceptionally(ex -> {
        log.error("Service failed", ex);
        return "Fallback Default"; 
    })
    .thenAccept(result -> process(result));
```

---

## Question 9: In a high-throughput service, how would you choose between an AtomicInteger and a synchronized counter?

### Answer
- **`AtomicInteger`:** Uses lock-free, hardware-level Compare-And-Swap (CAS) operations.
  - **Pros:** Highly performant under low to moderate contention. Non-blocking, avoids context switching and thread suspension overhead.
  - **Cons:** Under extremely high contention, CAS spin-loops can consume excessive CPU cycles (busy-waiting).
- **`synchronized` counter:** Uses intrinsic locks (monitors).
  - **Pros:** Conceptually simple. Good for complex compound actions involving multiple variables where CAS is impossible.
  - **Cons:** High overhead. Thread blocking, unblocking, and context switching make it much slower than atomics for simple counters under most loads.

**Conclusion:** For a simple counter, **always prefer `AtomicInteger`** (or `LongAdder` for extreme throughput). Only use `synchronized` if the counter increment is part of a larger, multi-step critical section that must be mutually exclusive.

---

## Question 10: What is a LongAdder and how does it prevent contention compared to an AtomicInteger?

### Answer
- **Problem with `AtomicInteger`:** Under high concurrency, many threads trying to update the same memory location via CAS will cause frequent failures and retries, leading to CPU cache invalidation overhead (false sharing/cache ping-pong).
- **`LongAdder` Mechanism:** It prevents contention by distributing the counting across an array of variables (cells) rather than a single memory location. 
  - When threads attempt to add concurrently, they hash to different cells in the array and update their respective cell independently. 
  - This eliminates the CAS contention.
- **Reading the value:** When `sum()` is called, it aggregates the base value and all the individual cell values.
- **Trade-off:** `LongAdder` provides significantly higher throughput for high-contention write-heavy scenarios at the cost of slightly slower reads (since `sum()` must iterate over the cells) and slightly higher memory usage.

---

## Question 11: Imagine we have a high-throughput module in Ignition that suddenly starts hanging and throughput drops to zero. You suspect a deadlock or thread starvation. Walk me through exactly how you would use JVM tools in production to generate thread dumps and what specific thread states or patterns you would look for in those dumps to confirm your diagnosis.

### Answer
**Generating Thread Dumps:**
1. Use `jcmd <pid> Thread.print` (preferred modern approach).
2. Use `jstack <pid>` for older JDKs.
3. Send a `SIGQUIT` (`kill -3 <pid>`) on Unix/Linux to dump to standard out.
4. Use APM tools (e.g., Datadog, New Relic) or Java Flight Recorder (JFR) if running.

**What to look for (Diagnosis):**
- **Deadlock:** The JVM explicitly detects monitor deadlocks and typically prints "Found one Java-level deadlock" at the bottom of the dump. You will see threads in `BLOCKED (on object monitor)` state, mutually waiting for locks held by each other. Look for "waiting to lock <0x...>" and "locked <0x...>".
- **Thread Starvation / Pool Exhaustion:** Look for a massive number of worker threads (e.g., Tomcat `http-nio` threads or custom Executor threads) stuck in `WAITING (parking)` or `TIMED_WAITING` states. They might be piled up waiting on a database connection pool lock, downstream HTTP read timeouts, or an empty queue.
- **High CPU Spin:** Look for threads in `RUNNABLE` state executing exactly the same application code line across multiple dumps, indicating an infinite loop or excessive GC activity.

---

## Question 12: How does the work-stealing algorithm in ForkJoinPool work, and in what scenarios is ForkJoinPool more efficient than a standard ThreadPoolExecutor?

### Answer
**Work-Stealing Algorithm:**
- In a `ForkJoinPool`, each worker thread has its own double-ended queue (deque) of tasks. 
- When a task spawns (forks) subtasks, they are pushed onto the head of the worker's own deque. The worker processes tasks from the head (LIFO order), minimizing contention.
- If a worker finishes its queue and becomes idle, it "steals" tasks from the **tail** (FIFO order) of another busy worker's deque. 
- Stealing from the tail reduces synchronization overhead with the owner thread (working at the head) and usually steals larger chunks of work (older tasks).

**When is it more efficient?**
- **Divide-and-Conquer Algorithms:** Recursive tasks that break down into smaller subtasks (e.g., parallel merge sort, matrix multiplication, recursive directory traversal).
- **Highly unbalanced workloads:** Where task durations are unpredictable. Work-stealing ensures CPU cores stay busy as idle threads actively look for work, unlike `ThreadPoolExecutor` where threads wait on a single shared blocking queue.

---

## Question 13: What is backpressure in reactive programming or message processing, and why is it important?

### Answer
**Definition:** Backpressure is a feedback mechanism where a downstream consumer signals an upstream producer to slow down the rate of data emission because the consumer cannot process the data fast enough.

**Why it's important:**
- **Resource Protection:** Without backpressure, a fast producer overwhelms a slow consumer. This leads to unbounded in-memory queues (Out Of Memory errors), increased latency due to garbage collection pressure, and eventual system crash.
- **System Stability:** It allows systems to degrade gracefully under load rather than failing catastrophically.

**Handling Methods:**
- **Buffering/Dropping:** Buffer up to a limit, then drop new messages (lossy).
- **Blocking:** The producer thread blocks until the consumer frees up space (standard `BlockingQueue` behavior).
- **Reactive Pull:** (e.g., Java 9 Flow API, Project Reactor). The consumer explicitly requests `n` items (`request(n)`). The producer only emits up to `n` items.

---

## Question 14: Explain Java's ReentrantLock. How does it differ from the 'synchronized' keyword, and when would you prefer to use it?

### Answer
**Definition:** `ReentrantLock` is an explicit, advanced lock implementation in `java.util.concurrent.locks`. It provides the same mutual exclusion and memory visibility semantics as `synchronized`, and is "reentrant" (a thread can acquire the lock multiple times without deadlocking itself).

**Differences from `synchronized`:**
- **Explicit API:** Requires explicit `lock()` and `unlock()` calls (must use a `try-finally` block to ensure release).
- **Fairness:** Can be configured to be "fair" (granting access to the longest waiting thread). `synchronized` is always unfair.
- **Interruptibility:** Provides `lockInterruptibly()`, allowing a thread waiting for a lock to be interrupted.
- **Timeouts:** Provides `tryLock(time, unit)`, allowing a thread to give up if the lock isn't acquired within a timeframe.
- **Multiple Condition Variables:** Supports multiple `Condition` objects per lock for finer-grained wait/notify logic.

**When to prefer it:** Use `ReentrantLock` only when you need its advanced features: fair locking, interruptible waiting, timed waiting, or multiple conditions. Otherwise, prefer `synchronized` for its simplicity, readability, and automatic lock release.

---

## Question 15: Explain the 'synchronized' keyword in Java. How does it ensure thread safety, and what are its limitations?

### Answer
**How it ensures thread safety:** 
- **Mutual Exclusion:** It uses the intrinsic lock (monitor) of a Java object. When a thread enters a `synchronized` block or method, it acquires the monitor. Any other thread trying to acquire the same monitor is blocked, preventing race conditions on shared state.
- **Memory Visibility:** It establishes a happens-before relationship. When a thread exits a synchronized block, it flushes its local CPU cache to main memory. When a thread enters, it invalidates its local cache and reads fresh data, ensuring visibility of updates.

**Limitations:**
- **Coarse-grained / Performance:** Uncontended synchronized blocks are fast, but high contention leads to heavy context switching and OS-level thread suspension.
- **No Timeout / Uninterruptible:** A thread waiting for a synchronized lock cannot be interrupted and cannot time out. If the lock owner hangs, the waiting threads hang forever, easily causing deadlocks.
- **Inflexible Structure:** Locking and unlocking must occur within the exact same lexical block (can't lock in one method and unlock in another).

---

## Question 16: What is Java's ThreadLocal class? When and why would you use it, and what are its potential pitfalls?

### Answer
**Definition:** `ThreadLocal` provides variables that are localized to the current thread. Each thread that accesses the variable has its own independently initialized copy.

**When and Why to use it:**
- **Thread Safety without Synchronization:** To make stateful, non-thread-safe objects safe by giving each thread its own instance (e.g., `SimpleDateFormat`, database connections, or transaction contexts).
- **Per-Thread Context:** To pass contextual data (like user IDs, correlation IDs for logging/MDC) deep down the call stack without explicitly passing it as method parameters.

**Pitfalls:**
- **Memory Leaks:** `ThreadLocal` values are stored in a map tied to the `Thread` object. In application servers where threads are pooled and reused, a `ThreadLocal` variable not explicitly removed (`remove()`) can survive across requests, holding onto heavy objects (like ClassLoaders) and causing severe memory leaks. Always call `ThreadLocal.remove()` in a `finally` block when done.

---

## Question 17: Explain the purpose of the 'volatile' keyword in Java. How does it ensure memory visibility, and when is it insufficient for thread safety?

### Answer
**Purpose & Memory Visibility:** The `volatile` keyword ensures that updates to a variable are predictably visible to other threads. It forces the JVM and CPU to read the variable directly from main memory rather than a local CPU cache, and to flush writes directly to main memory. It also establishes a "happens-before" barrier, preventing the compiler and CPU from reordering instructions across the volatile read/write.

**When it is insufficient:** 
- `volatile` **does not provide atomicity** for compound operations. 
- If you perform a read-modify-write operation like `count++` (which is actually read, add, write), `volatile` will not prevent lost updates if multiple threads do this concurrently. 
- For compound operations, you must use `synchronized`, locks, or `Atomic` classes (like `AtomicInteger`). `volatile` is typically only sufficient for simple flags (e.g., `boolean isRunning = false;`).

---

## Question 18: Explain the difference between Thread.sleep() and Object.wait() in Java. When would you use each?

### Answer
- **`Thread.sleep(long millis)`:**
  - Belongs to the `Thread` class.
  - **Lock Behavior:** It **does not release any locks** the thread currently holds.
  - **Usage:** Used for pacing, pausing execution for a specific duration, or simple polling loops.

- **`Object.wait()`:**
  - Belongs to the `Object` class. Must be called from within a `synchronized` block on that specific object.
  - **Lock Behavior:** It **releases the monitor lock** of the object it is called on, allowing other threads to enter synchronized blocks on that object.
  - **Wake up:** The thread sleeps until another thread calls `notify()` or `notifyAll()` on that same object (or a timeout expires).
  - **Usage:** Used for thread communication and condition signaling (e.g., producer-consumer patterns, waiting for a queue to become non-empty).

---

## Question 19: Explain the concept of an ExecutorService and thread pools in Java. What are their benefits, and how do you choose an appropriate pool type?

### Answer
**Concept:** `ExecutorService` is an interface that decouples task submission from task execution. Instead of manually creating `new Thread(task).start()`, you submit `Runnable` or `Callable` tasks to the service, which manages a pool of worker threads to execute them.

**Benefits:**
- **Thread Reuse:** Creating and destroying OS threads is expensive. Pools reuse existing threads, drastically reducing latency.
- **Resource Management:** Prevents the system from crashing under load by strictly limiting the maximum number of concurrent threads and queuing excess tasks.
- **Lifecycle Management:** Provides hooks to shut down gracefully and await termination.

**Choosing a Pool Type (`Executors` factory):**
- **`FixedThreadPool(n)`:** Use when you have a known, predictable load and want to strictly cap resource usage (e.g., database operations).
- **`CachedThreadPool()`:** Creates new threads as needed and caches idle ones. Good for many short-lived, asynchronous, lightweight tasks. Dangerous under heavy load as it can create unbounded threads.
- **`ScheduledThreadPool()`:** Use for periodic or delayed task execution (cron-like jobs).
- **`SingleThreadExecutor()`:** Ensures tasks execute sequentially in order of submission without concurrency issues.

---

