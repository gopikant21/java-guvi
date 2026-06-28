package org.example.libraryjwt.exception;

/**
 * Exception for when a book is not found
 * Task 10: Global Exception Handling
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

