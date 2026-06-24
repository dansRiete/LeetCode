# Question 1: How do you structure your tests (e.g. WebMvcTest) and integration tests and what do you typically mock versus run real?

## Answer

In a modern Spring Boot application, testing is structured across three main layers: **Unit Tests**, **Slice Tests**, and **Integration Tests** (the "Test Pyramid"). 

Here is how each layer is structured and the strategy for mocking versus running real components:

### 1. Unit Tests
*   **Focus:** Core business logic, domain models, and utility classes.
*   **Structure:** Standard JUnit 5 tests. No Spring context is loaded (`@SpringBootTest` is avoided) to keep tests lightning-fast.
*   **Mock vs. Real:**
    *   **Real:** The class under test and simple data objects/value objects.
    *   **Mocked:** All external dependencies (services, repositories) are mocked using Mockito (`@Mock`, `@InjectMocks`).

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    @Test
    void shouldReturnUser() {
        // Unit test logic here
    }
}
```

### 2. Slice Tests (Layer Isolation)
Spring Boot provides "slice" testing annotations to test specific layers of the application by loading a partial Spring Context.

*   **`@WebMvcTest` (Controllers):**
    *   **Focus:** HTTP routing, request/response serialization (Jackson), input validation (`@Valid`), and exception handling (`@ControllerAdvice`).
    *   **Mock vs. Real:**
        *   **Real:** Spring MVC infrastructure, the Controller class, security filters (if applicable).
        *   **Mocked:** The service layer is mocked using `@MockBean` because the goal is *only* to test the web layer, not the business logic.

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService; // Mocked service

    @Test
    void shouldReturn200() throws Exception {
        when(userService.getUser(1L)).thenReturn(new User("Alice"));
        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }
}
```

*   **`@DataJpaTest` (Repositories):**
    *   **Focus:** Custom JPQL/native queries, database constraints, and ORM mapping.
    *   **Mock vs. Real:**
        *   **Real:** Spring Data JPA, Hibernate, and the database itself (often via Testcontainers or an in-memory DB like H2).
        *   **Mocked:** Nothing is mocked here.

### 3. Integration Tests (`@SpringBootTest`)
*   **Focus:** End-to-end flow testing across all layers (Controller -> Service -> Repository -> DB) to ensure components wire together correctly.
*   **Structure:** Loads the full Spring Application Context. Requests are typically driven via `TestRestTemplate` or `WebTestClient`.
*   **Mock vs. Real:**
    *   **Real:** Almost everything. Controllers, services, and repositories all execute real code.
    *   **Real (via Testcontainers):** Databases (PostgreSQL, MongoDB), message brokers (Kafka), and caches (Redis) should be run as real Docker containers using **Testcontainers**. This prevents flakiness associated with in-memory substitutes like H2 behaving differently than production.
    *   **Mocked (External APIs):** External third-party REST services (e.g., Stripe, AWS, an external microservice) should be mocked using **WireMock**. This allows testing actual HTTP calls and timeouts without hitting real external networks. If WireMock is overkill, `@MockBean` on the Feign/WebClient wrapper is an acceptable alternative.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void createUserFlow() {
        // Sends real HTTP request, hits real DB in Testcontainers
        ResponseEntity<User> response = restTemplate.postForEntity("/users", new CreateUserReq(), User.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
```

### Summary of "Mock vs. Real" Strategy
*   **Business Logic/Algorithms:** Always Real (isolated Unit Tests).
*   **Web Layer (MVC):** Real infrastructure, Mocked services (`@WebMvcTest`).
*   **Databases/Brokers:** Real, provided by **Testcontainers** (`@SpringBootTest` / `@DataJpaTest`).
*   **External HTTP APIs:** Mocked via **WireMock**.
*   **Time/Clocks:** Use `java.time.Clock` as a Spring Bean and mock it during tests to verify time-sensitive logic deterministically.
