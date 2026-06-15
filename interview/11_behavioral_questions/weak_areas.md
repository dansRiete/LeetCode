# Weak Areas — Interview Q&A

These are the topics to sharpen before the interview based on practice session gaps.

---

## 1. Integration & E2E Testing

**Q: Your team had unit tests but no integration or E2E tests. How would you approach introducing them without disrupting delivery?**

On my previous project we had unit tests on every PR but we never got to integration or E2E tests — it came up in retrospectives but bandwidth was always the constraint. Looking back, we did have production surprises that better integration tests would have caught earlier, like the accounting service drift from the main application's business rules.

If I joined a team in that same position today, I wouldn't try to retrofit full coverage overnight. I'd start by identifying the two or three most critical user journeys — the ones where a bug causes immediate business impact — and write integration tests for those first using Testcontainers so they run against real dependencies without needing a shared environment. Once the team sees the value from that small investment, it's much easier to get buy-in to expand coverage gradually. I'd also make integration tests part of the PR pipeline from day one so they become a habit, not an afterthought.

---

**Q: What is the difference between unit, integration, and E2E tests, and when would you use each?**

Unit tests cover a single class or method in isolation — no database, no network, no external dependencies. They are fast, run on every commit, and are great for verifying business logic and edge cases. I use mocks to replace any dependencies.

Integration tests verify that multiple components work correctly together — for example, a service layer talking to a real database or a message consumer processing a real message from a broker. I use Testcontainers to spin up real dependencies in Docker so the tests are environment-independent. These run on PR merge and catch issues that unit tests can't, like incorrect SQL queries or ORM mapping mistakes.

E2E tests exercise the full system from the outside — like a real user or API client would. They are fewer in number, slower, and run in a staging environment before a release. They cover critical user journeys end to end and are the last safety net before production.

The key principle is the test pyramid: many unit tests, fewer integration tests, even fewer E2E tests. Inverting that pyramid leads to slow, brittle test suites.

---

## 2. Messaging Reliability — RabbitMQ

**Q: In your RabbitMQ implementation, failed messages were discarded. What would you do differently to make that system reliable?**

Discarding failed messages was the simplest approach at the time, but for an accounting service it's the wrong trade-off — every lost message is a missing financial record that might not surface until a manual reconciliation. If I were designing it today I would add three things.

First, a **dead letter queue (DLQ)**. When a message fails processing after a set number of retries, RabbitMQ routes it to the DLQ instead of discarding it. This means no data is lost — failed messages sit in the DLQ and can be inspected, replayed, or manually handled.

Second, **retry logic with backoff**. Most failures are transient — a brief DB unavailability, a downstream timeout. I'd configure the consumer to retry with an exponential backoff (e.g. 1s, 5s, 30s) before giving up and routing to the DLQ.

Third, **idempotency on the consumer side**. If a message is retried or replayed from the DLQ, the accounting service must not create a duplicate record. I'd use the reservation ID from the ArEvent DTO as an idempotency key — before processing, check if that event has already been recorded, and skip if so.

Finally, I'd add **alerting on DLQ depth** — if messages start piling up in the DLQ, it should page someone immediately rather than being discovered days later.

---

**Q: What is a Dead Letter Queue and why is it important?**

A Dead Letter Queue is a special queue where messages are routed when they cannot be successfully processed — either because they failed after the maximum number of retries, expired before being consumed, or were rejected by the consumer. Instead of being silently discarded, they land in the DLQ where they can be inspected, debugged, and replayed once the underlying issue is fixed.

For business-critical messaging like financial events or order processing, a DLQ is essential because it guarantees no message is permanently lost. Without one, a bug in the consumer or a temporary infrastructure issue silently drops data, and you only find out when someone notices the numbers don't add up.

---

## 3. Cloud & Kubernetes

**Q: Your DevOps engineer owned the Kubernetes infrastructure. What is your understanding of how your application ran in that environment?**

Our DevOps engineer managed the cluster configuration and deployments, but as the developer I was responsible for making sure my services were designed to run correctly in that environment. In practice that meant a few things.

Each service was **stateless** — no local session or file state — so Kubernetes could run multiple replicas and route traffic to any of them. I made sure Spring Boot's `/actuator/health` endpoint was properly configured so Kubernetes could use it as a **liveness and readiness probe** — the readiness probe tells Kubernetes not to send traffic until the app is fully started, and the liveness probe restarts the pod if it becomes unresponsive.

I also configured **graceful shutdown** in Spring Boot so that when Kubernetes terminates a pod during a rolling deployment, in-flight requests finish processing before the process exits rather than being dropped mid-execution.

For the Python batch services I wrote Dockerfiles and the DevOps engineer configured them as Kubernetes CronJobs — scheduled containers that spin up, run the job, and terminate.

---

**Q: What is the difference between a liveness probe and a readiness probe in Kubernetes?**

A **readiness probe** tells Kubernetes whether the application is ready to receive traffic. If it fails, Kubernetes removes the pod from the load balancer rotation but does not restart it. This is important during startup — a Spring Boot app with Flyway migrations might take 30 seconds to be ready, and you don't want traffic hitting it before the migrations complete.

A **liveness probe** tells Kubernetes whether the application is still alive. If it fails, Kubernetes restarts the pod. This handles cases where the application is running but stuck — for example, deadlocked threads or an unresponsive event loop.

In Spring Boot both are exposed via `/actuator/health/readiness` and `/actuator/health/liveness` when you include the `spring-boot-starter-actuator` dependency and enable the Kubernetes probes configuration.

---

**Q: What does it mean for a service to be stateless, and why does it matter in a cloud environment?**

A stateless service does not store any user or session data locally in memory or on disk between requests. Every request contains all the information needed to process it, and any data that needs to persist goes to an external store — a database, a cache like Redis, or a message broker.

In a cloud environment this matters because services run as multiple replicas behind a load balancer, and any replica might handle any request. If state were stored locally, a user's second request might go to a different pod that has no knowledge of the first request. It also means Kubernetes can freely restart, replace, or scale pods without data loss. Stateless design is a prerequisite for horizontal scaling and high availability.

### Tell me about a production incident you fixed. What was the situation, what action did you take, and what was the result? (STAR format)

**Key points:**
- Ensure you have a structured STAR story ready (Situation, Task, Action, Result).
- Highlight how you diagnosed the issue (logs, metrics, reproducing it).
- Explain the immediate mitigation (rollback, hotfix) vs the long-term fix (adding tests, fixing logic).
