package dev.alexkzk.doselect.outbox.service;

public interface EventDispatcher {
    void triggerImmediateProcessing();
}