# Senior Java Integration Engineer — Top 50 Interview Questions

## 1. Core Java

1.1. What is the difference between `HashMap` and `ConcurrentHashMap`?
1.2. How does `HashMap` handle collisions internally?
1.3. What is the difference between `Comparable` and `Comparator`?
1.4. What is the difference between `checked` and `unchecked` exceptions? When do you use each?
1.5. What is the difference between `LinkedList` and `ArrayDeque`? When would you use each?
1.6. Virtual Threads (Java 21), Records, Sealed Classes, Pattern matching instanceof — what each solves and when to use.

---

## 2. Multithreading

2.1. How does `synchronized` work? What are its limitations?
2.2. What is a `ThreadLocal` and when would you use it in an integration context?
2.3. Explain `volatile` — when is it sufficient vs needing `synchronized`?
2.4. What is the difference between `ExecutorService`, `ScheduledExecutorService`, and `ForkJoinPool`?
2.5. How do you handle backpressure when consuming a high-volume external API?
2.6. What is `ReentrantLock` and how does it differ from `synchronized`?
2.7. What is the difference between `Thread.sleep()` and `Object.wait()`?
2.8. What is a deadlock? Describe the four Coffman conditions and one prevention strategy.
2.9. What is CountDownLatch, Semaphore, BlockingQueue, and AtomicInteger? When would you use each?
2.10. CompletableFuture vs Future, thenApply/thenCompose/thenCombine, fan-out with allOf, exception handling with exceptionally/handle/whenComplete.

---

## 3. Streams & Functional Interfaces

3.1. What is the difference between `Optional.orElse()` and `Optional.orElseGet()`?
3.2. What are functional interfaces? Name 5 from `java.util.function`

---

## 4. JVM Internals

4.1. Explain the Java memory model — heap vs stack, young vs old generation
4.2. Can you have a memory leak in Java despite having a GC? What are GC roots? Give a concrete example.
4.3. Users report periodic latency spikes with no errors — how do you diagnose and what are the two most common root causes?
4.4. What is the difference between soft, weak, and phantom references? When would you use WeakReference, and why is WeakHashMap dangerous as a cache?
4.5. What is the difference between Minor GC and Full GC? Which is more dangerous and why? What triggers a Full GC?
4.6. What is G1GC and why did it become the default in Java 9? What problem does it solve vs Parallel GC?

---

## 5. HTTP Integrations & Resilience

5.1. How do you implement retry logic with exponential backoff in plain Java?
5.2. How do you handle timeouts — connection timeout vs read timeout, what's the difference?
5.3. How do you deserialize a JSON response when the schema may change between partner versions?
5.4. How do you handle pagination when consuming a partner REST API?
5.5. How do you secure outbound HTTP calls — OAuth2, API keys, mTLS?
5.18. What are the main HTTP security approaches? (Basic Auth, API Key, OAuth2, mTLS, JWT, HMAC — when to use each)
5.6. What is idempotency and why does it matter for integration connectors?
5.7. How do you handle partial failures — partner returns 200 but with error fields in the body?
5.8. How would you design a connector that needs to fan out to 5 partner APIs and aggregate results?
5.9. Explain the Circuit Breaker pattern — states, transitions, and when to use it
5.10. What is the Bulkhead pattern and why is it important for integrations?
5.11. How do you implement a dead letter queue in plain Java without a framework?
5.12. How do you distinguish between a transient failure (retry) and a permanent failure (fail fast)?
5.13. How do you handle a partner API that is rate-limited (429 responses)?
5.14. What is a connection pool, what happens when exhausted, what is a connection leak, HTTP keep-alive, HTTP/1.1 vs HTTP/2, connection vs read timeout scenario.
5.15. What is HTTP polling, its drawbacks, alternatives (webhooks/SSE/WebSocket), and webhook security with HMAC signatures.
5.16. HTTP methods (safe vs idempotent), PUT vs PATCH, status code categories, 401 vs 403, REST constraints.
5.17. JMS Queue vs Topic, acknowledgement modes, persistent vs non-persistent messages, JMS vs Kafka.

---

## 6. gRPC

6.1. What is the difference between gRPC and REST? When would you choose one over the other?
6.2. What are the 4 types of gRPC communication patterns?
6.3. How does Protobuf differ from JSON — serialization size, schema evolution, backward compatibility?
6.4. How do you handle gRPC deadlines/timeouts vs HTTP timeouts?
6.5. How do you add authentication to a gRPC call (interceptors)?
6.6. What happens when a gRPC server streams data and the client disconnects?
6.7. How do you version a Protobuf schema without breaking existing clients?

---

## 7. Testing

7.1. What is the difference between a unit test and an integration test? Where do you draw the line?
7.2. How do you test a class that makes HTTP calls without hitting the real endpoint?
7.3. How do you perform load testing — tools, metrics you watch, what defines a pass/fail?
7.4. What is a contract test (e.g. Pact)? Why is it useful for partner integrations?
7.5. How do you measure test coverage and what coverage percentage is "good enough"?
7.6. Mock vs stub vs spy, test pyramid, testing async code (CountDownLatch/Awaitility), flaky tests and their causes.

---

## 8. Google Cloud Platform

8.1. What GCP services would you use to build an async integration pipeline?
8.2. How does Cloud Pub/Sub guarantee at-least-once delivery — how do you handle duplicate messages?
8.3. What is the difference between Cloud Run and GKE for deploying a microservice?
8.4. How do you manage secrets (API keys, credentials) in GCP?
8.5. How would you use Cloud Monitoring/Logging to alert on integration failures?
8.6. What is the difference between SQS and SNS? SQS Standard vs FIFO? IAM user vs role vs policy?

---

## 9. Design & Architecture

9.1. How do you design a connector that needs to support multiple versions of a partner API simultaneously?
9.2. What is the Adapter pattern? How does it apply to integration connectors?
9.3. How do you approach deprecating and retiring an old integration?
9.4. How do you design for observability in an integration service — what do you log, what do you metric?
9.5. How would you onboard a new business partner — what's your process from contract to production?
9.6. Name all 5 SOLID principles. Give concrete violation examples for LSP, OCP, and ISP.

---

## 10. Behavioral / Leadership

10.1. Tell me about a time an integration you owned went down in production. What happened, how did you respond?
10.2. How do you push back on a product requirement that you believe is technically risky?
10.3. How do you mentor a junior engineer who is struggling with a complex integration task?

---

## Priority Study Areas
- **Highest risk:** 6.1-6.7 (gRPC — on resume, mostly unsure)
- **High impact:** 5.1, 5.9, 5.10, 5.12 (resilience patterns — core to the role)
- **Likely in live coding:** 5.1 (retry/backoff), 5.8 (fan out with CompletableFuture)

---

## Answer Files

### 01_core_java/
- `1.1_hashmap_vs_concurrenthashmap.md`
- `1.2_hashmap_collisions.md`
- `1.3_comparable_vs_comparator.md`
- `1.4_checked_vs_unchecked.md`
- `1.5_linkedlist_vs_arraydeque.md`

### 02_multithreading/
- `2.1_synchronized.md`
- `2.2_threadlocal.md`
- `2.3_volatile.md`
- `2.4_executor_service.md`
- `2.5_backpressure.md`
- `2.6_reentrant_lock.md`

### 03_streams_functional/
- `3.1_optional_orelse.md`
- `3.2_functional_interfaces.md`

### 04_jvm_internals/
- `4.1_java_memory_model.md`

### 05_http_integrations/ (includes resilience)
- `5.1_retry_exponential_backoff.md`
- `5.2_timeouts.md`
- `5.3_json_schema_evolution.md`
- `5.4_pagination.md`
- `5.5_securing_http_calls.md`
- `5.7_partial_failures.md`
- `5.8_fan_out.md`
- `5.9_circuit_breaker.md`
- `5.10_bulkhead.md`
- `5.11_dead_letter_queue.md`
- `5.12_transient_vs_permanent.md`
- `5.13_rate_limiting.md`

### 06_grpc/
- `6.1_grpc_vs_rest.md`
- `6.2_grpc_patterns.md`
- `6.3_protobuf_vs_json.md`
- `6.4_grpc_deadlines.md`
- `6.5_grpc_authentication.md`
- `6.6_grpc_client_disconnect.md`
- `6.7_protobuf_versioning.md`
