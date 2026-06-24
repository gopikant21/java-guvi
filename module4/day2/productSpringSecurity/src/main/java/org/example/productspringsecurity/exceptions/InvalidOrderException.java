package org.example.productspringsecurity.exceptions;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidOrderException insufficientStock(String productName, int requested, int available) {
        return new InvalidOrderException("Insufficient stock for " + productName + ". Requested: " + requested + ", Available: " + available);
    }

    public static InvalidOrderException emptyOrder() {
        return new InvalidOrderException("Cannot create an order with no items");
    }
}

