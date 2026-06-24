# Question 1: If one of 16 instances of a service in Kubernetes falls over once a week due to CPU throttling, how would you diagnose and fix it?

## Answer

When a pod "falls over" due to CPU throttling, it typically means the application became so starved for CPU cycles that it failed its liveness probes, causing Kubernetes to restart it. 

Here is a structured approach to diagnosing and fixing the issue:

### 1. Diagnosis & Investigation

*   **Verify the Crash Reason:** Use `kubectl describe pod <pod_name>` to confirm the restart reason. Check if the liveness probe failed due to a timeout. Ensure it wasn't an `OOMKilled` event masquerading as a CPU issue.
*   **Analyze Metrics (Prometheus/Grafana):** Examine the CPU usage vs. CPU requests/limits for the specific pod. Look for spikes correlating with the weekly crash. Also, check the `container_cpu_cfs_throttled_seconds_total` metric to quantify the exact throttling happening via the Linux Completely Fair Scheduler (CFS).
*   **Check for Uneven Load or Specific Tasks:** 
    *   Does this pod handle a specific heavy request (e.g., a weekly report generation, a large data sync)? 
    *   Is the load balancer distributing traffic unevenly, leading to a "hot" pod?
*   **Analyze Application Logs and Profiling:** Look at the logs immediately preceding the crash. If you are using Java, a CPU spike might be caused by a "Garbage Collection (GC) spiral of death" (where the JVM spends 99% of its CPU time doing GC). Using tools like Java Flight Recorder (JFR) or async-profiler can pinpoint hot methods or memory leaks.
*   **Node-level Issues (Noisy Neighbors):** Verify if the underlying Kubernetes Node is overcommitted. A "noisy neighbor" on the same node could be consuming excessive resources, although CPU limits are meant to isolate this.

### 2. Remediation & Fixes

*   **Adjust CPU Limits and Requests:** 
    *   If the application is legitimately bursting and the throttling is too aggressive, consider raising the CPU `limits` or setting `limits` equal to `requests` for Guaranteed QoS. 
    *   *Alternative approach:* Some modern Kubernetes practices suggest removing CPU limits entirely (relying only on requests for scheduling) to prevent unnecessary CFS throttling, assuming the cluster has adequate capacity.
*   **Tune Liveness Probes:** If the application requires occasional CPU bursts that temporarily delay responsiveness, increase the `timeoutSeconds` or `failureThreshold` of the liveness probe to prevent premature kills.
*   **Offload Heavy Tasks:** If the crash is caused by a weekly background task, extract that logic from the synchronous web service and deploy it as a dedicated Kubernetes `CronJob` or handle it via async message queues (e.g., Kafka, SQS).
*   **Application Optimization:** Fix underlying code issues causing CPU spikes. For a Java app, this might involve tuning the JVM garbage collector (e.g., switching to G1GC or ZGC), fixing inefficient algorithms, or bounding thread pools.
*   **Horizontal Pod Autoscaler (HPA):** Ensure HPA is configured correctly to scale up based on CPU utilization *before* individual pods reach their throttling thresholds.
