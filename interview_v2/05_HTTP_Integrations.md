# Question 1: In a microservice that calls multiple downstream service, how do you design timeouts, retries, and circuit breakers to avoid cascading failures while keeping latency predictable
## Answer
To avoid cascading failures and maintain predictable latency when calling multiple downstream services, you must apply the patterns of Timeouts, Retries, and Circuit Breakers in a coordinated fashion:

**1. Timeouts:**
- **Network Timeouts:** Establish strict connect and read timeouts for all HTTP/RPC calls. Connect timeouts should be very short (e.g., 50-100ms) to fail fast on unreachable hosts.
- **Global / Request Timeouts:** Define an overall upper bound for your service to respond to its caller. Use this to determine how long downstream requests are allowed to take.
- **Timeout Budgeting:** Pass the remaining time budget to downstream services (e.g., via a `grpc-timeout` or custom HTTP header) so they can abandon work if the caller has already timed out.

**2. Retries:**
- **Idempotency:** Only retry operations that are guaranteed to be safe and idempotent (like `GET` or `PUT`). Do not retry non-idempotent requests (like non-idempotent `POST`s) without an idempotency key.
- **Exponential Backoff and Jitter:** Space out retries exponentially and introduce randomness (jitter) to prevent a "thundering herd" problem when a downstream service recovers.
- **Bounded Retries:** Limit the maximum number of retries (e.g., 2 or 3) and restrict them to transient errors (like 503 Service Unavailable or network timeouts) rather than fatal errors (like 400 Bad Request or 401 Unauthorized).

**3. Circuit Breakers:**
- **Fail Fast:** If a downstream service starts failing repeatedly or timing out, a circuit breaker detects the failure rate and trips (opens). Subsequent calls will immediately fail without making a network request, thus protecting the downstream service from overload and saving caller threads/resources.
- **Half-Open State:** After a cooldown period, the circuit breaker transitions to a "half-open" state, allowing a small number of test requests through. If they succeed, the circuit closes; if they fail, it opens again.

**Implementation Example (Resilience4j in Java):**
```java
// Combining TimeLimiter, Retry, and CircuitBreaker
Supplier<String> decoratedSupplier = Decorators.ofSupplier(myRemoteCall)
    .withCircuitBreaker(circuitBreaker)
    .withRetry(retry)
    .withTimeLimiter(timeLimiter, scheduledExecutorService)
    .decorate();

Try.ofSupplier(decoratedSupplier)
   .recover(CallNotPermittedException.class, "Fallback (Circuit Open)")
   .recover(TimeoutException.class, "Fallback (Timeout)");
```

# Question 2: When you set timeouts across a call chain, how do you budget them between your service and multiple downstream calls so the overall request latency stays predictable? (like if your budget is 1 second how do you split it between two services)
## Answer
Timeout budgeting (or deadline propagation) ensures that upstream services don't wait for responses that are no longer needed, and downstream services don't waste resources processing requests that the upstream caller has already abandoned.

If your service has an overall budget of 1 second to respond to a user, and it needs to call Service A and then Service B sequentially, you should dynamically calculate the budget:

**1. Deadline Propagation:**
Instead of hardcoding a 500ms timeout for Service A and a 500ms timeout for Service B, calculate the absolute deadline timestamp when the request enters your service.

```java
long deadlineMs = System.currentTimeMillis() + 1000; // 1 second budget
```

**2. Sequential Calls Budgeting:**
Before calling Service A, compute the remaining time. If Service A takes 300ms, you have 700ms left. Before calling Service B, check the remaining time.
```java
long remainingForA = deadlineMs - System.currentTimeMillis();
if (remainingForA <= 0) throw new TimeoutException("Budget exceeded before calling A");
callServiceA(remainingForA);

// Time passes...
long remainingForB = deadlineMs - System.currentTimeMillis();
if (remainingForB <= 0) throw new TimeoutException("Budget exceeded before calling B");
callServiceB(remainingForB);
```

**3. Parallel Calls Budgeting:**
If you call Service A and Service B in parallel, they can both be given the full remaining budget (e.g., 1000ms minus setup time). You will wait for the `CompletableFuture.allOf()` with a timeout equal to the overall remaining budget.

**4. Passing Deadlines Downstream:**
Send the remaining budget to downstream services via headers (e.g., `grpc-timeout` in gRPC, or a custom `X-Timeout-Budget-Ms` in HTTP). If Service B itself needs to call a database, it should read the timeout header and apply that remaining time to its database queries.

# Question 3: When you introduce retries, how do you choose which operations are allowed to retry and how do you avoid retrying non-idempotent requests? (what API design signals do you rely on tell if a request is safe to retry)
## Answer
Retrying a request blindly can lead to duplicate transactions or data corruption if the request is not idempotent (i.e., making the request multiple times has the same effect as making it once). 

**Choosing Operations to Retry:**
1. **HTTP Method Semantics:** Rely on the standard HTTP methods.
   - **Safe/Idempotent:** `GET`, `HEAD`, `PUT`, `DELETE`. These can generally be retried safely. For instance, `PUT`ting the same resource state twice or `DELETE`ing a resource that is already deleted should not alter the system state beyond the first call.
   - **Non-Idempotent:** `POST`, `PATCH` (sometimes). You should not automatically retry a `POST` (e.g., "Create Order") unless you have mechanisms to prevent duplication.
2. **Type of Error:** Only retry transient network errors (Connection Reset, Socket Timeout) and specific HTTP status codes (503 Service Unavailable, 504 Gateway Timeout, 429 Too Many Requests). Do not retry client errors (400 Bad Request, 401 Unauthorized) as they will persistently fail.

**Avoiding Non-Idempotent Retries:**
To safely retry non-idempotent requests like `POST`, implement **Idempotency Keys**:
1. The client generates a unique ID (e.g., a UUID) for the operation and sends it via an HTTP header, like `Idempotency-Key: <uuid>`.
2. The server stores this key alongside the result of the operation in a fast datastore (like Redis) or database.
3. If the client experiences a network timeout (not knowing if the server processed the request or not) and retries the `POST` with the same `Idempotency-Key`.
4. The server sees the key, recognizes the request was already processed, and returns the cached successful response without executing the business logic again.

# Question 4: What is REST (Representational State Transfer) and what are its key architectural constraints?
## Answer
REST (Representational State Transfer) is an architectural style for designing networked applications. It relies on a stateless, client-server, cacheable communications protocol — almost always HTTP.

To be considered truly "RESTful", an API must adhere to six architectural constraints defined by Roy Fielding:

1. **Client-Server Architecture:** Separation of concerns. The client handles the UI and user state, while the server handles data storage and backend logic. They evolve independently.
2. **Statelessness:** Each request from client to server must contain all the information needed to understand and process the request. The server cannot store any session context about the client between requests.
3. **Cacheability:** Responses must implicitly or explicitly define themselves as cacheable or non-cacheable. If cacheable, clients or intermediaries can reuse the response data for subsequent equivalent requests, improving scalability and performance.
4. **Layered System:** A client cannot ordinarily tell whether it is connected directly to the end server, or to an intermediary (like a proxy, load balancer, or cache) along the way. This enables high availability and scalability.
5. **Uniform Interface:** This is central to REST and simplifies the architecture. It requires:
   - *Resource Identification:* URIs identify resources (e.g., `/users/123`).
   - *Resource Manipulation through Representations:* Clients modify resources by sending a representation of their state (e.g., a JSON payload via PUT/POST).
   - *Self-descriptive Messages:* Each message contains enough information to process it (e.g., standard HTTP methods, `Content-Type` headers).
   - *HATEOAS (Hypermedia As The Engine Of Application State):* Responses include hyperlinks to dynamically discover related actions and resources, guiding the client through state transitions.
6. **Code on Demand (Optional):** Servers can temporarily extend or customize client functionality by transferring executable code (e.g., JavaScript).

# Question 5: How do you approach API versioning in a microservice environment? Discuss different strategies and their trade-offs.
## Answer
In microservices, breaking changes to an API require versioning so existing clients aren't disrupted. There are several common strategies to handle API versioning, each with distinct trade-offs:

**1. URI Versioning (e.g., `/api/v1/users` and `/api/v2/users`)**
- **Pros:** Most common, extremely visible, straightforward to route via API Gateways, easy to test via browser.
- **Cons:** Violates strict REST principles (a resource URI shouldn't change just because its representation format changes); leads to URL bloat.

**2. Header Versioning (e.g., `X-API-Version: 2`)**
- **Pros:** Keeps URIs clean and semantically correct; simple to implement.
- **Cons:** Less discoverable than URI versioning; cannot be easily shared or tested via a simple browser link without an HTTP client.

**3. Content Negotiation / Media Type Versioning (e.g., `Accept: application/vnd.mycompany.v2+json`)**
- **Pros:** Strictly adheres to REST principles and the Uniform Interface; ties the version to the specific representation of the resource.
- **Cons:** Complex to implement and test; developers often find custom MIME types harder to work with.

**4. Parameter Versioning (e.g., `/api/users?version=2`)**
- **Pros:** Simple, URI stays relatively clean.
- **Cons:** Can complicate caching mechanisms; feels like a hack compared to other methods.

**Best Practices in Microservices:**
- Avoid breaking changes as long as possible (use additive changes: add fields instead of removing/renaming).
- **API Gateways:** Handle routing of different versions at the API Gateway layer to different backend microservice instances or deployments.
- **Sunset and Deprecation:** Actively monitor version usage and use headers like `Deprecation` and `Sunset` to communicate when older versions will be retired.

# Question 6: Explain the Bulkhead pattern in microservices or distributed systems. What problem does it solve, and how is it implemented?
## Answer
The **Bulkhead pattern** is a resilience mechanism designed to prevent failure in one part of a system from cascading and taking down the entire system. The name comes from the watertight compartments (bulkheads) in a ship's hull: if one compartment floods, the others remain dry, and the ship doesn't sink.

**What problem does it solve?**
In a distributed system, a single failing or slow downstream service can exhaust the upstream service's resources (like thread pools, connection pools, or memory). Without isolation, a bottleneck in one dependency (e.g., the Payment Service) will consume all worker threads in the API Gateway, preventing the Gateway from serving entirely unrelated requests (e.g., checking the Product Catalog).

**How is it implemented?**
Bulkheads isolate resources based on consumer, service, or functionality. Common implementations include:

1. **Thread Pool Isolation:** Allocate separate thread pools for different downstream dependencies. 
   - *Example:* The "Order Service" might have a thread pool of 10 threads for calling the "Payment Service", and a separate pool of 20 threads for calling the "Inventory Service". If "Payment" slows down and consumes all 10 of its threads, the "Inventory" calls are unaffected.
2. **Semaphore Isolation:** Instead of threads, use semaphores (counters) to limit the maximum number of concurrent requests to a specific service. This is lighter-weight than thread pool isolation and doesn't incur thread context-switching overhead.
3. **Deployment Isolation:** Deploy critical microservices on separate hardware or Kubernetes nodes from non-critical, heavy-processing services.

**Implementation Example (Resilience4j Semaphore Bulkhead):**
```java
// Limit to 10 concurrent calls to this specific backend
BulkheadConfig config = BulkheadConfig.custom()
    .maxConcurrentCalls(10)
    .maxWaitDuration(Duration.ofMillis(500))
    .build();
    
Bulkhead bulkhead = Bulkhead.of("paymentServiceBulkhead", config);

Supplier<String> restrictedSupplier = Bulkhead.decorateSupplier(bulkhead, () -> callPaymentService());
```
