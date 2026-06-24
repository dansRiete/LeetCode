# Question 1: When designing a high-throughput Java data pipeline that reads sensor tags from edge devices and writes them to a time-series database like InfluxDB, what specific design patterns would you use in Java to execute high-throughput writes without bottlenecking the JVM or creating excessive garbage? And how do you handle data retention policies for rapidly changing continuous metrics?

## Answer
**High-Throughput Write Patterns in Java:**
- **Batching & Micro-batching:** Grouping multiple sensor tags into a single payload reduces network I/O overhead and I/O interrupts. Use asynchronous periodic flushes based on size or time (e.g., flush every 5,000 records or 500ms).
- **Asynchronous Execution & Backpressure:** Use non-blocking I/O and reactive streams (e.g., Project Reactor, RxJava) or `CompletableFuture` to avoid thread blocking. Implement backpressure to pause or drop reads from the edge when InfluxDB write queues are full, preventing JVM `OutOfMemoryError`.
- **Zero-Allocation / Object Pooling:** To avoid excessive Garbage Collection (GC) pauses under high throughput, minimize object creation. Use object pooling, `ByteBuffer`s, or the **LMAX Disruptor** (a lock-free ring buffer) to pass messages between reader and writer threads without creating new objects for every sensor reading.
- **Efficient Serialization:** Avoid costly string concatenations. Use highly efficient serialization frameworks or primitive arrays.

**Handling Data Retention in InfluxDB:**
- **Downsampling:** High-frequency data (e.g., ms-level readings) is aggregated over time windows (e.g., 1-minute or 5-minute averages) using InfluxDB Tasks (2.x) or Continuous Queries (1.x).
- **Tiered Retention Policies (RPs):** Store raw, high-resolution data in a short-lived Retention Policy (e.g., 3 days). Write the downsampled data into a longer-lived RP (e.g., 1 year or infinite). This balances storage costs with the ability to query long-term historical trends.

---

# Question 2: What are the core components of InfluxDB (e.g., measurements, tags, fields, timestamps)? How do these components relate to each other and how does this data model benefit time-series data?

## Answer
**Core Components:**
- **Measurement:** Similar to an SQL table. It acts as a logical container for data sharing the same tags and fields (e.g., `cpu_load` or `temperature`).
- **Tags (Tag Keys/Values):** Indexed key-value pairs representing metadata (e.g., `host=server-1`, `region=us-east`). Because they are indexed, filtering and grouping by tags is highly performant.
- **Fields (Field Keys/Values):** Unindexed key-value pairs representing the actual metric data (e.g., `usage_idle=98.5`). They cannot be efficiently grouped by, but they store the core numerical/string payload.
- **Timestamp:** The primary index for all data points, usually stored with nanosecond precision.

**Relationships:**
A combination of a **Measurement**, a specific **Tag Set**, and a **Field Key** defines a unique **Series**. A **Point** is a single record in a Series, distinguished by its **Timestamp**.

**Benefits for Time-Series Data:**
- **Fast Metadata Filtering:** The separation of indexed tags and unindexed fields allows lightning-fast `GROUP BY` and `WHERE` queries on metadata without the overhead of indexing rapidly changing metric values.
- **Storage Efficiency:** The underlying columnar storage engine (TSM) compresses timestamps and numerical fields highly efficiently.
- **Schema-less Flexibility:** You can dynamically add new tags or fields to a measurement without executing heavy `ALTER TABLE` migrations.

---

# Question 3: Explain the core functions of an API Gateway (e.g., Apigee, Kong, AWS API Gateway) in a microservices architecture. How do API policies (like rate limiting, authentication, and traffic management) help centralize cross-cutting concerns and why is this beneficial?

## Answer
**Core Functions of an API Gateway:**
- **Routing & Reverse Proxying:** Acts as the single entry point for clients, routing incoming requests to the appropriate backend microservice based on URL paths or headers.
- **Protocol Translation:** Bridges different protocols, such as exposing a RESTful HTTP endpoint to clients while communicating via gRPC or WebSocket with backend services.
- **Payload/Header Transformation:** Modifies requests or responses on the fly (e.g., stripping sensitive headers, formatting JSON).

**Centralizing Cross-Cutting Concerns via Policies:**
API Gateways enforce policies at the edge before traffic reaches the backend:
- **Security:** Handling OAuth/JWT token validation, API key verification, and CORS.
- **Traffic Management:** Enforcing rate limiting, throttling, and quotas to prevent abuse and DDoS attacks.
- **Resiliency:** Implementing circuit breakers, retries, and canary routing.

**Benefits:**
- **Decoupling & Developer Velocity:** Microservices can focus purely on business logic rather than duplicating boilerplate code for authentication and rate limiting across dozens of services.
- **Centralized Governance:** Security and traffic rules are managed from a single control plane, simplifying audits and policy updates.
- **Reduced Attack Surface:** The backend services remain in private subnets, shielded from direct internet access.

---

# Question 4: Explain the OAuth 2.0 Authorization Code flow. What are the key steps involved, and how does it ensure the client application securely obtains an access token on behalf of the user?

## Answer
The **Authorization Code Flow** is the standard, highly secure OAuth 2.0 grant type used primarily by server-side web applications. 

**Key Steps:**
1. **Authorization Request:** The client application redirects the user's browser to the Authorization Server (e.g., Okta) with its `client_id`, requested `scopes`, and a `redirect_uri`.
2. **User Authentication & Consent:** The user logs in to the Authorization Server and consents to granting the requested permissions.
3. **Authorization Grant:** The Authorization Server redirects the browser back to the client's `redirect_uri`, appending a short-lived, single-use `authorization_code` in the URL.
4. **Token Request:** The client application makes a secure, backend server-to-server (`POST`) request to the Authorization Server, exchanging the `authorization_code` and its `client_secret` for tokens.
5. **Token Response:** The Authorization Server validates the credentials and returns an `access_token` (and optionally an `id_token` and `refresh_token`).

**Security Guarantees:**
- **Back-Channel Communication:** The `access_token` is transmitted strictly server-to-server. It is never exposed to the user's browser, preventing malicious scripts or network interceptors from stealing it.
- **Client Authentication:** The client must authenticate with its `client_secret` to exchange the code, ensuring that even if the `authorization_code` is intercepted, it cannot be used by an attacker.
*(Note: For SPAs or mobile apps where a `client_secret` cannot be hidden, this flow is augmented with PKCE - Proof Key for Code Exchange).*

---

# Question 5: How do Continuous Queries (InfluxDB 1.x) or Tasks (InfluxDB 2.x) help manage data downsampling and aggregation for long-term storage in a time-series database? What are the benefits of this approach?

## Answer
**Mechanism:**
Continuous Queries (CQs) and Tasks are automated, scheduled background jobs running within the InfluxDB engine. They periodically execute aggregation functions (e.g., `MEAN()`, `MAX()`, `SUM()`) over a defined time window of high-resolution raw data, and insert the calculated results into a new measurement or retention policy.

**How They Manage Data:**
- **Downsampling:** They convert granular, high-frequency metrics (e.g., a reading every second) into lower-resolution summaries (e.g., an average reading every 5 minutes or 1 hour).
- **Tiered Storage Routing:** Tasks typically write these aggregates into a long-term Retention Policy, allowing the database to automatically delete the raw data via a short-term Retention Policy.

**Benefits:**
- **Significant Storage Savings:** Retaining raw, second-by-second data indefinitely consumes massive disk space. Downsampling dramatically reduces the storage footprint while preserving historical trends.
- **Drastically Improved Query Performance:** When generating dashboards (e.g., Grafana) spanning months or years, querying raw data leads to timeouts and heavy CPU/memory usage. Querying pre-aggregated downsampled data is practically instantaneous.
- **Operational Simplicity:** Native CQs and Tasks remove the need for external ETL pipelines or custom cron scripts, keeping the data lifecycle management entirely within the database.
