package dev.alexkzk.doselect.ordergateway;

public class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}