# Question 1: What is a "sargable" predicate in SQL and why does it matter for query performance? Give examples of sargable vs non-sargable WHERE conditions.
## Answer
**Sargable** (Search ARGument ABLE) refers to a predicate (a condition in the `WHERE` clause) that allows the database engine to use an index to evaluate it efficiently. If a predicate is sargable, the database can perform an Index Seek rather than a full Index Scan or Table Scan, vastly improving query performance.

**Non-Sargable Example:**
Applying a function or operation to a column usually prevents index usage.
```sql
-- Non-sargable: function applied to column
SELECT * FROM users WHERE YEAR(created_at) = 2023;

-- Non-sargable: leading wildcard prevents B-Tree index usage
SELECT * FROM users WHERE email LIKE '%@gmail.com';
```

**Sargable Example:**
Rewriting the condition to isolate the column makes it sargable.
```sql
-- Sargable: column is isolated
SELECT * FROM users 
WHERE created_at >= '2023-01-01' AND created_at < '2024-01-01';

-- Sargable: trailing wildcard allows B-Tree traversal
SELECT * FROM users WHERE email LIKE 'john%';
```

# Question 2: What are the SQL isolation levels? How does PostgreSQL specifically implement them via MVCC (e.g. Read Uncommitted and Repeatable Read)?
## Answer
The SQL standard defines four isolation levels based on the anomalies they prevent (Dirty Reads, Non-Repeatable Reads, Phantom Reads):
1. **Read Uncommitted:** Allows reading uncommitted changes (Dirty Reads).
2. **Read Committed:** Guarantees data read is committed. Prevents Dirty Reads.
3. **Repeatable Read:** Ensures multiple reads of the same row in a transaction return the same data. Prevents Non-Repeatable Reads.
4. **Serializable:** Strictest level. Transactions behave as if executed serially. Prevents Phantom Reads.

**PostgreSQL Implementation via MVCC:**
Postgres uses Multi-Version Concurrency Control (MVCC), where each row has a `xmin` (creation transaction ID) and `xmax` (deletion/update transaction ID). Readers don't block writers, and writers don't block readers.

- **Read Uncommitted:** PostgreSQL actually treats this as `Read Committed`. It does *not* support dirty reads because MVCC inherently hides uncommitted row versions from other transactions.
- **Repeatable Read:** Takes a snapshot of the database at the start of the *transaction* (unlike Read Committed, which takes a snapshot at the start of each *statement*). It also prevents Phantom Reads in PostgreSQL, going beyond the SQL standard, though it can still suffer from serialization anomalies which are only caught in the `Serializable` level.

# Question 3: How do you prevent lost updates in a highly concurrent payment scenario with 500 parallel requests? Would you use SELECT FOR UPDATE?
## Answer
A "lost update" occurs when two concurrent transactions read the same data, modify it, and commit, causing one modification to overwrite the other. 

In a high-concurrency payment scenario (e.g., deducting a balance), you can prevent lost updates using pessimistic or optimistic locking.

**Pessimistic Locking (`SELECT ... FOR UPDATE`):**
Yes, `SELECT ... FOR UPDATE` is highly effective. It locks the read rows until the transaction commits, forcing concurrent requests to wait.
```sql
BEGIN;
SELECT balance FROM accounts WHERE id = 1 FOR UPDATE;
-- Application checks balance, then updates
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT;
```
This is robust but can reduce concurrency and cause lock contention.

**Alternative (Optimistic Locking / Atomic Updates):**
If the update logic is simple, atomic updates are usually better for 500 parallel requests to avoid lock wait timeouts.
```sql
UPDATE accounts 
SET balance = balance - 100 
WHERE id = 1 AND balance >= 100;
```
If returning affected rows is 0, the transaction can be aborted. For complex logic across multiple tables, `SELECT FOR UPDATE` or optimistic locking via a `version` column is preferred.

# Question 4: Explain how micro-partitioning works in modern cloud data platforms compared to traditional B-Tree indexing. How does selecting the right clustering key improve partition pruning for analytical queries?
## Answer
**Traditional B-Tree Indexing:**
B-Trees are optimized for OLTP workloads. They allow fast O(log n) point lookups by traversing a tree of index nodes. However, for analytical (OLAP) queries scanning billions of rows, navigating B-Trees row-by-row is inefficient.

**Micro-partitioning (e.g., Snowflake, BigQuery):**
Data is automatically divided into small, immutable columnar files (micro-partitions), typically 50-500MB in size. The database maintains metadata for each micro-partition (min/max values of each column, null counts, etc.).

**Partition Pruning via Clustering Keys:**
When an analytical query runs (e.g., `WHERE date >= '2023-01-01' AND status = 'ACTIVE'`), the engine checks the metadata. If a micro-partition's min/max values for `date` don't overlap with the query predicate, the entire micro-partition is skipped (pruned) without reading its data. 

Selecting the right **clustering key** sorts the data across micro-partitions so that similar values are physically co-located. This minimizes the overlap of min/max ranges across partitions, maximizing pruning efficiency and dramatically reducing I/O and query time.

# Question 5: When ingesting semi-structured data (like JSON) directly into a database, how do you evaluate the trade-offs between storing it natively in a specialized variant column versus parsing and normalizing it into traditional relational tables?
## Answer
When ingesting JSON, the choice between native JSON columns (e.g., `JSONB` in Postgres, `VARIANT` in Snowflake) and normalized relational tables depends on schema stability, access patterns, and read vs. write performance needs.

**Native JSON/Variant Column:**
- **Pros:** Maximum flexibility. Perfect for evolving or unpredictable schemas (e.g., third-party APIs). Faster ingestion (no ETL/parsing overhead).
- **Cons:** Slower query performance compared to native columns. Requires specialized functions to extract data. Indexes on JSON paths (like GIN in Postgres) can be large and slow to update.

**Parsed & Normalized (Relational Tables):**
- **Pros:** Strongly typed, enforces data integrity. Superior read performance, easy to index, and smaller storage footprint (due to column data types). Standard SQL querying.
- **Cons:** Rigid schema. Ingestion pipeline is complex and brittle—if the JSON structure changes, the ETL process might break.

**Best Practice / Trade-off:**
Use a hybrid approach. Extract highly queried, stable fields into top-level relational columns during ingestion for fast filtering and joins. Store the remainder of the unpredictable payload in a JSON/Variant column for flexibility.

# Question 6: Explain the performance degradation associated with OFFSET for deep pagination on large datasets. How do you implement keyset (cursor-based) pagination in SQL to resolve this?
## Answer
**The OFFSET Degradation Problem:**
A query like `SELECT * FROM orders ORDER BY created_at LIMIT 50 OFFSET 1000000` requires the database to sort the rows, compute the first 1,000,050 rows, discard the first 1,000,000, and return the 50. As `OFFSET` grows, the query becomes linearly slower, wasting immense CPU and I/O.

**Keyset (Cursor-based) Pagination:**
Instead of relying on row numbers, keyset pagination uses the last retrieved value of the sorted column as a "cursor" to fetch the next set.

**Implementation:**
Assuming we sort by `created_at` and `id` (as a tie-breaker):
```sql
-- Client passes the cursor (last_created_at, last_id) from the previous page
SELECT * FROM orders 
WHERE (created_at, id) > ('2023-10-01 10:00:00', 5521)
ORDER BY created_at ASC, id ASC 
LIMIT 50;
```
**Why it resolves the issue:**
If there is an index on `(created_at, id)`, the database engine can perform an Index Seek directly to the cursor's location and read the next 50 rows. The query time remains consistently fast regardless of page depth.

# Question 7: How do you execute a schema change involving a massive table rewrite (e.g., splitting a high-traffic table or changing a primary key data type) using SQL without causing application downtime or locking the table?
## Answer
Massive schema changes (like changing a PK type or splitting a table) require table locks which block reads/writes, causing downtime. To achieve zero downtime, we use the **Expand and Contract** pattern combined with background syncing.

**Steps for Zero-Downtime Rewrite:**
1. **Create the New Structure:** Create the new table `table_v2` with the desired schema.
2. **Setup Dual-Writing / Triggers:** Implement application-level dual-writes or database-level triggers so any new `INSERT`/`UPDATE`/`DELETE` on `table_v1` is automatically applied to `table_v2`.
3. **Backfill Existing Data:** Run a background script to copy historical data from `table_v1` to `table_v2` in small batches (using keyset pagination) to avoid long locks and replication lag. Handle conflicts by preferring the newly triggered updates over backfilled data (e.g., `INSERT ON CONFLICT DO NOTHING`).
4. **Validation:** Continuously verify that `table_v1` and `table_v2` are consistent.
5. **Switch Reads/Writes:** Deploy application code to read and write exclusively from `table_v2`.
6. **Cleanup (Contract):** Once stable, drop the triggers and eventually drop `table_v1`.

Tools like `gh-ost` (GitHub) or `pt-online-schema-change` automate this via triggers and shadow tables for MySQL.

# Question 8: What are the structural and querying differences between table partitioning and database sharding? In a high-throughput backend architecture, when is one preferred over the other?
## Answer
**Table Partitioning (Vertical/Horizontal within one DB):**
- **Structure:** A single logical table is divided into multiple physical tables (partitions) within the *same* database instance.
- **Querying:** Transparent to the application. The query optimizer handles "partition pruning." 
- **Use Case:** Great for data lifecycle management (e.g., dropping old time-series data partition by partition) and local query optimization. Doesn't scale compute/storage beyond a single machine.

**Database Sharding:**
- **Structure:** Data is distributed across multiple separate database *instances* (nodes). Each node holds a subset of the data (a shard).
- **Querying:** Not transparent. The application or a proxy (like Vitess) must route queries using a Shard Key. Cross-shard joins are extremely slow and complex.
- **Use Case:** Necessary when horizontal scaling is required—when a single database server cannot handle the storage capacity or the read/write IOPS throughput.

**When to prefer one:**
In a high-throughput backend, if the bottleneck is table size but CPU/IOPS are fine, use **Partitioning**. If the server is hitting hard limits on CPU, memory, or disk I/O, you must use **Sharding** to scale horizontally.

# Question 9: Explain the differences between optimistic locking (using version columns) and pessimistic locking (SELECT ... FOR UPDATE). In a high-concurrency transactional system, how do you decide which to use?
## Answer
**Optimistic Locking:**
Assumes conflicts are rare. It uses a `version` or `updated_at` column. 
- **Mechanism:** Read the row and its version. On update, `WHERE id = X AND version = Y`. If the update affects 0 rows, another transaction modified it, and the application must retry.
- **Pros:** No database locks. Highly scalable for read-heavy workloads.
- **Cons:** High concurrency on the *same* row leads to excessive application retries.

**Pessimistic Locking (`SELECT ... FOR UPDATE`):**
Assumes conflicts are common.
- **Mechanism:** Locks the row when read. Other transactions trying to read/update the row will block until the lock is released.
- **Pros:** Prevents lost updates directly. No complex application retry logic.
- **Cons:** Reduces concurrency. Can lead to lock contention, slow transaction times, and deadlocks.

**How to decide:**
- Use **Optimistic Locking** for mostly read-heavy systems where concurrent edits to the exact same record are rare (e.g., user updating their own profile).
- Use **Pessimistic Locking** for write-heavy, high-contention systems on specific records where strict serialization is required (e.g., financial ledger deduction, inventory decrements).

# Question 10: What are "Phantom Reads" and "Non-Repeatable Reads"? Provide an SQL transaction scenario where setting the isolation level to SERIALIZABLE is strictly necessary to maintain data integrity.
## Answer
**Non-Repeatable Read:** 
Transaction A reads a row. Transaction B *updates or deletes* that row and commits. Transaction A reads the row again and gets different data (or the row is missing).

**Phantom Read:** 
Transaction A runs a range query (e.g., `SELECT * WHERE age > 20`). Transaction B *inserts* a new row matching the condition and commits. Transaction A runs the same query and gets a "phantom" row it didn't see before.

**Scenario for SERIALIZABLE:**
Imagine an employee scheduling system where a rule dictates: "A shift must have exactly 2 managers."
1. Tx1 counts managers for Shift X (Result: 1).
2. Tx2 counts managers for Shift X (Result: 1).
3. Tx1 inserts Alice as Manager for Shift X.
4. Tx2 inserts Bob as Manager for Shift X.

Under `Repeatable Read`, both transactions see 1 manager and proceed. The result is 3 managers, violating data integrity. Setting isolation level to `SERIALIZABLE` ensures one transaction will fail with a serialization anomaly, preventing the violation.

# Question 11: What specific conditions cause a deadlock in a relational database? How can you design your backend transactions and enforce SQL execution order to minimize their occurrence?
## Answer
**What causes a deadlock?**
A deadlock occurs when two or more transactions hold locks on resources that the other transactions need to proceed, creating a cyclic dependency.
- Tx1 updates Row A, then tries to update Row B.
- Tx2 updates Row B, then tries to update Row A.
Both wait indefinitely. The DB engine detects this and aborts one transaction.

**How to minimize their occurrence:**
1. **Enforce Consistent Order:** The most effective defense. Always access and modify tables (and rows within tables) in the exact same alphabetical or ID-based order across all backend services. If Tx1 and Tx2 both lock Row A before Row B, deadlocks are impossible.
2. **Keep Transactions Short:** Avoid external API calls or complex processing inside an open transaction block.
3. **Use Batch Atomic Updates:** Instead of multiple statements, use single bulk updates or `INSERT ... ON CONFLICT`.
4. **Lower Isolation Levels:** Use Read Committed instead of Serializable if business logic permits.
5. **Use Optimistic Locking:** Avoid row-level locks entirely using version checks.

# Question 12: How does the N+1 query problem manifest when interacting with databases via an ORM? How do you rewrite the backend SQL strategy (e.g., using batching or specific joins) to resolve it efficiently?
## Answer
**How it manifests:**
The N+1 problem occurs when an ORM executes 1 query to fetch a list of entities, and then N additional queries to fetch related entities for each row. 
For example, fetching 100 users, and then looping through them to print their addresses:
```python
users = User.objects.all() # 1 query
for user in users:
    print(user.address) # N queries executed lazily
```

**How to resolve it:**
1. **Batching (Eager Loading with IN clause):** 
Instruct the ORM to fetch related data in a secondary batch query. (e.g., `prefetch_related` in Django, `includes` in Rails).
*SQL generated:* 
```sql
SELECT * FROM users;
SELECT * FROM addresses WHERE user_id IN (1, 2, ..., 100);
```
2. **Specific Joins (Eager Loading via JOIN):**
Instruct the ORM to fetch all data in a single query using a JOIN (e.g., `select_related` in Django).
*SQL generated:*
```sql
SELECT u.*, a.* FROM users u JOIN addresses a ON u.id = a.user_id;
```
Use JOINs for 1-to-1 or Many-to-1 relationships. Use Batching (IN clause) for 1-to-Many to avoid Cartesian explosion of data returned over the network.

# Question 13: How does Multi-Version Concurrency Control (MVCC) allow a database engine to process read and write operations concurrently without strict locking?
## Answer
**Multi-Version Concurrency Control (MVCC)** prevents read-write blocking. The core philosophy is: **"Readers do not block writers, and writers do not block readers."**

**How it works:**
Instead of overwriting data in place and locking the row, MVCC treats rows as immutable. 
1. **Updates are Insertions:** When a row is updated, the database creates a brand *new* version of the row with the new data and stamps it with the current Transaction ID (e.g., `xmin`).
2. **Tombstoning:** The old row is marked as deleted/expired by stamping it with the updater's Transaction ID (e.g., `xmax`).
3. **Snapshots:** When a transaction starts a read, it gets a "snapshot" ID. It only reads row versions created *before* its snapshot, and ignores versions deleted before its snapshot or created after it.

Because old data is preserved, a long-running read query can happily scan older row versions while concurrent write transactions append new versions. Periodic "Vacuuming" (in Postgres) or undo-log pruning (in MySQL) reclaims space from obsolete versions.

# Question 14: When analyzing an SQL execution plan, in what scenarios will the query optimizer choose a Hash Join over a Nested Loop Join or a Merge Join?
## Answer
When analyzing an SQL execution plan for `A JOIN B ON A.id = B.a_id`:

1. **Nested Loop Join:**
   - **Scenario:** Best when one table is extremely small, or both are small, and there is a highly selective index on the join condition. It iterates through the outer table and performs an index lookup on the inner table.

2. **Merge Join:**
   - **Scenario:** Best when both tables are large, but both are *already sorted* on the join key (e.g., both join columns are indexed, or an `ORDER BY` was recently applied). The optimizer merges the two sorted sets linearly.

3. **Hash Join:**
   - **Scenario:** Chosen when joining two large, unsorted datasets where indexes are missing or scanning them is cheaper than random index access.
   - **Mechanism:** The optimizer takes the smaller of the two tables and builds an in-memory Hash Table using the join key. It then scans the larger table, hashing the join key of each row to probe the Hash Table for matches. It is highly CPU and memory efficient for large equijoins (uses `=`).

# Question 15: Beyond basic indexing, when would you specifically implement a covering index, a filtered (partial) index, or a composite index? How does column cardinality dictate your composite index ordering?
## Answer
**Covering Index:**
Includes all columns required by the query (`SELECT`, `WHERE`, `ORDER BY`), allowing the engine to return results directly from the index without a table heap lookup (avoiding IO). Used for highly queried, narrow `SELECT` statements.

**Filtered (Partial) Index:**
An index with a `WHERE` clause (e.g., `CREATE INDEX i ON orders(date) WHERE status = 'PENDING'`). Used when you only query a small subset of data (like unprocessed queue items), saving massive amounts of disk space and insert overhead.

**Composite Index:**
An index on multiple columns `(A, B, C)`. 
**Ordering by Cardinality:**
Cardinality is the number of unique values. The general rule of thumb is to order composite index columns from **Highest Cardinality to Lowest Cardinality**. 
If querying `WHERE tenant_id = X AND status = Y`:
- `tenant_id` has high cardinality (10,000s).
- `status` has low cardinality (3 values).
Index order `(tenant_id, status)` filters down to a tiny subset instantly. However, always ensure columns used for equality (`=`) precede columns used for ranges (`>`, `<`) regardless of cardinality.

# Question 16: What are the performance, scoping, and optimization differences between using a Common Table Expression (CTE), a Temporary Table, and a Materialized View when processing complex, multi-step data transformations?
## Answer
**Common Table Expression (CTE):**
- **Scoping:** Exists only for the duration of the single query.
- **Performance:** Inlined into the main query by the optimizer. Good for readability, but re-evaluated if referenced multiple times (unless materialized by the DB engine, like Postgres 12+ does optionally). Does not hold indexes.

**Temporary Table:**
- **Scoping:** Exists for the duration of the database session (or transaction).
- **Performance:** Written to disk (or temp memory). You can create **indexes** on it. Best for multi-step batch processing where an intermediate result is large, requires indexing, or is queried multiple times by different statements in a script.

**Materialized View:**
- **Scoping:** Permanent physical table updated periodically.
- **Performance:** Stores pre-computed results of a complex query (e.g., nightly aggregations). Best when read speed must be instantaneous across many sessions, but the underlying data doesn't require real-time freshness. Trades write/maintenance cost for extreme read performance.

# Question 17: In what scenarios does the computational cost of maintaining a materialized view outweigh the read performance benefits?
## Answer
The computational cost of a materialized view outweighs read benefits in scenarios with:

1. **High Churn (Write-Heavy Workloads):** If the underlying base tables are updated constantly (thousands of TPS), keeping the materialized view in sync using synchronous triggers or fast refreshes consumes massive CPU/IO, slowing down the primary OLTP writes.
2. **Real-Time Data Requirements:** If the business requires strict real-time accuracy, the materialized view must be refreshed synchronously on every write. This creates locking contention and defeats the purpose of pre-computation.
3. **Infrequent Reads:** If the materialized view is only queried once a week for a report, but refreshed hourly, you are burning compute cycles to maintain data that is rarely consumed.
4. **Complex Joins with No Fast Refresh:** If the DB doesn't support incremental (fast) refresh for the specific SQL used, every refresh requires a full drop and recalculation of millions of rows, which can overload the system.

# Question 18: In a distributed database system, what is data skew? How does choosing the wrong distribution key impact query execution times across nodes, and how do you resolve it?
## Answer
**Data Skew:** 
Occurs when data is unevenly distributed across nodes (shards/partitions) in a distributed DB (like Redshift, Snowflake, or Citus). Some nodes end up holding significantly more data than others.

**Impact of a Wrong Distribution Key:**
If you choose a distribution key with low cardinality (e.g., `country_code` where 90% of users are in the US), one node processes 90% of the workload. When running a distributed query, the entire system must wait for the slowest, overloaded node to finish. This negates the benefits of parallel processing.

**Resolution:**
Choose a distribution key with **high cardinality and uniform distribution** (e.g., `user_id` or `device_id`). 
If a naturally uniform key isn't heavily joined, you can use a composite distribution key or add a random salt to the key to force uniform hashing. For small lookup tables, use "Replicated/Broadcast" distribution, copying the table to all nodes to avoid network shuffling entirely.

# Question 19: When and how would you use a Recursive Common Table Expression? Walk through how you would construct a query to traverse a hierarchical dataset, such as a localized directory structure or an organizational chart.
## Answer
**When to use:**
Use a Recursive CTE when traversing hierarchical, tree, or graph data structures stored in a flat relational table (e.g., employee-manager charts, category trees, bill of materials).

**How it works:**
It consists of an Anchor Member (base case) and a Recursive Member (inductive step), united by `UNION ALL`.

**Example: Organizational Chart**
```sql
WITH RECURSIVE OrgChart AS (
    -- Anchor: Select the CEO (no manager)
    SELECT id, name, manager_id, 1 as level
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive: Join employees to the CTE
    SELECT e.id, e.name, e.manager_id, oc.level + 1
    FROM employees e
    INNER JOIN OrgChart oc ON e.manager_id = oc.id
)
SELECT * FROM OrgChart ORDER BY level;
```
The database executes the anchor, then repeatedly executes the recursive member using the previous iteration's results until no new rows are returned.

# Question 20: Explain a scenario where you would combine LAG() or LEAD() with a custom sliding window frame (e.g., ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW). How does this approach compare to self-joins for calculating period-over-period growth?
## Answer
**Scenario for Sliding Window:**
Calculating a Running Total, Moving Average, or Cumulative Max.
For example, calculating a 7-day moving average of sales:
```sql
SELECT date, sales,
       AVG(sales) OVER (
           ORDER BY date 
           ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
       ) as moving_avg
FROM daily_sales;
```

**Using LAG/LEAD with Frames:**
Technically, `LAG` and `LEAD` look at specific offset rows and don't require explicit window frames. However, using `SUM() OVER(ROWS BETWEEN ...)` or `FIRST_VALUE()` provides similar analytical power.

**Comparison to Self-Joins (for Period-over-Period):**
To calculate Day-over-Day growth:
- **Window Function:** `sales - LAG(sales, 1) OVER (ORDER BY date)`
- **Self-Join:** `SELECT a.sales - b.sales FROM sales a JOIN sales b ON a.date = b.date + INTERVAL '1 day'`

**Why Window Functions are superior:**
Self-joins on large tables require massive Cartesian products or expensive Hash Joins. Window functions require only a single table scan followed by an in-memory sort, making them drastically faster and more readable.

# Question 21: How do you handle "Upsert" operations efficiently on massive datasets? Discuss the mechanics and performance implications of using a MERGE statement versus discrete INSERT and UPDATE blocks.
## Answer
Handling upserts ("Insert if not exists, else Update") on massive datasets is a common ETL challenge.

**Discrete INSERT/UPDATE Blocks:**
- Writing a script to `UPDATE` existing rows, then `INSERT` missing rows requires multiple table scans. In high concurrency, doing this manually requires strict locks to avoid race conditions, severely degrading performance.

**MERGE Statement (or `INSERT ON CONFLICT`):**
- **Mechanics:** `MERGE INTO target USING source ON target.id = source.id WHEN MATCHED THEN UPDATE ... WHEN NOT MATCHED THEN INSERT ...`
- **Performance Implications:** 
  1. **Single Pass:** The engine parses the data once and executes the upsert in a single atomic transaction. 
  2. **Reduced Logging:** Often highly optimized internally for Write-Ahead Logging (WAL).
  3. **Concurrency:** `INSERT ... ON CONFLICT` (Postgres) uses row-level locking natively, preventing race conditions without deadlocking the entire table.

For massive batch ETLs in OLAP (e.g., Snowflake), `MERGE` combined with a staging table is the standard because it operates efficiently on columnar data files.

# Question 22: How do you write an SQL query to fill in missing date gaps in a time-series dataset (e.g., ensuring a continuous daily report even on days with zero events) using a calendar table?
## Answer
Relational databases only return data that exists. If grouping sales by date, days with zero sales won't appear. We use a **Calendar Table** (or generated series) to enforce continuity.

**SQL Query Strategy:**
1. Generate a continuous sequence of dates.
2. `LEFT JOIN` the actual data table onto the sequence.
3. Use `COALESCE` to turn `NULL`s into `0`.

**Example using Postgres `generate_series`:**
```sql
WITH date_series AS (
    SELECT generate_series(
        '2023-01-01'::date, 
        '2023-01-31'::date, 
        '1 day'::interval
    )::date AS report_date
)
SELECT 
    ds.report_date,
    COALESCE(SUM(s.amount), 0) AS total_sales
FROM date_series ds
LEFT JOIN sales s ON ds.report_date = s.sale_date
GROUP BY ds.report_date
ORDER BY ds.report_date;
```
This guarantees every day in January appears in the result set.

# Question 23: How would you use SQL window functions to group a continuous stream of activity logs into discrete "sessions," assuming a session ends after 30 minutes of inactivity?
## Answer
Sessionization involves grouping sequential events into a "session" ID if the time gap between events is less than a threshold (e.g., 30 mins).

**Step-by-Step Logic:**
1. **Find Time Delta:** Use `LAG()` to find the time difference between the current and previous event for a user.
2. **Flag New Sessions:** Use a `CASE` statement to flag a row as `1` if the delta is > 30 mins (or if it's the first event), else `0`.
3. **Cumulative Sum for Session ID:** Run a cumulative `SUM()` over the flags to generate a unique session ID.

**SQL Example:**
```sql
WITH TimeDeltas AS (
    SELECT user_id, timestamp,
           LAG(timestamp) OVER (PARTITION BY user_id ORDER BY timestamp) as prev_ts
    FROM activity_logs
),
SessionFlags AS (
    SELECT user_id, timestamp,
           CASE 
             WHEN prev_ts IS NULL THEN 1
             WHEN EXTRACT(EPOCH FROM (timestamp - prev_ts))/60 > 30 THEN 1
             ELSE 0 
           END as is_new_session
    FROM TimeDeltas
)
SELECT user_id, timestamp,
       SUM(is_new_session) OVER (PARTITION BY user_id ORDER BY timestamp) as session_id
FROM SessionFlags;
```
This elegantly creates grouped session buckets in a single pass without cursors or complex application logic.

