# Weak Areas — Master Recap

All questions answered "not sure" or incorrectly across all sessions. Study these before your interview.

---

## gRPC (HIGHEST PRIORITY — on your resume)

**gRPC vs REST:**
- gRPC uses HTTP/2, REST uses HTTP/1.1
- gRPC uses Protobuf (binary), REST uses JSON (text)
- Browsers cannot call gRPC directly — needs gRPC-Web proxy
- gRPC: strongly typed contract (.proto), code generated, streaming built-in
- REST: human-readable, browser-compatible, simpler debugging

**4 gRPC patterns:**
```
Unary              → one request, one response (like REST)
Server streaming   → one request, many responses (server pushes)
Client streaming   → many requests, one response (client pushes bulk)
Bidirectional      → both stream simultaneously
```

**Protobuf vs JSON:**
- Binary, 3-10x smaller, much faster to parse
- No field names on wire — uses field numbers only
- Renaming a field = safe (wire uses numbers not names)
- Adding a new field = safe (old clients ignore unknown numbers)
- Reusing a field number = NOT safe (old clients misinterpret)
- Changing field type = NOT safe (corrupts deserialization)
- Always `reserved` removed field numbers: `reserved 3; reserved "price";`

**gRPC deadlines vs HTTP timeouts:**
- HTTP timeout = relative duration, not propagated — server keeps running after client gives up
- gRPC deadline = absolute point in time, propagated across entire call chain
- Set deadline on client: `stub.withDeadlineAfter(5, TimeUnit.SECONDS)`
- Always set deadlines — default is wait forever

**gRPC authentication — interceptors + Metadata:**
- Metadata = HTTP headers equivalent
- Interceptor = HTTP middleware equivalent
- Attach once to channel, applies to all calls:
```java
ManagedChannel channel = ManagedChannelBuilder
    .forAddress("partner.com", 443)
    .intercept(authInterceptor) // adds Bearer token to every call
    .build();
```

**Client disconnects during server stream:**
- Server does NOT stop automatically — must check `Context.current().isCancelled()`
- Without check: server wastes resources streaming to nobody
```java
for (Flight f : flights) {
    if (Context.current().isCancelled()) { observer.onError(...); return; }
    observer.onNext(buildResponse(f));
}
```

**Protobuf versioning:**
- Safe: add field, rename field, remove + reserve field, add RPC method
- Not safe: reuse field number, change field type, remove without reserving
- Breaking changes: use package versioning `integration.flights.v2`

---

## Core Java

**HashMap — load factor and rehashing:**
- Default capacity: 16 buckets, load factor: 0.75
- Rehash threshold: 16 × 0.75 = 12 entries → doubles to 32 buckets, redistributes all
- Bucket: empty → linked list (1-7 entries, O(n)) → red-black tree (8+, O(log n)) → back to list at 6
- Must override both `equals()` AND `hashCode()` on keys — equal objects must hash to same bucket

**synchronized limitations (missed in previous session):**
- Waiting threads CANNOT be interrupted — biggest limitation
- No timeout on lock acquisition — waits forever
- Fix for both: use `ReentrantLock.lockInterruptibly()` or `tryLock(5, TimeUnit.SECONDS)`

**ThreadLocal memory leak (missed in previous session):**
- Thread pool reuses threads — ThreadLocal values persist between requests
- Always `remove()` in finally block:
```java
try { threadLocal.set(value); processRequest(); }
finally { threadLocal.remove(); } // CRITICAL
```

---

## GC

**GC roots** — starting points for reachability tracing:
- Local variables on active thread stacks
- Static fields of loaded classes
- Active threads themselves
- JNI references (native code)

**Memory leak examples:**
- Unbounded static collection: `static List<byte[]> cache` — never removed → OOM
- Listener never unregistered
- ThreadLocal not cleaned up in thread pool

**GC logging flags:**
```
-Xlog:gc*:file=gc.log:time,uptime,level   (Java 9+)
-XX:+PrintGCDetails -Xloggc:gc.log        (Java 8)
-XX:+HeapDumpOnOutOfMemoryError
```

**Minor vs Full GC:**
- Minor GC — Young Gen only, fast (<10ms)
- Full GC — entire heap + compaction, slow (seconds)
- Full GC triggers: Old Gen full, `System.gc()`, Metaspace full

**Reference types:**
```
Strong  — never collected              → default
Soft    — JVM low on memory            → memory-sensitive cache
Weak    — next GC, always              → listener registries
Phantom — after finalization           → cleanup
```
WeakHashMap — entries disappear unpredictably. Use Caffeine/Guava instead.

**G1GC vs Parallel GC:**
- Parallel GC — max throughput, unpredictable long pauses (5-10s)
- G1GC — heap split into ~2MB regions, collects most-garbage-first, `-XX:MaxGCPauseMillis=200`
- Default since Java 9. Evolution: Parallel → G1GC → ZGC/Shenandoah (sub-ms)

---

## Concurrency

**CountDownLatch** — one-time gate, blocks until count = 0:
```java
CountDownLatch latch = new CountDownLatch(3);
executor.submit(() -> { doWork(); latch.countDown(); });
latch.await(5, TimeUnit.SECONDS);
```

**Semaphore** — N threads allowed simultaneously:
```java
Semaphore sem = new Semaphore(3);
sem.acquire();
try { partnerApi.call(); } finally { sem.release(); }
```
Use for: rate limiting, throttling concurrent partner calls.

**BlockingQueue** — thread-safe producer/consumer with backpressure:
```java
BlockingQueue<Order> queue = new ArrayBlockingQueue<>(500);
queue.put(order);  // blocks if full
queue.take();      // blocks if empty
```
Used internally by ThreadPoolExecutor.

**CompletableFuture vs Future:**
- Future: blocking `get()` only, no chaining, no callbacks
- CompletableFuture: chainable, non-blocking, composable

**thenApply / thenCompose / thenCombine:**
- `thenApply`   — sync transform (map): `order -> order.getId()`
- `thenCompose` — async chain (flatMap): next step also returns CF
- `thenCombine` — zip two independent futures

**Fan-out pattern:**
```java
List<CompletableFuture<Order>> futures = partnerIds.stream()
    .map(id -> CompletableFuture.supplyAsync(() -> fetchOrder(id), executor))
    .toList();

List<Order> results = CompletableFuture
    .allOf(futures.toArray(new CompletableFuture[0]))
    .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
    .get(10, TimeUnit.SECONDS);
```
Always use custom executor for I/O — never `ForkJoinPool.commonPool()`.

**Exception handling:**
```
exceptionally  — exception only,  can change result  → fallback value
handle         — always called,   can change result  → transform both paths
whenComplete   — always called,   cannot change      → logging, metrics
```

---

## Testing

**Mock vs Stub vs Spy:**
- Stub — returns canned data: `when(x).thenReturn(y)`
- Mock — verifies calls: `verify(x, times(1)).method()`
- Spy — real object, selectively override: `Mockito.spy(realObj)`

**Test Pyramid:** Unit (~70%) → Integration (~20%) → E2E (~10%)
Anti-pattern: Ice Cream Cone — heavy E2E, weak unit tests.

**Async testing:**
```java
// Awaitility (most readable)
await().atMost(5, SECONDS).untilAsserted(() -> verify(repo).save(any()));
```

**Flaky test** — non-deterministic, passes sometimes fails sometimes:
- Timing → Awaitility not sleep
- Shared state → reset in `@BeforeEach`
- `LocalDate.now()` → inject fixed `Clock`

---

## HTTP & Messaging

**REST constraints:** Stateless, Client-Server, Cacheable, Uniform Interface, Layered System, Code on Demand.
Most important: **Stateless** — every request self-contained, no server session.

**HTTP methods:**
```
GET     safe=yes  idempotent=yes
POST    safe=no   idempotent=no
PUT     safe=no   idempotent=yes
DELETE  safe=no   idempotent=yes
PATCH   safe=no   idempotent=no
```
Key codes: 204 No Content, 409 Conflict, 429 Rate Limited, 502 Bad Gateway, 504 Gateway Timeout.

**Webhook security — HMAC:**
```java
// MessageDigest.isEqual() not .equals() — prevents timing attacks
if (!MessageDigest.isEqual(hmacSHA256(secret, body).getBytes(), received.getBytes()))
    throw new SecurityException("Invalid signature");
```
Also: reject requests older than 5 min (replay attack), return 200 immediately + process async.

**JMS Queue vs Topic:**
- Queue — point-to-point, one consumer per message
- Topic — pub/sub, all subscribers get every message

**JMS Ack modes:**
- `AUTO_ACKNOWLEDGE` — auto after receive(), risk of loss
- `CLIENT_ACKNOWLEDGE` — manual `msg.acknowledge()`, safest
- `DUPS_OK_ACKNOWLEDGE` — lazy, duplicates possible

**Kafka vs JMS:** Kafka = immutable log, consumers track offset, can replay. JMS = deleted after consumption.

---

## Design

**SOLID — all 5:**
- S — Single Responsibility: one reason to change
- O — Open/Closed: open for extension, closed for modification (interfaces not if/else)
- L — Liskov Substitution: subtypes honor parent contract (`List.of().add()` violates)
- I — Interface Segregation: don't force unused methods (split fat interfaces)
- D — Dependency Inversion: depend on abstractions not concretions (inject interface, not `new Concrete()`)

---

## AWS

**SQS vs SNS:**
- SQS — queue, pull-based, one consumer, persists 14 days
- SNS — push, fan-out, all subscribers, no persistence

**SQS Standard vs FIFO:**
- Standard — at-least-once, best-effort order, unlimited throughput
- FIFO — exactly-once, guaranteed order, 300 msg/s

**IAM:**
- Policy — JSON permissions document
- User — static credentials (avoid in app code)
- Role — temporary credentials assumed by EC2/Lambda/ECS — always use this

---

## Modern Java

**Virtual Threads (Java 21):** blocked virtual thread unmounts from carrier — carrier keeps working.
Write blocking code, get async throughput. Use `Executors.newVirtualThreadPerTaskExecutor()`.

**Sealed Classes (Java 17):** `sealed class Shape permits Circle, Square {}` — compiler enforces exhaustive switch.

**Pattern matching instanceof (Java 16):** `if (shape instanceof Circle c) { c.radius(); }` — no cast needed.
