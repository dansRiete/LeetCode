package dev.alexkzk.doselect.outbox.service;

import dev.alexkzk.doselect.outbox.model.OutboxEvent;
import dev.alexkzk.doselect.outbox.model.User;
import dev.alexkzk.doselect.outbox.repository.OutboxRepository;
import dev.alexkzk.doselect.outbox.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final OutboxRepository outboxRepository;
    private final EventDispatcher eventDispatcher;

    public RegistrationService(UserRepository userRepository,
                               OutboxRepository outboxRepository,
                               EventDispatcher eventDispatcher) {
        this.userRepository = userRepository;
        this.outboxRepository = outboxRepository;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Registers a new user and ensures reliable outbox event creation.
     *
     * @param username The chosen username
     * @param email    The user email address
     * @throws IllegalArgumentException if the email is already registered
     */
    @Transactional
    public void registerUser(String username, String email) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        userRepository.save(new User(username, email));

        // TODO 3: Create and persist an OutboxEvent entity recording the event
        outboxRepository.save(new OutboxEvent("USER_REGISTERED", "eve@example.com"));

        // TODO 4: Register a transaction synchronization hook to trigger
        //         eventDispatcher.triggerImmediateProcessing() safely ONLY AFTER commit.
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        // If the transaction rolls back, this code block is completely skipped.
                        eventDispatcher.triggerImmediateProcessing();
                    }
                }
        );
    }
}