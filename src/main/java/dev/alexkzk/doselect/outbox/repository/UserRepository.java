package dev.alexkzk.doselect.outbox.repository;

import dev.alexkzk.doselect.outbox.model.User;

public interface UserRepository {
    boolean existsByEmail(String email);
    void save(User user);
}