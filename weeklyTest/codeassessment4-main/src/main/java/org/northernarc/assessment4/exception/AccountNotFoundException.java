package org.northernarc.assessment4.exception;

/**
 * Exception thrown when an account is not found in the database
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

