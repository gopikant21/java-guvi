package org.northernarc.assessment4.exception;

/**
 * Exception thrown when a customer is not found in the database
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

