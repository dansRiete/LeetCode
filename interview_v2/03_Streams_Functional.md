# Question 1: What is a functional interface in Java, and why are they important for Lambdas and the Streams API?

## Answer

A functional interface in Java is an interface that contains exactly one abstract method (often called a Single Abstract Method or SAM type). It can, however, contain any number of `default` or `static` methods. The `@FunctionalInterface` annotation is optionally used to enforce this rule at compile time, preventing the accidental addition of more abstract methods.

**Importance for Lambdas and the Streams API:**
Functional interfaces are the foundation of Lambda expressions in Java. Because they have only one abstract method, the Java compiler can infer the method signature and use a lambda expression as a concise, inline implementation of that interface. 

In the Streams API, methods like `filter`, `map`, and `reduce` rely entirely on standard functional interfaces defined in the `java.util.function` package (e.g., `Predicate<T>`, `Function<T, R>`, `Consumer<T>`, `Supplier<T>`). Passing a lambda expression is functionally equivalent to passing an implementation of the corresponding functional interface, allowing for highly readable, declarative data processing.

**Example:**
```java
@FunctionalInterface
public interface StringProcessor {
    String process(String input);
}

// 1. Implementing using a Lambda Expression
StringProcessor toUpperCase = s -> s.toUpperCase();
System.out.println(toUpperCase.process("hello")); // Output: HELLO

// 2. Streams API Usage
List<String> names = Arrays.asList("Alice", "Bob");
names.stream()
     .filter(name -> name.startsWith("A")) // Targets Predicate<String> (boolean test(T t))
     .forEach(System.out::println);       // Targets Consumer<String> (void accept(T t))
```

# Question 2: Explain Java's Optional class and the common 'orElse' and 'orElseGet' patterns. When should you use Optional?

## Answer

`Optional<T>` is a container object introduced in Java 8 that may or may not contain a non-null value. It serves as an explicit API-level construct to signify that a value might be absent, encouraging developers to handle the absent case and mitigating the risk of a `NullPointerException` (NPE).

**`orElse` vs. `orElseGet`:**
Both methods are used to provide a default value when the `Optional` is empty, but they differ significantly in execution semantics:
- **`orElse(T other)`**: Evaluates the `other` argument *eagerly*. The default value is computed or instantiated regardless of whether the `Optional` is empty or present.
- **`orElseGet(Supplier<? extends T> supplier)`**: Evaluates the supplier *lazily*. The `supplier` is only invoked if the `Optional` is actually empty. This is crucial for performance if the default value is expensive to create or involves side effects (like a database call).

**When to use `Optional`:**
- **As Return Types:** This is the primary intended use case. Methods that search for a value or retrieve data (e.g., `findUserById`) should return an `Optional<User>` instead of `null` if the value might not exist.
- **Do NOT use as Class Fields:** `Optional` is not `Serializable`, which can cause issues with frameworks and persistence mechanisms.
- **Do NOT use as Method Parameters:** It forces callers to wrap values in `Optional.of()` or `Optional.empty()`, making the calling code verbose. Simple `null` checks or overloaded methods are preferred here.

**Example:**
```java
public Optional<User> findUser(String id) {
    // Returns Optional.of(user) or Optional.empty()
}

// orElse: new User() is instantiated every time, even if findUser returns a present Optional.
User user1 = findUser("123").orElse(new User("default_id")); 

// orElseGet: new User() is ONLY instantiated if findUser returns an empty Optional.
User user2 = findUser("123").orElseGet(() -> new User("default_id"));
```
