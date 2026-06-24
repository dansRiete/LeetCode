# Question 1: What is the contract between equals() and hashCode()? What happens to a HashMap if hashCode() always returns 42?

## Answer
The contract between `equals()` and `hashCode()` is fundamental for hash-based collections:
1. **Consistency with `equals`**: If two objects are equal according to the `equals(Object)` method, they **must** have the same `hashCode()`.
2. **Unequal objects**: If two objects have the same `hashCode()`, they are *not* required to be equal according to `equals(Object)`. (However, distinct hash codes for unequal objects improve performance).
3. **Internal consistency**: The `hashCode()` must consistently return the same integer across multiple invocations during an application's execution, provided no information used in `equals` comparisons is modified.

**If `hashCode()` always returns 42:**
All instances will map to the exact same bucket index in a `HashMap`. This causes a massive hash collision. Instead of distributing elements across the array, the `HashMap` degrades into a single linked list (or a balanced tree in Java 8+). The time complexity for `get()` and `put()` operations drops from O(1) constant time to O(N) or O(log N) worst-case time, resulting in severe performance degradation.

# Question 2: Explain the internal structure of a HashMap in Java. How are collisions handled and what happens when a bucket has too many elements?

## Answer
A `HashMap` in Java is internally backed by an array of nodes (buckets). By default, it initializes with a capacity of 16 and a load factor of 0.75. Each node stores the hash, the key, the value, and a reference to the next node.

**Handling Collisions:**
Collisions occur when two different keys produce hashes that map to the same bucket index. Java handles this using **chaining**. The new entry is appended to a linked list at that specific bucket.

**When a bucket has too many elements:**
Since Java 8, to mitigate the worst-case O(N) lookup time of excessively long linked lists, a bucket converts its internal structure into a balanced Red-Black Tree. This **treeification** happens when:
- The number of elements in a single bucket reaches the `TREEIFY_THRESHOLD` (default is 8).
- The overall array capacity is at least `MIN_TREEIFY_CAPACITY` (default is 64).

This optimization brings the worst-case lookup time down from O(N) to O(log N). If elements are removed and the bucket size drops below the `UNTREEIFY_THRESHOLD` (default is 6), the tree converts back into a linked list. 
Additionally, when the total number of elements exceeds the map's capacity multiplied by its load factor, the entire `HashMap` resizes (doubles the array size) and rehashes all elements.

# Question 3: What is the difference between checked and unchecked exceptions in Java? When should you use each?

## Answer
**Checked Exceptions:**
- **Definition**: Exceptions that inherit from `Exception` (but not from `RuntimeException`).
- **Enforcement**: Checked at compile-time. The compiler forces the programmer to handle them using a `try-catch` block or declare them in the method signature using the `throws` keyword.
- **When to use**: Use them for expected, recoverable scenarios that are outside the immediate control of the program. Examples include network failures, missing files (`IOException`), or database connectivity issues (`SQLException`).

**Unchecked Exceptions:**
- **Definition**: Exceptions that inherit from `RuntimeException`.
- **Enforcement**: Not checked at compile-time. The compiler does not mandate handling or declaring them.
- **When to use**: Use them for programming errors, logic bugs, or unrecoverable system states where catching the exception doesn't help fix the issue. Examples include `NullPointerException`, `IllegalArgumentException` (e.g., passing invalid arguments to a method), or `IndexOutOfBoundsException`.

# Question 4: Explain the difference between Java's Comparable and Comparator interfaces. When would you use each?

## Answer
**`Comparable`:**
- **Purpose**: Defines the *natural* or *default* ordering of objects.
- **Implementation**: The class whose instances are to be sorted must implement `Comparable<T>`.
- **Method**: Requires overriding the `compareTo(T o)` method.
- **When to use**: When an object has a logical default sorting order. For example, sorting `String` alphabetically or `Employee` objects by `employeeId`.
  ```java
  public class Employee implements Comparable<Employee> {
      private int employeeId;
      public int compareTo(Employee other) {
          return Integer.compare(this.employeeId, other.employeeId);
      }
  }
  ```

**`Comparator`:**
- **Purpose**: Defines an *external* or *custom* ordering of objects.
- **Implementation**: Created as a separate class, an anonymous inner class, or a lambda expression implementing `Comparator<T>`.
- **Method**: Requires overriding the `compare(T o1, T o2)` method.
- **When to use**: When you need to sort objects in a way that differs from their natural ordering, or when you cannot modify the source code of the class you are sorting.
  ```java
  // Custom sorting by salary instead of natural ID
  Comparator<Employee> bySalary = Comparator.comparing(Employee::getSalary);
  ```

# Question 5: What are the key differences between Java's LinkedList and ArrayDeque? When would you choose one over the other?

## Answer
**`LinkedList`:**
- **Internal Structure**: A doubly-linked list.
- **Interfaces**: Implements both `List` and `Deque`.
- **Memory Overhead**: High, because each element is wrapped in a node object containing pointers to the previous and next nodes.
- **Nulls**: Allows `null` elements.
- **When to use**: Primarily when you strictly need O(1) insertions/deletions from the *middle* of the collection using a `ListIterator`.

**`ArrayDeque`:**
- **Internal Structure**: A resizable circular array.
- **Interfaces**: Implements `Deque` (Queue and Stack behaviors), but *not* `List`.
- **Memory Overhead**: Low. Elements are stored contiguously in an array, offering excellent cache locality and faster iteration.
- **Nulls**: Does not allow `null` elements.
- **When to use**: Almost always preferred over `LinkedList` when implementing a Queue (FIFO) or Stack (LIFO).

**Conclusion:** `ArrayDeque` is significantly more memory-efficient and performant for standard queue and stack operations due to better CPU cache locality. Only use `LinkedList` if you need `List` functionality with frequent mid-list insertions/removals.

# Question 6: Briefly explain Java Records, Lambdas, and Streams. What benefits do they bring to modern Java development?

## Answer
**Records** (Introduced in Java 14):
- **Concept**: A concise way to declare immutable data carrier classes. 
- **Benefits**: It eliminates boilerplate code. The compiler automatically generates the canonical constructor, `equals()`, `hashCode()`, `toString()`, and read-only accessor methods (e.g., `name()` instead of `getName()`). It is ideal for DTOs and value objects.
  ```java
  public record Point(int x, int y) {}
  ```

**Lambdas** (Introduced in Java 8):
- **Concept**: Anonymous, inline functions that can be passed around as objects. Represented as `(parameters) -> body`.
- **Benefits**: Enables functional programming paradigms and heavily reduces the verbosity of anonymous inner classes. They make the code more readable, especially when passing behaviors to methods.
  ```java
  list.forEach(item -> System.out.println(item));
  ```

**Streams** (Introduced in Java 8):
- **Concept**: A declarative API to process sequences of elements (collections, arrays) functionally. Supports operations like `map`, `filter`, `reduce`, and `collect`.
- **Benefits**: Allows developers to express complex data transformations cleanly and concisely. It abstracts away looping and mutating state. Additionally, streams support trivial parallelization via `parallelStream()`, maximizing multi-core architectures without writing low-level threading code.
  ```java
  List<String> names = employees.stream()
      .filter(e -> e.getSalary() > 50000)
      .map(Employee::getName)
      .toList();
  ```
