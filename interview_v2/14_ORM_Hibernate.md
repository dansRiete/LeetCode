# Question 1: Name the four JPA entity lifecycle states, what triggers each transition, and what happens at flush()?

## Answer
The four JPA entity lifecycle states are:
1. **New (Transient)**: The entity is newly instantiated and not yet associated with an `EntityManager` (persistence context) and has no database representation.
   - Trigger: Creating a new object using the `new` keyword.
2. **Managed (Persistent)**: The entity is associated with the `EntityManager`. Any changes made to it will be tracked and automatically synchronized to the database upon flush or transaction commit.
   - Trigger: Calling `em.persist()` on a transient entity, or retrieving an entity from the DB via `em.find()` or a query.
3. **Detached**: The entity was previously managed but is no longer associated with a persistence context. Changes are not tracked.
   - Trigger: Closing the `EntityManager`, calling `em.clear()`, `em.detach(entity)`, or when the transaction ends (in standard Spring configurations).
4. **Removed**: The entity is scheduled for deletion from the database.
   - Trigger: Calling `em.remove()` on a managed entity.

**What happens at `flush()`?**
When `flush()` is called (either manually or automatically before commit/query), the `EntityManager` synchronizes the persistence context with the underlying database. It detects any state changes (dirty checking) in Managed entities and executes the corresponding SQL `INSERT`, `UPDATE`, and `DELETE` statements. Note that `flush()` synchronizes state but does not commit the transaction; a rollback will still undo the SQL executions.

# Question 2: Given a LAZY @ManyToOne and a loop over 100 entities accessing the association, how many SQL queries fire and what are three ways to fix it?

## Answer
This describes the classic **N+1 problem**. If you load 100 entities (1 query) and then access the LAZY association for each entity, Hibernate fires 1 additional query per entity to initialize the proxy. Total: **101 queries**.

Three ways to fix it:
1. **JOIN FETCH (JPQL/HQL)**: Fetch the association eagerly in the initial query.
   ```java
   @Query("SELECT e FROM Entity e JOIN FETCH e.association")
   List<Entity> findAllWithAssociation();
   ```
2. **@EntityGraph (Spring Data JPA)**: Override the fetch strategy at the query level dynamically.
   ```java
   @EntityGraph(attributePaths = {"association"})
   List<Entity> findAll();
   ```
3. **@BatchSize (or jdbc.batch_size)**: Keep the association LAZY, but when accessed, fetch the associations for the next N entities in a single `IN (...)` clause instead of one by one.
   ```java
   @BatchSize(size = 10) // on the association or entity
   ```
   *(Alternatively, using a DTO Projection to select exactly the columns needed in a single query is also an excellent fix.)*

# Question 3: What is the difference between @EntityGraph and JOIN FETCH, and when would you prefer one over the other?

## Answer
Both are used to avoid the N+1 problem by eagerly fetching associated entities.
- **JOIN FETCH**: Defined explicitly in JPQL queries. It executes an inner or outer join (based on how you write it).
  - *Preference*: Use `JOIN FETCH` when you are writing a custom query anyway, need complex join conditions, or need to fetch multiple nested levels explicitly.
- **@EntityGraph**: A JPA feature (heavily used via Spring Data JPA) that overrides global fetch strategies at the query definition level without modifying the JPQL.
  - *Preference*: Use `@EntityGraph` for declarative fetching on standard repository methods (like `findAll()`, `findById()`) where you don't want to manually write JPQL just to add a `JOIN FETCH`. It also avoids query duplication and makes dynamic fetching cleaner.

# Question 4: What are the default fetch types for @ManyToOne and @OneToMany? What is the rule of thumb for choosing?

## Answer
**Defaults:**
- **@ManyToOne** and **@OneToOne** (To-One associations): Default is `FetchType.EAGER`.
- **@OneToMany** and **@ManyToMany** (To-Many associations): Default is `FetchType.LAZY`.

**Rule of Thumb:**
Always default to **`FetchType.LAZY`** for *all* associations (including To-One).
Global eager fetching leads to unpredictable performance, unnecessary data loading, and Cartesian product issues. When specific use cases require the data, explicitly fetch the associations on a per-query basis using `JOIN FETCH` or `@EntityGraph`.

# Question 5: Why is changing LAZY to EAGER a bad fix for N+1?

## Answer
Changing `LAZY` to `EAGER` changes the fetch strategy **globally** for that entity.
1. **Performance & Memory**: Every time the entity is loaded, the association is loaded, even if the current business logic doesn't need it. This can lead to massive unneeded data retrieval and potential OutOfMemory (OOM) errors.
2. **Multiple Queries / Cartesian Product**: Depending on how data is queried (e.g. `em.find` vs JPQL), EAGER fetching might either generate multiple subsequent queries (still causing N+1 implicitly) or generate a massive JOIN leading to a Cartesian product if fetching multiple collections eagerly.
3. **Loss of Control**: You lose the ability to load just the parent entity quickly. The correct fix is to keep `LAZY` globally and use `JOIN FETCH` or `@EntityGraph` locally where the association is actually needed.

# Question 6: What is the difference between L1 and L2 cache in Hibernate? How do you enable L2 cache?

## Answer
**L1 Cache (First-Level Cache):**
- **Scope**: Session (`EntityManager`) scoped.
- **Behavior**: Enabled by default and cannot be disabled. It stores entities during a single transaction/session to ensure repeatable reads and avoid hitting the DB multiple times for the same entity in the same transaction.

**L2 Cache (Second-Level Cache):**
- **Scope**: `SessionFactory` (Application) scoped.
- **Behavior**: Disabled by default. It caches entities across multiple sessions/transactions, reducing database load globally.

**How to enable L2 Cache:**
1. Configure the properties in `application.properties`/`yml`:
   ```properties
   spring.jpa.properties.hibernate.cache.use_second_level_cache=true
   spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
   ```
2. Annotate the entity with `@Cacheable` and define the cache concurrency strategy:
   ```java
   @Entity
   @Cacheable
   @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   public class MyEntity { ... }
   ```

# Question 7: What types of cache does Hibernate have and how do they work together?

## Answer
Hibernate has three main cache types:
1. **First-Level (L1) Cache**: Bound to the `Session`/`EntityManager`. Always active. Ensures object identity within a transaction.
2. **Second-Level (L2) Cache**: Bound to the `SessionFactory`. Application-wide cache. Caches entity state across sessions.
3. **Query Cache**: Works in conjunction with the L2 cache. It stores the *results* of queries (the list of entity IDs that match a query, plus scalar values), not the actual entity data.

**How they work together:**
When a query is executed and query cache is enabled, Hibernate checks the Query Cache for the entity IDs. If found, it then looks up those IDs in the L1 cache, and if not present, in the L2 cache. If the entities aren't in the L2 cache either, it must hit the DB. Thus, the Query Cache heavily relies on the L2 cache to prevent the N+1 problem when resolving the cached IDs back into full entities.

# Question 8: What is the difference between REQUIRED and REQUIRES_NEW propagation? Give a real scenario for REQUIRES_NEW.

## Answer
- **`REQUIRED` (Default)**: If a transaction is already active, the method joins it. If not, it creates a new one. Both methods share the same transaction boundary; a failure in either rolls back the entire transaction.
- **`REQUIRES_NEW`**: Always creates a new, independent transaction. If an existing transaction is active, it is suspended until the new transaction completes. A rollback in the new transaction does not necessarily roll back the outer, suspended transaction.

**Scenario for `REQUIRES_NEW`:**
**Audit Logging**. If you have a business transaction that might fail and roll back, but you strictly want to log the attempt (e.g., failed login attempt, payment processing attempt) to the database regardless of the main transaction's outcome. By calling an audit service method annotated with `@Transactional(propagation = Propagation.REQUIRES_NEW)`, the audit log is committed immediately in its own transaction, ensuring it is saved even if the outer transaction rolls back.

# Question 9: By default, which exceptions trigger rollback in @Transactional? How do you change this?

## Answer
**Default Behavior:**
By default, `@Transactional` triggers a rollback only for **unchecked exceptions** (`RuntimeException` and its subclasses) and **Errors**. Checked exceptions (like `IOException`, `SQLException`) do *not* trigger a rollback.

**How to change this:**
You can specify the exception types explicitly using the `rollbackFor` and `noRollbackFor` attributes of the `@Transactional` annotation.
```java
// Roll back for any exception, including checked ones
@Transactional(rollbackFor = Exception.class)

// Do not roll back for a specific unchecked exception
@Transactional(noRollbackFor = CustomRuntimeException.class)
```

# Question 10: What is the self-invocation pitfall with @Transactional and how do you fix it?

## Answer
**The Pitfall:**
Spring's `@Transactional` relies on AOP proxies. When an external caller invokes a transactional method on a Spring bean, it goes through the proxy, which starts the transaction. However, if a method within the same bean calls another `@Transactional` method (`this.someTransactionalMethod()`), the call is made directly on the target instance, completely bypassing the proxy. The transactional behavior (like starting a new transaction) is ignored.

**How to fix it:**
1. **Refactor**: Extract the transactional method into a separate Spring bean (service class) and inject it. (Best Practice)
2. **Self-Injection**: Inject the bean into itself using `@Lazy` and call the method via the injected proxy.
   ```java
   @Autowired @Lazy private MyService self;
   ```
3. **AopContext**: Use `AopContext.currentProxy()` to explicitly call through the proxy (requires `exposeProxy=true` configuration).

# Question 11: How does @Version work mechanically — what SQL does Hibernate generate and what exception is thrown on conflict?

## Answer
**Mechanics:**
When you add `@Version` to a field (usually an integer or timestamp), Hibernate enables **Optimistic Locking**. Whenever an entity is updated, Hibernate automatically increments this version field.

**Generated SQL:**
Hibernate modifies the `UPDATE` statement to include the version in the `WHERE` clause:
```sql
UPDATE table_name 
SET column1 = 'value', version = 2 
WHERE id = 1 AND version = 1;
```

**Conflict Detection:**
If two concurrent transactions read the entity at version 1, and the first commits, the DB version becomes 2. When the second transaction tries to flush, its `UPDATE ... WHERE id = 1 AND version = 1` will affect 0 rows.
Hibernate checks the affected row count. Since it is 0, Hibernate knows another transaction modified the data concurrently and throws an `OptimisticLockException` (wrapped by Spring as `ObjectOptimisticLockingFailureException`).

# Question 12: When would you use pessimistic locking instead of optimistic locking?

## Answer
**Pessimistic Locking** (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) locks the row at the database level (`SELECT ... FOR UPDATE`).
**Optimistic Locking** (`@Version`) checks for conflicts only at the end of the transaction.

Use Pessimistic Locking when:
1. **High Contention**: Multiple transactions frequently attempt to update the same rows concurrently. Optimistic locking would result in massive `OptimisticLockException`s and retry storms, degrading performance.
2. **Strict Sequential Integrity**: You absolutely must prevent dirty reads or concurrent modifications at the very beginning of a complex calculation.

*Crucial Caveat*: Pessimistic locking should only be used when transactions are extremely short; otherwise, holding database locks for a long time will severely block other threads and kill application concurrency or lead to deadlocks.

# Question 13: What is dirty checking and when does Hibernate perform it? What is the benefit of @Transactional(readOnly=true)?

## Answer
**Dirty Checking** is Hibernate's mechanism to automatically detect changes made to managed entities. When an entity is loaded into the persistence context (L1 cache), Hibernate keeps a snapshot. Before committing or flushing, Hibernate compares the current state of the entity with the snapshot. If it detects differences, it automatically generates and executes an `UPDATE` SQL statement.
- **When it performs it**: At `flush()` time, which happens implicitly before queries are executed and right before the transaction commits.

**Benefit of `@Transactional(readOnly=true)`:**
It hints to Hibernate that no updates will occur. Hibernate optimizes by **not taking a snapshot** and **skipping the dirty checking phase** at flush time. This significantly reduces memory consumption and CPU overhead for pure read operations. It may also set the underlying JDBC connection to read-only, allowing database-level optimizations.

# Question 14: If you load and modify an entity inside @Transactional, do you need to call save()? Why?

## Answer
**No, you do not need to call `save()`**.
Because the method is wrapped in `@Transactional`, the entity loaded from the database is in the **Managed** state within the persistence context. Due to **Dirty Checking**, Hibernate tracks any modifications to managed entities. When the transaction completes, Hibernate automatically issues the necessary `UPDATE` SQL statement to synchronize the changes with the database. Calling `save()` is redundant and unnecessary.

# Question 15: When does LazyInitializationException occur? What is open-in-view and why is it an anti-pattern?

## Answer
**LazyInitializationException** occurs when you attempt to access an uninitialized lazy association of an entity, but the Hibernate `Session` (persistence context) that managed the entity has already been **closed**.

**Open Session In View (OSIV):**
OSIV is a pattern (often enabled by default in Spring Boot) that keeps the Hibernate Session open for the entire HTTP request lifecycle (down to the view/JSON serialization layer). It prevents `LazyInitializationException` because the session remains open during JSON serialization, dynamically fetching lazy associations.

**Why it's an anti-pattern:**
It forces the database connection to remain open for the entire HTTP request. If serialization or network transfer is slow, or if the view layer calls external APIs, the DB connection pool is exhausted quickly. It also silently triggers the N+1 problem during view rendering without the developer noticing.

# Question 16: What is wrong with loading all entities and saving them one by one? How do you fix it with @Modifying?

## Answer
**The Problem:**
Loading and updating entities one by one inside a loop generates a massive amount of SQL queries. For 500,000 records, it fires 1 `SELECT` (to load) and 500,000 individual `UPDATE` queries. This takes an exorbitant amount of time, saturates network I/O, and drastically degrades performance.

**The Fix:**
Instead of loading entities into memory, perform a bulk update directly using a JPQL query annotated with `@Modifying`.
```java
@Modifying
@Query("UPDATE Entity e SET e.status = :status WHERE e.condition = :condition")
int updateStatusBulk(@Param("status") String status, @Param("condition") String cond);
```
This executes exactly **1 SQL UPDATE statement** at the database level, bypassing the persistence context entirely, which is orders of magnitude faster.

# Question 17: What annotations are required alongside @Query for a bulk UPDATE/DELETE?

## Answer
For a custom bulk update or delete, you need two annotations:
1. **`@Modifying`**: Tells Spring Data JPA that the `@Query` is not a `SELECT` statement, but a write operation (`UPDATE` or `DELETE`). It alters the execution behavior (using `executeUpdate()` instead of `getResultList()`). It also provides the `clearAutomatically` flag to manage L1 cache staleness.
2. **`@Transactional`**: Unlike built-in repository methods like `save()`, custom `@Query` modifying methods do not have a default transaction applied. Write operations require an active transaction, so you must add `@Transactional` (usually at the service layer, but strictly required somewhere in the call stack).

# Question 18: What is the difference between em.persist() and em.merge()? When does each throw an exception?

## Answer
- **`em.persist(entity)`**: Transitions a **New (Transient)** entity to the **Managed** state. It schedules an `INSERT` statement. The entity passed becomes managed immediately.
  - *Exception*: Throws `EntityExistsException` if you attempt to persist an entity that already exists in the DB (usually detected via a generated ID or unique constraint conflict at flush).
- **`em.merge(entity)`**: Used for **Detached** entities. It copies the state of the given detached entity onto the managed entity with the same ID in the persistence context (loading it from the DB if necessary). It returns the *new managed instance*. The original object passed in remains detached.
  - *Exception*: Throws `IllegalArgumentException` if the entity is a removed entity. (Note: If merging an entity with an ID that does not exist in the database, Hibernate doesn't throw; it assumes it's a new entity and will perform an `INSERT`).

# Question 19: What are the JPA cascade types and what does CascadeType.ALL mean? What is orphanRemoval?

## Answer
JPA Cascade Types dictate how entity state transitions propagate from a parent entity to its associated child entities.
The 5 standard types are: **PERSIST, MERGE, REMOVE, REFRESH, DETACH**.
**`CascadeType.ALL`** is simply a shorthand that applies all 5 cascade types to the association.

**`orphanRemoval = true`:**
While `CascadeType.REMOVE` deletes the child when the *parent is deleted*, `orphanRemoval = true` deletes the child when it is *removed from the parent's collection* or its reference is set to null. If a child entity is no longer referenced by its parent, it is considered an "orphan" and Hibernate automatically issues a `DELETE` statement for it.

# Question 20: When would you use JPQL vs Criteria API vs native SQL?

## Answer
- **JPQL (Java Persistence Query Language)**: Use for most static, well-defined queries. It queries against entity models rather than DB tables, providing DB portability and type safety at the entity level. Easiest to read and maintain via `@Query`.
- **Criteria API**: Use when the query structure is **highly dynamic at runtime** (e.g., complex search filters where `WHERE` clauses, joins, and sorting depend on optional user input). It allows building queries programmatically using Java objects, ensuring compile-time type safety.
- **Native SQL**: Use when you need to leverage **database-specific features** not supported by JPA (e.g., window functions, specific index hints, proprietary JSON operators, complex CTEs), or for complex bulk operations where Hibernate's generated SQL is suboptimal.

# Question 21: What Hibernate properties enable JDBC batching? Why do you need flush()+clear() in a batch loop?

## Answer
**Enabling JDBC Batching:**
You need to set specific properties to enable batching inserts/updates:
1. `spring.jpa.properties.hibernate.jdbc.batch_size=50` (Defines the batch size).
2. `spring.jpa.properties.hibernate.order_inserts=true` (Groups identical inserts to be batched).
3. `spring.jpa.properties.hibernate.order_updates=true` (Groups identical updates).
*(Gotcha: Batching is disabled if the entity uses `GenerationType.IDENTITY` for its primary key, because Hibernate needs the ID immediately after insert).*

**Why `flush()` + `clear()` in a loop?**
When batch inserting thousands of entities, every `em.persist()` adds the entity to the L1 cache. If you don't clear the cache, the application will eventually run out of memory (OOM).
By calling `em.flush()` (to send the batch to the DB) followed by `em.clear()` (to detach entities and empty the L1 cache) every N iterations (matching the batch size), you maintain a constant, low memory footprint.

# Question 22: What is the difference between Page and Slice in Spring Data JPA? When would you use Slice?

## Answer
Both are used for pagination, but with different mechanics:
- **`Page<T>`**: Retrieves the requested chunk of data **AND** executes an additional `COUNT` query to determine the total number of records and total pages. Useful for classic pagination UIs (e.g., "Page 1 of 50").
- **`Slice<T>`**: Retrieves the requested chunk of data but **skips the COUNT query**. Instead, it asks the DB for `N + 1` rows (where N is the page size). If the extra row exists, it knows there is a next page (`hasNext() = true`).

**When to use `Slice`:**
Use `Slice` for **"Infinite Scroll"** or "Load More" UIs, especially on very large tables where executing a `COUNT(*)` query is extremely slow and resource-intensive.

# Question 23: What does @EntityGraph do and how does it differ from LAZY/EAGER defaults?

## Answer
`@EntityGraph` is a declarative way to define fetch plans in Spring Data JPA. It instructs Hibernate to eagerly fetch specific associations for a particular query, effectively overriding the default fetch strategies. It generates a SQL `LEFT OUTER JOIN` to fetch the data in a single query.

**Difference from LAZY/EAGER defaults:**
- `LAZY` / `EAGER` mappings on the entity are **static and global**. They apply every time the entity is loaded across the entire application.
- `@EntityGraph` is **dynamic and per-query**. It allows you to keep associations globally `LAZY` (best practice) while eagerly fetching them only in the specific repository methods where the associated data is actually needed, preventing N+1 without polluting global behavior.

# Question 24: What is a JPA Specification and when would you use it over @Query?

## Answer
**JPA Specification** is a Spring Data JPA wrapper around the JPA Criteria API. It uses the `Specification<T>` interface to define isolated, reusable, and composable query predicates.

**When to use it over `@Query`:**
Use Specifications for **Dynamic Filtering**.
With `@Query`, the structure of the SQL is static. If you have an API endpoint with 10 optional filters, writing a `@Query` with `WHERE (:name IS NULL OR e.name = :name) AND ...` becomes unreadable and performs poorly.
Specifications allow you to programmatically build the `WHERE` clause based on which parameters are present. You can logically chain them using `Specification.where(spec1).and(spec2)`, making it perfect for dynamic search pages. (Requires the repository to extend `JpaSpecificationExecutor`).

# Question 25: What is the difference between interface projection and DTO projection in Spring Data JPA?

## Answer
Projections are used to select only specific columns from the database instead of loading the entire entity.
- **Interface Projection**: You define a Java interface with getter methods matching the properties you want. Spring dynamically creates a proxy at runtime to back the interface.
  - *Advantage*: Very easy to define, especially for nested relationships (nested interfaces).
  - *Disadvantage*: Relies on proxies, which can have a slight overhead.
- **DTO Projection (Class-based)**: You define a standard Java class (or Record) with a constructor matching the selected fields.
  - *Advantage*: Extremely fast, pure POJOs without proxy overhead. Ideal for performance-critical APIs.
  - *Disadvantage*: Constructing complex queries with nested associations is harder, requiring full constructor expressions (`new com.example.MyDto(e.name, e.child.name)`) in JPQL.

# Question 26: What methods does JpaRepository provide and how does query derivation work?

## Answer
**Provided Methods:**
`JpaRepository` extends `CrudRepository` and `PagingAndSortingRepository`. It provides methods like:
- `save(entity)`, `saveAll(entities)`
- `findById(id)`, `findAll()`, `findAll(Pageable)`
- `existsById(id)`, `count()`
- `delete(entity)`, `deleteById(id)`, `deleteAllInBatch()`
- `flush()`, `saveAndFlush(entity)`
- `getReferenceById(id)` (returns a lazy proxy).

**Query Derivation:**
Spring Data JPA automatically parses method names to construct SQL queries at application startup.
You start with an introducer (e.g., `find`, `read`, `count`, `exists`, `delete`), followed by `By`, and then entity property names combined with keywords.
Examples: `findByNameAndAgeGreaterThan(String name, int age)`, `findByStatusIn(List<String> statuses)`, `countByActiveTrue()`, `findTop3ByOrderByCreatedAtDesc()`.

# Question 63: What are the three PessimisticLockModeType values in JPA and when would you use each?

## Answer
1. **`PESSIMISTIC_READ`** (`SELECT ... FOR SHARE`):
   - Acquires a shared lock. Other transactions can read the row, but cannot update or delete it until your transaction finishes.
   - *Use Case*: You need to ensure the data you are reading doesn't change during your transaction, but you don't intend to modify it and want to allow other concurrent readers.
2. **`PESSIMISTIC_WRITE`** (`SELECT ... FOR UPDATE`):
   - Acquires an exclusive lock. Other transactions cannot read (depending on DB isolation), update, or delete the row.
   - *Use Case*: Standard pessimistic locking. You intend to update the row and must prevent any other transaction from reading or writing to it until you are done.
3. **`PESSIMISTIC_FORCE_INCREMENT`**:
   - Acquires a pessimistic write lock and forcibly increments the entity's `@Version` column immediately, even if the entity's properties haven't changed.
   - *Use Case*: Rarely used, but applicable when modifying an entity's associated collection (e.g., adding a child) and you want to explicitly bump the parent's version to signal an update to other optimistic locks.

# Question 64: What are the limitations of Hibernate's L2 cache?

## Answer
1. **Invalidation Overhead**: If an entity is frequently updated, the L2 cache must constantly invalidate and update its entries across the application. This overhead can make the cache slower than just hitting the database.
2. **Distributed Inconsistency**: In a multi-node/microservice environment, a local L2 cache (like Ehcache) quickly becomes stale if Node A updates the DB while Node B still holds the old L2 cache. It requires a distributed cache (e.g., Hazelcast, Redis) to broadcast invalidations, adding network latency and complexity.
3. **Complex Associations**: Caching collections and associations can be brittle. Invalidating a single entity might require cascading invalidations across related cache regions, leading to unpredictable performance.
4. **Bypassed by Bulk Operations**: JPQL/Native SQL bulk updates (`@Modifying`) or external database modifications bypass the persistence context and do not invalidate the L2 cache, immediately leading to stale data.

# Question 65: What are the differences between Hibernate L2 cache and Spring's @Cacheable, and can they be used together?

## Answer
- **Hibernate L2 Cache**: Operates purely at the ORM/Database layer. It caches entities by their Primary Key. It is transparent to the application code; when you call `em.find()`, Hibernate automatically checks the L2 cache.
- **Spring `@Cacheable`**: Operates at the method/Service layer using Spring AOP. It caches the *return value* of an entire method based on the method's parameters. It can cache DTOs, complex lists, or calculation results.

**Can they be used together?**
Yes, they serve different purposes. You can use `@Cacheable` on a service method returning a DTO to avoid application logic overhead entirely. You can configure L2 cache for reference data entities (e.g., `Country`, `Currency`) so that any DB query for those entities across the app avoids network hits. However, be cautious: caching the same entity data in both layers wastes memory and complicates cache invalidation strategies.

# Question 132: What is the N+1 problem in Hibernate and how do you fix it? Do database indexes on foreign keys help?

## Answer
**The N+1 Problem:**
Occurs when you execute 1 query to fetch a list of *N* parent entities, and then loop through them, accessing a lazily-loaded association. Hibernate fires *N* additional queries to initialize each proxy, resulting in N+1 total database round-trips.

**How to fix it:**
Fetch the data eagerly at the query level using `JOIN FETCH` (in JPQL) or `@EntityGraph` (in Spring Data JPA). Alternatively, use `@BatchSize` to mitigate it by loading associations in batches.

**Do DB indexes on foreign keys help?**
While a foreign key index speeds up each individual query (since looking up children by the parent's ID is faster), **it does not solve the N+1 problem**. The core issue is network latency and query parsing overhead caused by executing N separate round-trips to the database, not strictly the execution time of a single query.

# Question 133: Explain the different @Transactional propagation levels. Specifically, how does NESTED work via savepoints?

## Answer
The main propagation levels are:
- **`REQUIRED`** (Default): Joins an existing transaction; creates a new one if none exists.
- **`REQUIRES_NEW`**: Suspends the existing transaction and starts a completely new, independent one.
- **`SUPPORTS`**: Joins an existing transaction if one exists; otherwise, executes non-transactionally.
- **`NOT_SUPPORTED`**: Suspends the current transaction and executes non-transactionally.
- **`MANDATORY`**: Requires an existing transaction; throws an exception if none exists.
- **`NEVER`**: Throws an exception if an active transaction exists.
- **`NESTED`**: Executes within a nested transaction if a current transaction exists. If not, behaves like `REQUIRED`.

**How `NESTED` works via savepoints:**
Unlike `REQUIRES_NEW` (which starts a separate DB connection/transaction), `NESTED` operates on the *same physical transaction*. It sets a JDBC **Savepoint** before executing the nested method.
- If the nested method fails, it rolls back *only* to the savepoint, allowing the outer transaction to catch the exception and potentially recover/commit.
- If the outer transaction rolls back, the nested changes are also rolled back.
- The nested transaction's changes are only truly committed when the outer transaction commits.
