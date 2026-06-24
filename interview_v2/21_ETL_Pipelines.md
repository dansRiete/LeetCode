# 21 ETL Pipelines

# Question 1: What is the difference between ETL and ELT? Under what circumstances would you choose ELT over ETL?

## Answer
- **ETL (Extract, Transform, Load)**: Data is extracted from source systems, transformed in a dedicated processing engine (like Spark or Talend) or middle tier, and then loaded into the target data warehouse.
- **ELT (Extract, Load, Transform)**: Data is extracted and loaded directly into the target data warehouse in its raw form. The transformations are then pushed down and executed inside the data warehouse using its native compute capabilities (e.g., dbt + Snowflake/BigQuery).
- **When to choose ELT**: ELT is preferred in modern cloud architectures where cloud data warehouses (Snowflake, BigQuery, Redshift) have immense, highly scalable compute power that can process data faster and cheaper than an intermediate processing tier. It is also beneficial when you want to retain raw data for data scientists to explore before it gets aggregated or modeled.

# Question 2: How do you handle incremental loads versus full loads in a data pipeline? What are the common strategies for Change Data Capture (CDC)?

## Answer
- **Full Load**: Completely dropping and rewriting the target table. Simple but highly inefficient for large datasets.
- **Incremental Load**: Only processing records that have been inserted, updated, or deleted since the last run.
- **CDC Strategies**:
  1. **Timestamp/Watermark-based**: Querying records where `updated_at > last_processed_timestamp`. Cannot track hard deletes well.
  2. **Log-based CDC (Preferred)**: Using tools like Debezium to read the database transaction logs (e.g., MySQL binlog, Postgres WAL) directly. It is highly reliable, captures exact changes (inserts, updates, deletes) in real-time, and minimizes impact on the source database.
  3. **Diff/Snapshot-based**: Taking snapshots periodically and performing a full outer join to identify differences. Extremely slow for large tables.

# Question 3: What are the key strategies for ensuring data quality and handling bad records during the Transformation phase?

## Answer
- **Schema Validation**: Failing fast if incoming data violates expected types or formats (e.g., using Great Expectations or JSON Schema validation).
- **Dead Letter Queues (DLQ) / Quarantine Zones**: Instead of crashing the entire pipeline when a malformed record is encountered, the bad record is routed to a separate storage area (a DLQ or quarantine table) along with the error reason. Data engineers can inspect, fix, and reprocess these records later.
- **Defaulting/Imputation**: Replacing nulls or bad values with a known default value if the business logic permits.
- **Circuit Breakers**: Halting the pipeline if a certain threshold of bad records is exceeded (e.g., "stop processing if >5% of records are malformed").

# Question 4: Explain the concept of idempotency in data pipelines. Why is it crucial, and how do you achieve it?

## Answer
**Idempotency** means that running a pipeline multiple times with the same input parameters (e.g., the same date range) produces the exact same final state, without causing data duplication or corruption.
- **Why it's crucial**: Pipelines fail all the time (network issues, out of memory). Engineers need to be able to safely click "retry" without worrying about duplicating rows or writing complex cleanup scripts.
- **How to achieve it**:
  - For full loads: Always use `OVERWRITE` instead of `APPEND`.
  - For incremental loads: Use `UPSERT` / `MERGE` statements based on a unique primary key instead of standard `INSERT` statements.
  - Delete-then-Insert: Explicitly `DELETE` the records for the current processing window (e.g., `DELETE FROM target WHERE date = '2023-10-01'`) before inserting the newly calculated records.

# Question 5: How do you handle schema evolution in streaming or batch ETL pipelines to ensure downstream systems do not break?

## Answer
Schema evolution occurs when upstream sources add, remove, or modify columns.
- **Schema Registries**: Use tools like Confluent Schema Registry (for Kafka/Avro). It enforces compatibility rules (backward, forward, full) so producers cannot publish breaking schema changes.
- **Flexible Data Formats**: Use formats like Avro, Parquet, or JSON which support schema evolution explicitly.
- **Handling in the Warehouse**:
  - **Additions**: Most modern warehouses (like Snowflake) support `SCHEMA EVOLUTION` flags to automatically append new columns.
  - **Deletions/Modifications**: Harder to handle automatically. Usually handled by abstracting raw tables behind SQL Views. If a column is dropped upstream, the view can hardcode a `NULL` for that column to prevent downstream dashboards from breaking.

# Question 6: Compare batch processing vs. stream processing. Under what scenarios would you choose one over the other?

## Answer
- **Batch Processing**: Processing a large volume of data at scheduled intervals (e.g., nightly). High latency but high throughput and cheaper compute costs. Good for historical reporting, billing aggregation, or complex ML model training where real-time data is not necessary.
- **Stream Processing**: Processing data continuously as it arrives (event-by-event or micro-batches). Low latency, but often more complex to build and maintain (handling out-of-order events, late arrivals, windowing). Used for fraud detection, live dashboards, real-time recommendations, or alerting systems.

# Question 7: What is backfilling in an ETL context, and how do you design a pipeline to support easy and safe backfilling?

## Answer
**Backfilling** is the process of reprocessing historical data for a specific time period. This is often needed when business logic changes, a bug is fixed, or a new column is added.
- **Designing for backfilling**:
  - **Parameterization**: Pipelines should not rely on `CURRENT_DATE()`. They should be parameterized to accept an `execution_date` or `window_start`/`window_end`.
  - **Idempotency**: The pipeline must be idempotent so reprocessing old dates safely overwrites the incorrect historical data without duplication.
  - **Orchestrator Support**: Tools like Airflow allow you to easily define a past date range and automatically spawn tasks to backfill that period.

# Question 8: How do you monitor and alert on data pipeline failures, data freshness, and data anomalies?

## Answer
- **Pipeline Failures**: Use the orchestrator's built-in alerting (e.g., Airflow Slack/PagerDuty callbacks on task failure).
- **Data Freshness (SLAs)**: Monitor the maximum timestamp in target tables. If data is older than X hours, trigger an alert. Tools like dbt source freshness checks or Monte Carlo can automate this.
- **Data Anomalies (Data Observability)**: Run automated tests (using dbt tests, Great Expectations, or Anomalo) to check for sudden drops in row counts, unexpected NULL spikes, or metrics deviating from historical baselines (e.g., "daily revenue dropped by 90%").

# Question 9: Describe the role of a data orchestrator (like Apache Airflow or Dagster) in modern ETL pipelines. What problems do they solve?

## Answer
A data orchestrator manages the scheduling, execution, and monitoring of complex data workflows.
- **Dependency Management**: They allow you to define workflows as Directed Acyclic Graphs (DAGs), ensuring tasks run in the correct order (e.g., "Don't run the reporting transformation until the ingestion task completes").
- **Scheduling**: Replacing cron jobs with reliable, time-based or event-based triggers.
- **Retries & Alerts**: Automatically retrying failed tasks, triggering alerts on failure, and providing a centralized UI to view logs and pipeline status.
- **Backfilling**: Providing native mechanisms to rerun specific parts of a pipeline for historical dates.

# Question 10: What are the best practices for optimizing the performance of a slow-running ETL pipeline?

## Answer
- **Optimize I/O**: Read only the necessary columns and partition data effectively (e.g., partitioning by date so the engine can skip scanning irrelevant directories). Use columnar formats like Parquet.
- **Push-down Compute**: If doing ELT, leverage the data warehouse's compute cluster rather than pulling data into memory.
- **Avoid Cross Joins and Data Skew**: In distributed systems like Spark, ensure join keys are evenly distributed to prevent single nodes from bottlenecking (data skew). Use broadcast joins for small-to-large table joins.
- **Incremental Processing**: Avoid full table scans; shift to CDC or incremental logic.
- **Cluster Sizing**: Ensure the compute cluster (Spark, Snowflake warehouse) is appropriately sized and horizontally scaled for the workload.
