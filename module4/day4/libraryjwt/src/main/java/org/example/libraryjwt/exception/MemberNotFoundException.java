package org.example.libraryjwt.exception;

/**
 * Exception for when a member is not found
 * Task 10: Global Exception Handling
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

