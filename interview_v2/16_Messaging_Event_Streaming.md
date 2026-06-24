# Question 1: If you have a Kafka topic with 3 partitions and a consumer group with 4 consumers, what happens to the consumers?

## Answer

In Apache Kafka, a fundamental rule is that a single partition can only be consumed by at most **one** consumer within a given consumer group at any time. This design guarantees strict ordering of message processing within that partition.

Given a topic with **3 partitions** and a consumer group with **4 consumers**:
*   **3 consumers** will each be assigned exactly one partition and will actively process messages.
*   **1 consumer** will remain **idle** and will not receive any messages from that topic.

### Key Takeaways for a Senior Engineer:
1.  **Maximum Parallelism**: The number of partitions strictly dictates the maximum level of parallel consumption achievable by a single consumer group. To process messages faster by adding more consumers, you must first increase the number of partitions.
2.  **High Availability & Failover**: Having an "extra" consumer is not necessarily an anti-pattern if high availability is prioritized. The idle 4th consumer acts as a hot standby. If one of the active consumers crashes or goes offline, the group coordinator will trigger a **rebalance**. The idle consumer will then take over the partition that belonged to the failed consumer, minimizing processing downtime.
3.  **Resource Allocation**: While an idle consumer provides failover capabilities, it does consume system resources (memory, thread overhead, network connections to brokers). A more typical deployment ensures `# consumers = # partitions` for optimal resource utilization, relying on standard orchestration (like Kubernetes pods restarting) to handle failovers, unless extremely fast recovery times are mandated.
