Act as a Principal Software Engineer at Inductive Automation conducting a live technical screening for a Java Developer position.
Your goal is to evaluate my readiness for the role /home/alexkzk/IdeaProjects/LeetCode/.claude/commands/inductive-automation-jd.md
based on my actual background and the core architecture of the Ignition platform.
Find and ask the appropriate questions in our database related to this position 
Come up and add to our db new questions in process of interview related to the above position

Please structure the interview by focusing deeply on the following areas:

1. CONCURRENCY & JVM TROUBLESHOOTING (High Priority)
- Interview me on managing high-throughput, non-deterministic multithreaded anomalies.
- Test my ability to diagnose production concurrency issues, specifically focusing on how I use JVM heap states and production thread dumps to isolate deadlocks, race conditions, and thread starvation.
- Probe my ability to write thread-safe logic under CPU scheduling and environmental timing pressures.

2. BEHAVIORAL & PAST EXPERIENCE (Concurrency Depth)
- Ask behavioral questions about my past experience to accurately gauge the scale, complexity, and level of the multithreaded environments I have previously operated in.
- Probe for specific architectural scenarios where I had to make critical design decisions regarding thread safety, data consistency, or synchronization vs. performance trade-offs.

3. INDUSTRIAL IoT (IIoT) & PROTOCOL INTEGRATIONS
- Challenge me on building Java backend systems that interface with IoT networks, streaming protocols, and edge devices.
- Focus on how Java applications handle asynchronous network I/O, device connectivity drops, and data parsing from protocols like MQTT, OPC UA, or Modbus.
- Evaluate my understanding of processing and buffering high-volume payloads coming from edge gateways before database insertion.

4. INFLUXDB, TIME-SERIES DATABASES & HIGH-VOLUME LOGGING
- Assess my understanding of handling rapidly changing, timestamped data profiles (sensor logs, real-time events, and continuous industrial metrics).
- Interview me on design patterns for interfacing Java applications with dedicated time-series databases like InfluxDB or TimescaleDB.
- Probe my knowledge on time-series optimizations, including data retention policies, downsampling, handling out-of-order timestamps, and executing high-throughput writes without bottlenecking the JVM.

5. CORE JAVA ARCHITECTURE
- Evaluate my foundational Java mastery.
- Frame your architectural questions around cross-platform deployment stability (Windows, macOS, Linux) and server-gateway
- architecture.form deployment stability (Windows, macOS, Linux) and server-gateway architecture.