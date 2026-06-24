# Time-Series Databases Interview Questions

# Question 1: How do time-series databases differ fundamentally from traditional relational databases? Explain concepts like timestamp indexing, retention policies, and why TSDBs are optimized for append-only workloads.

## Answer

Time-Series Databases (TSDBs) are purpose-built to handle data where time is the primary axis. Unlike traditional relational databases (RDBMS), which focus on ACID transactions and managing updates/deletions of complex interrelated state, TSDBs are optimized for high-volume, continuous data ingestion and aggregate reads over time windows.

*   **Timestamp Indexing:** In a TSDB, data is typically indexed and partitioned primarily by time. This allows for extremely fast retrieval of metrics within a specific time range, leveraging columnar storage structures to scan contiguous memory/disk blocks efficiently without sequential scanning.
*   **Append-Only Workloads:** Time-series data is historically immutable (e.g., a CPU reading at 10:00 AM doesn't change). TSDBs are optimized for append-only insertions. They often use Log-Structured Merge-trees (LSM-trees) or Time-Structured Merge (TSM) trees instead of B-trees, enabling high write throughput without the overhead of row-level locks or index rebalancing.
*   **Retention Policies:** Because TSDBs ingest massive volumes, data ages rapidly. TSDBs implement automated retention policies (Data Lifecycle Management) to periodically expire or drop raw, high-resolution data once it falls outside a configured temporal window, often replacing it with downsampled aggregates to save disk space.

---

# Question 2: Can you explain the data model in InfluxDB? What are measurements, tags, and fields, and why is it critical to keep the cardinality of tags low?

## Answer

The InfluxDB data model relies on a few core concepts designed for fast querying of multidimensional time-series data:

*   **Measurement:** Analogous to a table in an RDBMS. It acts as the container for tags, fields, and timestamps (e.g., `cpu_load`, `sensor_temperature`).
*   **Tags:** Key-value pairs that represent metadata. Crucially, **tags are indexed**. They are used to filter and group data efficiently (e.g., `host=serverA`, `region=us-east`).
*   **Fields:** Key-value pairs that represent the actual metrics or payload. **Fields are not indexed** (e.g., `value=85.5`, `status=true`). They store the varying data over time.

**Tag Cardinality:** Cardinality refers to the number of unique tag sets (combinations of tag keys and values) for a measurement. In InfluxDB, high cardinality creates memory pressure because InfluxDB maintains an index for all unique tag combinations to speed up queries (the Time Series Index, TSI). If you use an unbounded identifier (like an auto-incrementing ID, session ID, or user ID) as a tag, the cardinality explodes, leading to OOM errors, degraded write performance, and slow query execution. Such unbounded values must always be stored as fields.

---

# Question 3: Time-series data can grow exponentially. How do databases like InfluxDB handle compression for high-frequency data, and what algorithms (e.g., Gorilla or Delta-of-Delta) are typically utilized?

## Answer

TSDBs achieve massive compression (often 10x or more over traditional RDBMS) because time-series data changes predictably and incrementally. They employ specialized encoding and compression techniques tailored to each data type:

*   **Timestamps (Delta-of-Delta):** Timestamps usually arrive at regular intervals (e.g., every 10s). Instead of storing full 64-bit timestamps, TSDBs store the difference (delta) between consecutive timestamps. Because the interval is constant, the difference of the differences (delta-of-delta) is often exactly `0`. A sequence of zeros can be highly compressed using Run-Length Encoding (RLE) or Simple8b.
*   **Floats (Gorilla Compression):** Made famous by Facebook's Gorilla paper, this technique XORs consecutive floating-point values. If values change slightly, the XOR result has many leading and trailing zeros. The algorithm stores only the significant bits and the number of leading/trailing zeros, drastically shrinking the payload.
*   **Integers and Strings:** Integers are typically compressed using ZigZag encoding and delta encoding. Repeated strings (like tag values) are compressed using Dictionary Encoding (replacing repeating strings with small integer pointers) followed by Snappy or LZ4 compression on the blocks.

---

# Question 4: When visualizing months of high-frequency telemetry, querying raw data is inefficient. How do you implement continuous queries or downsampling tasks in InfluxDB to pre-aggregate data?

## Answer

Querying millions of high-resolution data points for a multi-month visualization is computationally expensive and slow. Downsampling solves this by pre-calculating aggregates (e.g., 1-minute averages rolled up into 1-hour averages).

Combining downsampling with retention policies—where raw data is kept for 7 days but 1-hour rollups are kept for 1 year—dramatically reduces storage costs and accelerates dashboard load times.

*   **InfluxDB 1.x (Continuous Queries):** You define a Continuous Query (CQ) that automatically runs periodically. For example:
    ```sql
    CREATE CONTINUOUS QUERY "cq_1h_cpu" ON "telegraf"
    BEGIN
      SELECT mean("usage_user") INTO "cpu_1h" FROM "cpu" GROUP BY time(1h), *
    END
    ```

*   **InfluxDB 2.x / Flux (Tasks):** InfluxDB 2.x and later uses Tasks, which are scheduled Flux scripts offering more flexibility and data manipulation capabilities.
    ```flux
    option task = {name: "downsample_1h", every: 1h}
    from(bucket: "raw_telemetry")
      |> range(start: -task.every)
      |> filter(fn: (r) => r._measurement == "cpu")
      |> aggregateWindow(every: 1h, fn: mean)
      |> to(bucket: "downsampled_telemetry")
    ```

---

# Question 5: If you need to ingest millions of metrics per second into InfluxDB, how would you design the ingestion pipeline? Discuss techniques like batching, connection pooling, and handling backpressure.

## Answer

To support millions of writes per second, ingestion must be highly optimized and decoupled to protect the TSDB from resource exhaustion:

*   **Batching:** Writing points individually causes massive network and protocol overhead. Ingestion clients must buffer metrics and send them in large batches (e.g., 5,000–10,000 points per HTTP request). This maximizes network throughput and allows the TSDB engine to write larger, more optimized blocks to disk.
*   **Connection Pooling / Keep-Alive:** Reusing HTTP connections via connection pooling (HTTP Keep-Alive) avoids the latency and CPU overhead of TCP handshakes and TLS negotiation for every batch.
*   **Backpressure Handling:** High spikes can overwhelm the TSDB. The pipeline should include an asynchronous, resilient buffer (like Kafka or RabbitMQ) between producers and consumers. If InfluxDB pushes back (e.g., returns `HTTP 429 Too Many Requests` or write timeouts), the ingest consumers can implement exponential backoff and pause pulling from the buffer, preventing data loss.
*   **Parallel Ingestion & Sharding:** Distribute the load using multiple consumer threads writing concurrently. When doing so, group data by partition keys (e.g., tag groupings) to avoid lock contention when writing to the database shards.

---

# Question 6: Describe how you would integrate a streaming platform like Kafka with InfluxDB. Why might you use Kafka as a buffer before writing to a time-series database?

## Answer

Integrating Kafka as a buffer before InfluxDB forms a robust, loosely coupled telemetry pipeline. Producers (IoT devices, microservices) publish metrics to Kafka topics. A consumer group (e.g., using Telegraf, Kafka Connect with an InfluxDB Sink Connector, or a custom Spring Kafka application) reads from these topics, batches the points, and writes them to InfluxDB.

**Why use Kafka as a buffer?**
1.  **Shock Absorption / Surge Protection:** InfluxDB might experience CPU or I/O bottlenecks during massive metric spikes or internal compactions. Kafka durably buffers the data until InfluxDB can catch up, preventing dropped metrics and dropped connections at the edge.
2.  **Decoupling:** Producers don't need to know about InfluxDB's availability, credentials, or schema. They just write standardized payloads to Kafka.
3.  **Data Enrichment & Fan-out:** With Kafka, other downstream systems (like a Flink real-time anomaly detection pipeline or a Hadoop data lake) can consume the exact same telemetry stream concurrently without placing any additional read or query load on InfluxDB.

---

# Question 7: In real-world telemetry, sensors occasionally drop metrics. How do you handle missing data points when querying? Explain the use of interpolation functions (like linear or LOCF) in your queries.

## Answer

Missing data disrupts time-aligned calculations, impacts machine learning models, and creates gaps in dashboard visualizations. We handle these gaps during querying using imputation or interpolation functions:

*   **LOCF (Last Observation Carried Forward):** This technique fills a missing interval with the exact value of the previous recorded point. It is ideal for stateful metrics (e.g., a door's "open/closed" status, or a sensor's configured threshold) where the value is assumed to remain constant until explicitly changed.
*   **Linear Interpolation:** This draws a straight line between the known point before the gap and the known point after the gap, calculating proportional values for the missing timestamps. It is suited for continuous metrics like temperature, CPU usage, or battery drain, where changes are generally gradual.

**Implementation Example (Flux):**
InfluxDB's Flux language handles this natively during aggregation windows:
```flux
from(bucket: "sensors")
  |> range(start: -1h)
  |> aggregateWindow(every: 1m, fn: mean) // Creates regular intervals; missing windows yield 'null'
  |> interpolate.linear() // Or use fill(usePrevious: true) for LOCF
```

---

# Question 8: What is deadband filtering in the context of IoT and time-series data? How would you implement deadbanding at the edge versus inside the database to reduce unnecessary data transmission and storage?

## Answer

Deadband filtering is an optimization technique where a new metric is only transmitted or stored if its value differs from the previously recorded value by more than a predefined threshold (the "deadband"). 

**Why it matters:** In IoT, sensors often emit high-frequency data where the value remains static (e.g., room temperature staying at 72.0°F for hours). Transmitting every point wastes bandwidth, battery life, and database storage.

*   **Implementation at the Edge (Preferred):** The IoT device or Edge Gateway caches the last sent value. It evaluates new sensor readings locally; if `abs(new_value - last_sent_value) <= threshold`, the data point is dropped. This drastically reduces network egress costs and saves battery on remote devices.
*   **Implementation at the Ingestion/Database Layer:** If edge devices are "dumb" and stream everything, deadbanding can be implemented at the ingest layer (e.g., using a stream processor like Kafka Streams/Flink, or Telegraf processors) or inside the TSDB using downsampling tasks. While this saves database storage and downstream query compute, it does *not* save network bandwidth from the edge. When implementing server-side, a stateful function is needed to compare the incoming stream against the last recorded value for that specific tag set.
