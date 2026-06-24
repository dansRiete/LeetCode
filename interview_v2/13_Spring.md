# Question 1: What is Spring Boot auto-configuration and how does it work under the hood?
## Answer
Auto-configuration is a mechanism in Spring Boot that automatically configures beans based on classpath dependencies and existing configurations. Under the hood, it's enabled via `@EnableAutoConfiguration` (part of `@SpringBootApplication`). It uses `META-INF/spring.factories` (or `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in Spring Boot 2.7+) to load `AutoConfiguration` classes. These classes rely heavily on `@Conditional` annotations like `@ConditionalOnClass`, `@ConditionalOnMissingBean`, and `@ConditionalOnProperty`, which ensure auto-configured beans gracefully "back off" if the user explicitly defines their own beans.

# Question 2: How do you exclude a specific auto-configuration class, and why would you need to?
## Answer
You can exclude an auto-configuration class using the `exclude` parameter on the main application annotation (e.g., `@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)`) or via the `spring.autoconfigure.exclude` property in your `application.yml`. You would do this to prevent Spring from attempting to configure components you don't need (e.g., disabling a default database connection attempt during a specific test profile), to override a default security configuration, or to resolve conflicts when providing a custom implementation.

# Question 3: What is the difference between constructor injection and field injection? Which is preferred and why?
## Answer
Constructor injection passes dependencies through the class constructor, while field injection uses reflection to inject dependencies directly into fields (via `@Autowired`).
**Constructor Injection is highly preferred** because:
1. It enforces mandatory dependencies, ensuring the bean is never created in an invalid state.
2. It allows fields to be marked as `final` (immutable).
3. It simplifies unit testing without the Spring container (you can instantiate the class directly with `new MyService(mock)`).
4. It allows Spring to detect circular dependencies cleanly at application startup.
Field injection, while less verbose, hides dependencies, couples the class tightly to the DI container, and can silently allow circular dependencies leading to runtime errors.

# Question 4: Explain the Spring bean lifecycle from instantiation to destruction. Name the key callback points.
## Answer
The core lifecycle steps for a standard singleton bean are:
1. **Instantiation**: Spring creates the bean instance (the constructor is called).
2. **Dependency Injection**: Spring populates properties and resolves injected dependencies.
3. **Aware Interfaces**: Spring injects container infrastructure if the bean implements interfaces like `BeanNameAware` or `ApplicationContextAware`.
4. **Pre-Initialization (`BeanPostProcessor`)**: `postProcessBeforeInitialization` is called. This is where methods annotated with `@PostConstruct` execute.
5. **Initialization**: Custom init methods execute (`InitializingBean.afterPropertiesSet()`, followed by any custom `@Bean(initMethod=...)`).
6. **Post-Initialization (`BeanPostProcessor`)**: `postProcessAfterInitialization` is called. This is critical as it's often where AOP proxies (for transactions, caching, etc.) are generated.
7. **Destruction**: On context shutdown, methods annotated with `@PreDestroy` execute, followed by `DisposableBean.destroy()`, and then custom `@Bean(destroyMethod=...)`.

# Question 5: How does Spring resolve a circular dependency between two beans?
## Answer
For singleton beans using field or setter injection, Spring resolves circular dependencies using a "three-level cache" (singleton objects, early singleton objects, singleton factories). It exposes an "early reference" (a partially initialized bean) before dependency injection completes, allowing the cycle to resolve.
However, if **constructor injection** is used, Spring fails immediately with a `BeanCurrentlyInCreationException` because neither object can be fully instantiated first.
*Note:* As of Spring Boot 2.6+, circular dependencies are prohibited by default even for field injection. They indicate poor architectural design, but can be temporarily worked around by injecting a lazy proxy using `@Lazy`.

# Question 6: What are the Spring bean scopes? What is the default, and what is the risk of injecting a prototype bean into a singleton?
## Answer
The core Spring bean scopes are `singleton` (the default), `prototype` (a new instance created every time it is injected/requested), `request`, `session`, `application`, and `websocket`.
**The Risk**: If you inject a `prototype` bean into a `singleton` bean, the injection happens only once (at application startup). Therefore, the singleton holds a single, reused instance of the prototype, completely defeating the purpose of the prototype scope.
**The Fix**: To get a fresh prototype instance each time, you should use `ObjectProvider<MyPrototypeBean>`, `@Lookup` method injection, or declare the prototype with a scoped proxy mode (`@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)`).

# Question 7: What is the difference between @Controller and @RestController?
## Answer
Both annotations mark a class as a Spring MVC controller.
*   `@Controller`: Typically used for traditional MVC web applications. Methods usually return a `String` representing a view name, which is resolved by a `ViewResolver` (e.g., Thymeleaf or JSP) into HTML.
*   `@RestController`: A convenience composed annotation that combines `@Controller` and `@ResponseBody`. Method return values bypass the view resolver entirely and are serialized directly into the HTTP response body (typically into JSON or XML via an `HttpMessageConverter`).

# Question 8: How does @ControllerAdvice and @ExceptionHandler work for global exception handling?
## Answer
`@ControllerAdvice` (or `@RestControllerAdvice` for APIs) acts as a global interceptor for exceptions thrown by controllers. Inside it, you define methods annotated with `@ExceptionHandler(CustomException.class)`.
When a controller throws an exception, Spring's `HandlerExceptionResolver` scans the advice classes for an `@ExceptionHandler` that matches the exception type. The matching method executes, allowing you to intercept the error centrally, set specific HTTP status codes (often via `@ResponseStatus` or by returning a `ResponseEntity`), and return a standardized error response (e.g., a uniform JSON payload) across the entire application.

# Question 9: What is the difference between @RequestParam, @PathVariable, and @RequestBody?
## Answer
*   `@RequestParam`: Extracts values from URL query parameters (e.g., `/api?id=5`) or form data. It supports attributes like `required=false` and `defaultValue`.
*   `@PathVariable`: Extracts values dynamically from URI template path segments (e.g., `/api/users/{id}`).
*   `@RequestBody`: Reads the entire raw HTTP request body and deserializes it into a Java object using an `HttpMessageConverter` (usually Jackson for JSON). It is typically paired with `@Valid` to enforce input validation on incoming DTOs.

# Question 10: What is the difference between @Before, @After, @AfterReturning, @AfterThrowing, and @Around advice?
## Answer
These are Spring AOP advice annotations dictating *when* cross-cutting logic executes relative to a method (join point):
*   `@Before`: Runs just before the method execution.
*   `@After`: Runs after the method completes, regardless of success or failure (similar to a `finally` block).
*   `@AfterReturning`: Runs only if the method completes successfully without throwing an exception.
*   `@AfterThrowing`: Runs only if the method throws an exception.
*   `@Around`: The most powerful advice type. It wraps the method invocation completely. You are passed a `ProceedingJoinPoint` and must manually call `.proceed()` to execute the target method. It allows you to alter arguments, modify the return value, or swallow/transform exceptions.

# Question 11: What is the main limitation of Spring AOP, and when would it silently not work?
## Answer
Spring AOP relies heavily on runtime proxies (JDK dynamic proxies or CGLIB). Its primary limitation is that **self-invocation bypasses the proxy**. 
If method A calls method B, and both are within the *same class*, the call uses the internal `this` reference rather than the Spring proxy. Consequently, any AOP annotations on method B (such as `@Transactional`, `@Async`, or `@Cacheable`) will be completely ignored and silently fail to execute. Additionally, Spring AOP only works on public methods and requires the object to be managed as a Spring bean.

# Question 12: How does the Spring Security filter chain work? What is SecurityFilterChain?
## Answer
Spring Security operates as a series of standard Servlet Filters following the Chain of Responsibility pattern. The core integration point is the `DelegatingFilterProxy`, which bridges the servlet container to the Spring context. It delegates to the `FilterChainProxy`, which manages one or more `SecurityFilterChain` beans.
The `SecurityFilterChain` (configured via `HttpSecurity`) contains a sequence of ordered, built-in filters (e.g., `UsernamePasswordAuthenticationFilter`, `BasicAuthenticationFilter`, `AuthorizationFilter`). These filters handle authentication, CSRF protection, and authorization. If a filter successfully processes a request or denies access, it can short-circuit the chain and return a response without passing the request to the underlying Spring MVC `DispatcherServlet`.

# Question 13: What is the difference between authentication and authorization in Spring Security?
## Answer
*   **Authentication**: Verifying *who* the user is (Identity). It answers "Are you who you say you are?" In Spring, this is handled by components like `AuthenticationManager` and `AuthenticationProvider`, and the result is stored in the `SecurityContext`.
*   **Authorization**: Verifying *what* the authenticated user is allowed to do (Access Control). It answers "Do you have permission to access this resource?" This happens after authentication, managed by components like the `AuthorizationFilter` or method-level security interceptors checking `GrantedAuthority` records.

# Question 14: What is the difference between @Value and @ConfigurationProperties? When would you use each?
## Answer
*   `@Value`: Used to inject a single property value into a field. It supports SpEL expressions and default fallbacks (e.g., `@Value("${app.url:http://default}")`). It is best for simple, isolated, or standalone properties.
*   `@ConfigurationProperties`: Binds a hierarchical group of properties to a structured Java bean (POJO). It supports type-safety, validation (via `@Valid`), and relaxed binding (automatically mapping `my-prop` to `myProp`). It is highly preferred for grouping related configurations (e.g., database credentials, cloud provider settings) into a cohesive, testable object.

# Question 15: How do Spring profiles work? How do you activate a profile and how do you define profile-specific beans?
## Answer
Spring Profiles allow you to segregate application configuration and register beans conditionally based on the environment (e.g., `dev`, `test`, `prod`).
*   **Activating**: You can activate a profile via `application.properties` (`spring.profiles.active=dev`), JVM command-line arguments (`-Dspring.profiles.active=dev`), or OS environment variables (`SPRING_PROFILES_ACTIVE=dev`).
*   **Defining Beans**: You define profile-specific beans by adding the `@Profile("dev")` annotation alongside `@Component`, `@Configuration`, or `@Bean`. You can also provide profile-specific property files (e.g., `application-dev.yml` overriding `application.yml`).

# Question 16: How does @Cacheable work? What is the default cache key, and how do you customize it?
## Answer
`@Cacheable` uses AOP to intercept method calls. Before executing the method, it checks the configured cache to see if the result already exists for the given key. If found, it returns the cached value immediately. If not, the method executes, and the result is stored in the cache.
*   **Default Key**: Spring uses a `KeyGenerator` that defaults to a combination of all method parameters (no parameters = `SimpleKey.EMPTY`, one parameter = the parameter itself, multiple parameters = a combined `SimpleKey`).
*   **Customization**: You can customize the key using SpEL in the annotation's `key` attribute (e.g., `@Cacheable(value="users", key="#user.id")`) or by specifying a custom `keyGenerator` bean.

# Question 17: What is the difference between @CacheEvict and @CachePut?
## Answer
*   `@CachePut`: **Always** executes the underlying method and updates the cache with the new return value. It is primarily used for update operations to ensure the cache stays fresh.
*   `@CacheEvict`: Removes one or more entries from the cache entirely. You can evict specific keys or clear the whole cache (`allEntries=true`). It is primarily used for delete operations or when underlying data is known to be stale.

# Question 18: What is the chunk-oriented processing model in Spring Batch? Explain the roles of ItemReader, ItemProcessor, and ItemWriter.
## Answer
Chunk-oriented processing optimizes batch jobs by handling data in chunks inside a single transaction boundary rather than one row at a time.
*   **ItemReader**: Reads data sequentially, one item at a time, until the chunk size is reached.
*   **ItemProcessor**: Transforms or filters the read item. It processes items one at a time (returning `null` will filter the item out of the final write).
*   **ItemWriter**: Receives a `Chunk` (a List) of the processed items and writes them out in bulk (e.g., a batch database insert). If the write succeeds, the entire chunk's transaction commits simultaneously.

# Question 19: What is a JobParameter in Spring Batch and why is it important for re-running jobs?
## Answer
A `JobParameter` is a key-value pair passed to a Job at runtime (e.g., `run.date=2024-01-01`).
Spring Batch identifies a unique `JobInstance` by the combination of its Job Name and its identifying `JobParameters`. This is crucial for re-running jobs: if a job fails halfway, re-executing it with the *exact same* parameters resumes the existing `JobInstance` from where it left off. To run the job entirely anew, you must vary at least one identifying parameter (like a timestamp).

# Question 20: What does @Lazy do in Spring? How does it help with circular dependencies and what is the tradeoff?
## Answer
`@Lazy` defers the initialization of a bean until it is actually requested, rather than eagerly loading it at application startup.
*   **Circular Dependencies**: If Bean A and Bean B depend on each other, applying `@Lazy` to the injected field or constructor argument in Bean A tells Spring to inject an uninitialized proxy instead of the fully realized Bean B. Bean B is only instantiated when A invokes a method on the proxy, breaking the startup cycle.
*   **Tradeoff**: You lose fail-fast early validation. Configuration errors, missing dependencies, or instantiation failures for the lazy bean will not be caught at startup. Instead, they will throw runtime exceptions when the application is actively serving requests.

# Question 21: What is the Spring MVC request lifecycle? Walk through what happens from HTTP request to response.
## Answer
1.  **Filter Chain**: The HTTP request enters the Servlet container and passes through Servlet Filters (including the Spring Security chain).
2.  **DispatcherServlet**: The central Front Controller receives the request.
3.  **HandlerMapping**: The `DispatcherServlet` consults the `HandlerMapping` to find the appropriate Controller method based on URL and HTTP method.
4.  **HandlerInterceptors**: Any registered `preHandle` interceptors are executed.
5.  **HandlerAdapter**: Resolves method arguments (e.g., converting JSON via `HttpMessageConverters` for `@RequestBody`) and invokes the Controller method.
6.  **Controller**: Business logic executes and returns a result.
7.  **Message Conversion/View Resolution**: If `@RestController`, an `HttpMessageConverter` serializes the return value directly to JSON. If `@Controller`, a `ViewResolver` resolves the view template.
8.  **HandlerInterceptors**: `postHandle` and `afterCompletion` interceptors execute.
9.  **Response**: The final HTTP response is sent back to the client.

# Question 22: What is the difference between @RequestMapping, @GetMapping, @PostMapping?
## Answer
*   `@RequestMapping`: The foundational annotation for mapping requests. Crucially, if used without the `method` attribute, it defaults to matching **ALL** HTTP methods (GET, POST, PUT, etc.), which can be a security and design flaw. It is also often used at the class level to define a base path.
*   `@GetMapping` / `@PostMapping`: Composed annotations introduced in Spring 4.3 that act as strict shortcuts for `@RequestMapping(method = RequestMethod.GET)` and `POST`. They make the code much more readable and explicitly enforce correct HTTP method matching.

# Question 23: What HTTP status codes would you return for: successful creation, resource not found, validation error, and server error?
## Answer
*   **Successful creation**: `201 Created`
*   **Resource not found**: `404 Not Found`
*   **Validation error**: `400 Bad Request`
*   **Server error**: `500 Internal Server Error`
*(Bonus: `204 No Content` is the standard for a successful deletion or update with no response body).*

# Question 24: What is content negotiation in Spring MVC and how does it work?
## Answer
Content negotiation allows Spring MVC to serve different resource representations (e.g., JSON, XML) from the exact same URI.
Spring determines the requested format typically by inspecting the client's HTTP `Accept` header. It then iterates through registered `HttpMessageConverters` to find one that supports both the Controller's Java return type and the requested media type. You can explicitly restrict formats using the `produces` attribute on mapping annotations (e.g., `@GetMapping(produces = "application/json")`). If no suitable converter is found, Spring returns a `406 Not Acceptable` error.

# Question 25: How do you handle validation in a Spring REST controller? What annotations are involved?
## Answer
You handle validation by including the `spring-boot-starter-validation` dependency.
1. Annotate the fields inside your DTO with constraints like `@NotBlank`, `@Min`, or `@Email`.
2. In the Controller, annotate the incoming `@RequestBody` parameter with `@Valid` (or `@Validated` if using validation groups).
3. If validation fails, Spring automatically throws a `MethodArgumentNotValidException`.
4. You catch this exception globally using `@RestControllerAdvice`, extract the structured `FieldErrors`, and return a clean, user-friendly `400 Bad Request` response.

# Question 26: What is JWT and how does Spring Security validate a JWT token on each request?
## Answer
JWT (JSON Web Token) is a stateless, self-contained token consisting of a Base64-encoded Header, Payload (claims), and Signature.
In Spring Security, you typically validate it by implementing a custom `OncePerRequestFilter`. On each request, this filter extracts the JWT from the `Authorization: Bearer` header. It cryptographically verifies the signature and expiration using a library (e.g., JJWT). If valid, the filter extracts the user's details/authorities from the claims, creates an `Authentication` token (like `UsernamePasswordAuthenticationToken`), and places it into the `SecurityContextHolder`. The filter is typically injected via `addFilterBefore(..., UsernamePasswordAuthenticationFilter.class)`.

# Question 27: What is the difference between @PreAuthorize and @Secured? How do you enable method-level security?
## Answer
Both enforce security directly on method executions, and both require adding `@EnableMethodSecurity` to a configuration class to function (otherwise they are silently ignored).
*   `@Secured`: A legacy annotation that only accepts simple, hardcoded role lists (e.g., `@Secured("ROLE_ADMIN")`).
*   `@PreAuthorize`: The modern, highly flexible annotation that evaluates Spring Expression Language (SpEL). It allows for complex logic, such as checking method arguments (`@PreAuthorize("#user.id == authentication.principal.id")`), combining multiple authorities (`hasRole('ADMIN') and hasAuthority('WRITE')`), or dynamically calling secondary bean methods.

# Question 28: How does Spring Security handle CSRF protection? When would you disable it?
## Answer
CSRF (Cross-Site Request Forgery) protection prevents malicious sites from tricking a user's browser into submitting state-changing POST/PUT requests to an application where they are logged in via cookies. Spring Security mitigates this by generating a unique, synchronized token on the server and requiring it to be included in all mutating requests.
**Disabling it**: You typically disable CSRF (`http.csrf(AbstractHttpConfigurer::disable)`) in **stateless REST APIs** that use token-based authentication (like JWT) sent via the `Authorization` header. Browsers do not automatically attach custom headers across domains, which inherently prevents CSRF exploits without needing a sync token.

# Question 29: What is the difference between ROLE_ prefix and authority in Spring Security?
## Answer
In Spring Security, a `GrantedAuthority` represents a granular, specific permission (e.g., `READ_DOCUMENT`, `WRITE_PRIVILEGE`).
A "Role" is essentially a coarse-grained authority that conventionally starts with the prefix `ROLE_`. When you use role-checking methods like `hasRole('ADMIN')` in SpEL, Spring Security automatically prepends the `ROLE_` prefix under the hood and checks the authorities list for `ROLE_ADMIN`. If you use `hasAuthority('ADMIN')`, it checks exactly for the literal string `ADMIN`.

# Question 30: How would you implement stateless authentication in a Spring Boot REST API?
## Answer
1. In your `SecurityFilterChain`, set the session creation policy to `STATELESS` (`http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)`) so Spring does not create `HttpSession` cookies.
2. Disable CSRF, as it is unnecessary for stateless token APIs.
3. Expose an authentication endpoint (e.g., `/login`) that validates credentials and issues a signed JWT.
4. Implement a custom `OncePerRequestFilter` that intercepts incoming requests, parses the JWT from the `Authorization` header, cryptographically validates it, and temporarily populates the `SecurityContextHolder` with an `Authentication` object for the lifespan of that single request.

# Question 31: What is the difference between OAuth2 and OIDC (OpenID Connect)? What does each solve?
## Answer
*   **OAuth2** is strictly an **authorization** framework. It allows an application to obtain limited access (via an Access Token) to a user's resources on another service, without exposing the user's credentials. It does not provide any standardized way to verify the user's identity.
*   **OIDC** is an **authentication** layer built on top of OAuth2. It adds an `id_token` (a JWT) alongside the standard access token. The `id_token` contains verifiable claims about the user's identity (email, name, sub). OIDC solves Identity/AuthN ("who the user is"), while OAuth2 solves Delegated AuthZ ("what the application can do").

# Question 32: In OAuth2, what is the difference between the Authorization Code flow and Client Credentials flow? When would you use each?
## Answer
*   **Authorization Code Flow**: Involves a human user and browser redirects. The user authenticates with the Identity Provider (IdP), the application receives a temporary code, and the backend securely exchanges that code for an Access Token. It is used for web/mobile apps where users need to log in.
*   **Client Credentials Flow**: Involves machine-to-machine communication without a user context. The client application authenticates directly to the IdP using its own `client_id` and `client_secret` to obtain an Access Token. It is used for background daemons or microservices calling other secure APIs.

# Question 33: What is Okta and how does Spring Security integrate with an external identity provider like Okta using OAuth2/OIDC?
## Answer
Okta is a managed, enterprise-grade Identity Provider (IdP) that implements OAuth2 and OIDC specifications.
Spring Security seamlessly integrates with Okta using `spring-boot-starter-oauth2-resource-server` (for backend APIs). You simply define the Okta issuer URI in your `application.yml` (`spring.security.oauth2.resourceserver.jwt.issuer-uri=https://{yourOktaDomain}/oauth2/default`). Spring Boot automatically configures the JWT decoder, fetches Okta's public keys via the standardized JWKS endpoint, and handles signature and claim validation for all incoming JWTs without custom logic.

# Question 34: What is Apigee and what role does it play in securing APIs? How does it differ from Spring Security?
## Answer
Apigee is an enterprise API Gateway (managed by Google Cloud). It sits at the infrastructure perimeter, acting as a gatekeeper in front of your backend services. It handles cross-cutting concerns like global rate limiting, traffic routing, API key validation, and OAuth2 token verification.
**Difference**: Apigee blocks unauthorized or abusive traffic at the network edge before it ever reaches your application. Spring Security resides *inside* the Spring Boot application itself and is responsible for fine-grained, application-level authorization (e.g., verifying if the authenticated user has permission to read a specific database row).

# Question 35: What is an access token vs a refresh token in OAuth2? What happens when an access token expires?
## Answer
*   **Access Token**: A short-lived token (e.g., 15 minutes) sent on every API request. Its short lifespan minimizes the damage if it is intercepted.
*   **Refresh Token**: A long-lived token securely stored by the client, used exclusively to request new access tokens.
*   **Expiration Flow**: When the access token expires, the backend API rejects the request with a `401 Unauthorized`. The client intercepts this error, transparently sends the Refresh Token to the auth server's `/token` endpoint to get a new Access Token, and retries the original API request.

# Question 36: What is the difference between @Component and @Service in Spring?
## Answer
`@Component` is the root, generic stereotype annotation indicating a class is a Spring-managed bean.
`@Service` is a specialization of `@Component`. Functionally, they are identical in the IoC container. However, `@Service` is conventionally used to explicitly mark classes in the business layer. This provides semantic clarity, helps developers understand application architecture, and allows you to easily target business logic with pointcuts when configuring AOP (e.g., applying `@Transactional` broadly to all `@Service` beans).

# Question 37: What happens if a method A calls method B in the same class, and method B is annotated with @Transactional? Will a transaction open?
## Answer
No, a transaction will not open. Spring's declarative transaction management relies on AOP proxies. When an external caller invokes a bean, it hits the proxy, which starts the transaction and delegates to the target class. However, when method A internally calls method B within the same instance, the call uses the bare `this` reference, bypassing the proxy completely. The `@Transactional` advice is silently ignored.

# Question 38: What common code smells do you look for in a Spring service code review? (e.g. field injection, double for money, missing @Transactional, System.out, findAll)
## Answer
During a senior-level code review, common smells include:
1.  **Field Injection**: Using `@Autowired` on fields instead of using constructor injection.
2.  **Missing `@Transactional`**: Performing multi-step database writes without transaction boundaries, risking partial commits.
3.  **In-Memory Filtering (`findAll()`)**: Fetching massive datasets into Java memory instead of pushing filters down to the SQL database.
4.  **Floating point for Money**: Using `double` or `float` instead of `BigDecimal`, leading to precision loss.
5.  **Console Logging**: Using `System.out.println` instead of a proper logging framework like SLF4J.
6.  **Self-Invocation**: Calling `@Async` or `@Transactional` methods from within the same class, bypassing proxies.
7.  **God Classes**: Services that lack Single Responsibility, importing too many repositories and performing disparate domain functions.

# Question 39: When building a REST API in Java/Spring what practices do you follow to ensure it's clean, well documented, and thoroughly tested for production?
## Answer
*   **Clean Design**: Adhere strictly to RESTful resource conventions, use semantic HTTP status codes, decouple the database domain from the API contract using DTOs, and implement API versioning.
*   **Robustness**: Centralize error handling using `@RestControllerAdvice` and enforce strict input validation using `@Valid` and JSR-380 annotations.
*   **Documentation**: Integrate Swagger/OpenAPI (e.g., via `springdoc-openapi`) for automatic, interactive API documentation.
*   **Testing**: Employ a testing pyramid. Use `@WebMvcTest` for controller boundaries, `@DataJpaTest` for repository queries, and rely heavily on **Testcontainers** with `@SpringBootTest` to run fully isolated, database-backed integration tests against real infrastructure rather than mocks.

# Question 40: In Spring, when would you choose @TransactionalEventListener over a regular @EventListener for publishing domain events?
## Answer
You choose `@TransactionalEventListener` when downstream side-effects must be strictly coupled to the success of a database transaction.
A regular `@EventListener` executes immediately upon event publication, even if the surrounding transaction later rolls back. A `@TransactionalEventListener` defers execution until a specific phase of the transaction—usually `AFTER_COMMIT`. This ensures that actions like sending an email notification or calling an external system only happen if the domain changes were successfully persisted, preventing "phantom" events for rolled-back operations.
