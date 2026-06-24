# Question 1: Explain AWS EC2 instances, S3 buckets, and Lambda functions. When would you use each?

## Answer
These three AWS services represent fundamental computing and storage paradigms: IaaS (Infrastructure as a Service), Object Storage, and FaaS (Function as a Service).

**1. EC2 (Elastic Compute Cloud)**
*   **What it is:** Virtual machines (VMs) deployed in AWS data centers providing scalable computing capacity. You have full OS-level control.
*   **Use cases:** Legacy application migration (lift-and-shift), stateful applications, software that requires specific OS configurations, high-performance computing, or long-running monolithic applications.
*   **Cost model:** Pay for uptime (per second/hour) and allocated resources, regardless of actual utilization.

**2. S3 (Simple Storage Service)**
*   **What it is:** A highly scalable, durable, and secure object storage service. Data is stored as objects within "buckets".
*   **Use cases:** Storing static assets (images, videos, static websites), data lakes for analytics, backups and archives, and staging data for ETL processes.
*   **Cost model:** Pay per GB stored, plus costs for data transfer and requests (GET, PUT, etc.).

**3. Lambda**
*   **What it is:** A serverless compute service that runs code in response to events without provisioning or managing servers. AWS automatically manages the underlying compute resources.
*   **Use cases:** Event-driven architectures (e.g., triggering a function when a file is uploaded to S3), backend for APIs (via API Gateway), cron jobs, and real-time stream processing.
*   **Cost model:** Pay only for the compute time consumed (measured in milliseconds) and the number of invocations. Zero cost when idle.

# Question 2: How do you ensure high availability and scalability for an application deployed on AWS?

## Answer
High availability (HA) ensures a system remains operational despite failures, while scalability ensures it can handle varying loads. For a typical multi-tier web application, I would implement the following architecture:

**1. Multi-AZ Deployment (High Availability):**
Deploy critical components (EC2 instances, RDS databases, ElastiCache) across multiple Availability Zones (AZs) within an AWS Region. If one AZ goes down, the application continues to run from the others. For databases, use RDS Multi-AZ for synchronous replication and automatic failover.

**2. Auto Scaling Groups (Scalability & HA):**
Place EC2 instances within an Auto Scaling Group (ASG) across multiple AZs. Configure scaling policies based on metrics like CPU utilization or request count (using CloudWatch). The ASG will automatically provision new instances during traffic spikes and terminate them during lulls, while also replacing unhealthy instances.

**3. Elastic Load Balancing (ELB):**
Use an Application Load Balancer (ALB) or Network Load Balancer (NLB) to distribute incoming traffic across the instances in the ASG. The ELB continuously performs health checks and only routes traffic to healthy instances.

**4. Managed Services & Serverless:**
Offload state and leverage managed services where possible. Use S3 for static assets and CloudFront (CDN) to cache content at edge locations globally. Use DynamoDB for scalable NoSQL storage or Aurora for relational data with read replicas. Utilizing serverless components like API Gateway and Lambda automatically handles scaling without manual provisioning.

**5. Decoupling:**
Use SQS (Simple Queue Service) or SNS (Simple Notification Service) to decouple microservices. This prevents a surge in requests from overwhelming backend workers and ensures messages are not lost if a component temporarily fails.

# Question 3: Describe GCP Compute Engine VMs, Cloud Storage, and Cloud Functions. When would you use each?

## Answer
These are Google Cloud Platform's foundational services for compute, storage, and serverless execution, analogous to AWS EC2, S3, and Lambda.

**1. Compute Engine (GCE)**
*   **What it is:** IaaS offering that provides highly customizable virtual machines. It features custom machine types and live migration.
*   **Use cases:** Lift-and-shift migrations, applications requiring specific OS-level access, custom kernel tuning, high-performance computing, and databases not easily supported by managed services.
*   **Pros:** Custom sizing allows precise cost control; live migration minimizes maintenance downtime.

**2. Cloud Storage (GCS)**
*   **What it is:** Unified object storage for developers and enterprises. It offers a single API across various storage classes (Standard, Nearline, Coldline, Archive).
*   **Use cases:** Storing unstructured data like media files, website hosting, backups, disaster recovery, and data lakes for BigQuery or Dataproc.
*   **Pros:** Strong consistency, global namespace, and seamless lifecycle management between storage classes.

**3. Cloud Functions**
*   **What it is:** Event-driven, serverless compute service that scales automatically and requires no infrastructure management.
*   **Use cases:** Webhooks, lightweight APIs, event-driven data processing (e.g., reacting to a GCS file upload or a Pub/Sub message), and automated cloud administration tasks.
*   **Pros:** Zero maintenance, scales to zero (no cost when idle), and tight integration with GCP event sources.

# Question 4: How do GCP Pub/Sub and IAM differ from AWS SNS/SQS and IAM, respectively?

## Answer
While GCP and AWS offer similar capabilities, their architectural approaches to messaging and identity management differ significantly.

**Messaging: GCP Pub/Sub vs. AWS SNS/SQS**
*   **GCP Pub/Sub:** It is a single, globally distributed service that combines both message ingestion and delivery. It acts as both a message router (like SNS) and a queue (like SQS). Publishers send messages to a *Topic*, and subscribers receive them via *Subscriptions* attached to that topic. It guarantees at-least-once delivery and scales massively out-of-the-box.
*   **AWS SNS/SQS:** AWS separates these concerns. **SNS** is a pub/sub service used for fan-out (sending a message to multiple endpoints). **SQS** is a distributed message queue used for point-to-point, reliable asynchronous communication. Often, they are used together: an SNS topic fans out messages to multiple SQS queues.
*   **Key Difference:** Pub/Sub provides a unified, global abstraction for both fan-out and queuing, while AWS requires wiring SNS and SQS together for comparable functionality.

**Identity Management: GCP IAM vs. AWS IAM**
*   **GCP IAM:** Focuses on the "Who" (identity) and "What" (role). Roles (a collection of permissions) are bound to Identities (users, service accounts) at a specific resource level in a rigid hierarchy (Organization -> Folder -> Project -> Resource). Inheritance flows top-down. Service Accounts are treated as both identities and resources.
*   **AWS IAM:** Policy-centric. JSON policies defining permissions are attached to Users, Groups, or Roles. AWS heavily utilizes "AssumeRole" for cross-account access or for AWS services to interact with each other. Resource-based policies (like S3 bucket policies) exist alongside identity-based policies.
*   **Key Difference:** GCP IAM relies heavily on an organizational hierarchy and predefined/custom roles bound at resource levels. AWS IAM is more heavily policy-driven with complex, highly granular JSON documents and relies significantly on the role assumption mechanism.

# Question 5: Compare AWS SQS and SNS. When would you use SQS over SNS, and vice-versa?

## Answer
Both SQS and SNS are messaging services in AWS, but they serve fundamentally different purposes: message queuing versus pub/sub notification.

**AWS SQS (Simple Queue Service)**
*   **Paradigm:** Point-to-point, asynchronous message queuing (Pull-based).
*   **Mechanism:** A producer sends a message to the queue. A consumer polls the queue, retrieves the message, processes it, and explicitly deletes it.
*   **Key Features:** Message persistence, ordering (with FIFO queues), dead-letter queues (DLQ), and exactly-once processing capabilities (with FIFO).
*   **When to use:**
    *   **Decoupling microservices:** Ensuring that if a downstream service fails, messages aren't lost (they remain in the queue).
    *   **Rate limiting/Load leveling:** Buffering requests to protect backend databases from traffic spikes.
    *   **Batch processing:** Workers pulling jobs from a queue at their own pace.

**AWS SNS (Simple Notification Service)**
*   **Paradigm:** Publish/Subscribe fan-out (Push-based).
*   **Mechanism:** A publisher sends a message to a "Topic". SNS immediately pushes that message to all subscribers (Lambda, SQS, HTTP endpoints, email, SMS) simultaneously.
*   **Key Features:** Instantaneous delivery, message filtering, and broad protocol support. Messages are not persisted once delivered.
*   **When to use:**
    *   **Fan-out architecture:** Sending identical alerts or data to multiple disparate systems (e.g., updating a search index and sending an email simultaneously).
    *   **Real-time alerting:** Triggering an immediate action or human notification based on an event.

**Combining Them (The Fan-out Pattern):**
Often, the best approach is to use them together. An event is published to an SNS Topic, which fans the message out to multiple SQS Queues. This provides the instant broadcasting capability of SNS with the reliability and fault tolerance of SQS for each consuming service.
