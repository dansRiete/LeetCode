package dev.alexkzk.doselect.outbox.repository;

import dev.alexkzk.doselect.outbox.model.OutboxEvent;

public interface OutboxRepository {
    void save(OutboxEvent event);
}