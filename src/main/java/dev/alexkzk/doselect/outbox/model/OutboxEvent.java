package dev.alexkzk.doselect.outbox.model;

public class OutboxEvent {
    private Long id;
    private final String eventType;
    private final String payload;
    private boolean processed;

    public OutboxEvent(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.processed = false;
    }

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public boolean isProcessed() { return processed; }
    public void markProcessed() { this.processed = true; }
}