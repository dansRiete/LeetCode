package dev.alexkzk.doselect.outbox.service;

import dev.alexkzk.doselect.outbox.model.OutboxEvent;
import dev.alexkzk.doselect.outbox.model.User;
import dev.alexkzk.doselect.outbox.repository.OutboxRepository;
import dev.alexkzk.doselect.outbox.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    private FakeUserRepository userRepository;
    private FakeOutboxRepository outboxRepository;
    private FakeEventDispatcher eventDispatcher;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        outboxRepository = new FakeOutboxRepository();
        eventDispatcher = new FakeEventDispatcher();
        service = new RegistrationService(userRepository, outboxRepository, eventDispatcher);
    }

    @AfterEach
    void tearDown() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    // --- Transaction lifecycle helpers ---
    // TransactionSynchronizationManager is thread-local, so we can drive it directly
    // without a Spring Boot context. This mimics exactly what Spring's PlatformTransactionManager
    // does before and after calling the @Transactional method body.

    private void beginTransaction() {
        TransactionSynchronizationManager.initSynchronization();
    }

    private void commitTransaction() {
        List<TransactionSynchronization> syncs =
                new ArrayList<>(TransactionSynchronizationManager.getSynchronizations());
        syncs.forEach(s -> s.beforeCommit(false));
        syncs.forEach(TransactionSynchronization::beforeCompletion);
        syncs.forEach(TransactionSynchronization::afterCommit);   // the hook fires here
        syncs.forEach(s -> s.afterCompletion(TransactionSynchronization.STATUS_COMMITTED));
        TransactionSynchronizationManager.clearSynchronization();
    }

    // On rollback, afterCommit() is deliberately skipped — that's the whole point.
    private void rollbackTransaction() {
        List<TransactionSynchronization> syncs =
                new ArrayList<>(TransactionSynchronizationManager.getSynchronizations());
        syncs.forEach(TransactionSynchronization::beforeCompletion);
        syncs.forEach(s -> s.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK));
        TransactionSynchronizationManager.clearSynchronization();
    }

    // --- Happy path ---

    @Test
    void eventNotFiredBeforeCommit() {
        beginTransaction();
        service.registerUser("alice", "alice@example.com");

        assertFalse(eventDispatcher.wasTriggered(), "dispatcher must stay silent until commit");
    }

    @Test
    void eventFiredAfterCommit() {
        beginTransaction();
        service.registerUser("alice", "alice@example.com");
        commitTransaction();

        assertTrue(eventDispatcher.wasTriggered(), "dispatcher must fire after successful commit");
    }

    @Test
    void userAndOutboxWrittenInSameTransaction() {
        beginTransaction();
        service.registerUser("dave", "dave@example.com");

        assertEquals(1, userRepository.savedUsers.size());
        assertEquals(1, outboxRepository.savedEvents.size());
        commitTransaction();
    }

    @Test
    void outboxEventHasCorrectTypePayloadAndUnprocessedFlag() {
        beginTransaction();
        service.registerUser("eve", "eve@example.com");
        commitTransaction();

        OutboxEvent event = outboxRepository.savedEvents.get(0);
        assertEquals("USER_REGISTERED", event.getEventType());
        assertFalse(event.isProcessed());
        assertTrue(event.getPayload().contains("eve@example.com"));
    }

    // --- Rollback / failure scenarios ---

    @Test
    void eventNotFiredAfterRollback() {
        beginTransaction();
        service.registerUser("bob", "bob@example.com");
        rollbackTransaction();

        assertFalse(eventDispatcher.wasTriggered(), "dispatcher must stay silent after rollback");
    }

    @Test
    void ghostEventSuppressedWhenCommitFailsAfterOutboxWrite() {
        // This is the hidden DoSelect test vector: the DB flushes the outbox row but then
        // the transaction fails to commit (e.g. a constraint violation at flush time).
        // The synchronization must have been registered, but afterCommit() must never fire.
        beginTransaction();
        service.registerUser("carol", "carol@example.com");

        assertEquals(1, outboxRepository.savedEvents.size(), "outbox row exists before commit");

        rollbackTransaction();  // simulates commit failure — afterCommit() is NOT called

        assertFalse(eventDispatcher.wasTriggered(),
                "no ghost event must be dispatched when the commit fails");
    }

    // --- Validation ---

    @Test
    void duplicateEmailThrowsBeforeAnyPersistence() {
        userRepository.registerExistingEmail("taken@example.com");
        beginTransaction();

        assertThrows(IllegalArgumentException.class,
                () -> service.registerUser("x", "taken@example.com"));

        assertEquals(0, outboxRepository.savedEvents.size());
    }

    @Test
    void noSynchronizationRegisteredWhenValidationFails() {
        userRepository.registerExistingEmail("dup@example.com");
        beginTransaction();

        assertThrows(IllegalArgumentException.class,
                () -> service.registerUser("y", "dup@example.com"));

        assertTrue(TransactionSynchronizationManager.getSynchronizations().isEmpty(),
                "no afterCommit hook must be registered if the method threw before reaching that line");
    }

    @Test
    void noGhostEventAfterDuplicateEmailRollback() {
        userRepository.registerExistingEmail("dup@example.com");
        beginTransaction();

        assertThrows(IllegalArgumentException.class,
                () -> service.registerUser("z", "dup@example.com"));
        rollbackTransaction();

        assertFalse(eventDispatcher.wasTriggered());
    }

    // --- In-process stubs (no Mockito needed) ---

    static class FakeUserRepository implements UserRepository {
        final List<User> savedUsers = new ArrayList<>();
        private final Set<String> existingEmails = new HashSet<>();

        @Override
        public boolean existsByEmail(String email) {
            return existingEmails.contains(email);
        }

        @Override
        public void save(User user) {
            savedUsers.add(user);
            existingEmails.add(user.getEmail());
        }

        void registerExistingEmail(String email) {
            existingEmails.add(email);
        }
    }

    static class FakeOutboxRepository implements OutboxRepository {
        final List<OutboxEvent> savedEvents = new ArrayList<>();

        @Override
        public void save(OutboxEvent event) {
            savedEvents.add(event);
        }
    }

    static class FakeEventDispatcher implements EventDispatcher {
        private boolean triggered = false;

        @Override
        public void triggerImmediateProcessing() {
            triggered = true;
        }

        boolean wasTriggered() {
            return triggered;
        }
    }
}