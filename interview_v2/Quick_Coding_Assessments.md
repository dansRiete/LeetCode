# Quick Coding Assessments (Java & SQL)

These are short, common coding snippets frequently asked during technical screens to quickly gauge your familiarity with Java syntax (especially Streams/Lambdas) and fundamental SQL queries.

## Java Assessments

### 1. Write a Lambda function to concatenate two strings.
```java
BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
System.out.println(concat.apply("Hello, ", "World!"));
```

### 2. Given a list of integers, use Java Streams to find the sum of all even numbers.
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
int sumOfEvens = numbers.stream()
                        .filter(n -> n % 2 == 0)
                        .mapToInt(Integer::intValue)
                        .sum();
```

### 3. Given a list of strings, use Streams to group them by their length.
```java
List<String> words = Arrays.asList("apple", "bat", "cat", "banana");
Map<Integer, List<String>> groupedByLength = words.stream()
        .collect(Collectors.groupingBy(String::length));
```

### 4. Find the first non-repeated character in a string using Java Streams.
```java
String input = "swiss";
Character firstNonRepeated = input.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
    .entrySet().stream()
    .filter(entry -> entry.getValue() == 1)
    .map(Map.Entry::getKey)
    .findFirst()
    .orElse(null);
```

### 5. Write a method to check if a string is a palindrome.
```java
public boolean isPalindrome(String str) {
    if (str == null) return false;
    String reversed = new StringBuilder(str).reverse().toString();
    return str.equals(reversed);
}
```

### 6. Write a thread-safe Singleton class in Java.
```java
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### 7. Swap two numbers without using a temporary variable.
```java
int a = 10;
int b = 20;
a = a + b; // a becomes 30
b = a - b; // b becomes 10
a = a - b; // a becomes 20
```

### 8. Write a custom Functional Interface and use it.
```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

// Usage
MathOperation addition = (a, b) -> a + b;
System.out.println(addition.operate(5, 3)); // Outputs 8
```

---

## SQL Assessments

### 9. Write a SQL query to find the second highest salary from an `Employee` table.
```sql
-- Approach 1 (Standard):
SELECT MAX(salary) FROM Employee 
WHERE salary < (SELECT MAX(salary) FROM Employee);

-- Approach 2 (Offset):
SELECT DISTINCT salary FROM Employee 
ORDER BY salary DESC 
LIMIT 1 OFFSET 1;
```

### 10. Write a SQL query to find duplicate rows based on an email column.
```sql
SELECT email, COUNT(email)
FROM Users
GROUP BY email
HAVING COUNT(email) > 1;
```

### 11. Write a SQL query to fetch the top 3 highest-paid employees per department.
```sql
WITH RankedEmployees AS (
    SELECT name, department_id, salary,
           DENSE_RANK() OVER(PARTITION BY department_id ORDER BY salary DESC) as rank
    FROM Employee
)
SELECT name, department_id, salary
FROM RankedEmployees
WHERE rank <= 3;
```

### 12. Write a SQL query to find employees who do not have a manager.
```sql
SELECT employee_name 
FROM Employee 
WHERE manager_id IS NULL;
```

### 13. Write a SQL query to get the number of employees in each department, even if the department has no employees.
```sql
SELECT d.department_name, COUNT(e.employee_id) as employee_count
FROM Department d
LEFT JOIN Employee e ON d.department_id = e.department_id
GROUP BY d.department_name;
```

### 14. Explain how to delete duplicate records from a table keeping only the one with the lowest ID.
```sql
DELETE FROM Employee 
WHERE id NOT IN (
    SELECT MIN(id) 
    FROM Employee 
    GROUP BY email
);
```

### 15. Write a SQL query to find all employees whose names start with the letter 'A' and contain exactly 5 letters.
```sql
SELECT name 
FROM Employee 
WHERE name LIKE 'A____'; -- 'A' followed by exactly 4 underscores
```
