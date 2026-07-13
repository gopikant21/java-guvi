package org.example.productjwt.exceptions;

/**
 * Thrown when a request contains invalid or malformed data.
 * Returns HTTP 400 Bad Request
 */
public class ValidationException extends RuntimeException {

    private final String field;
    private final String reason;

    public ValidationException(String field, String reason) {
        super(String.format("Validation error for field '%s': %s", field, reason));
        this.field = field;
        this.reason = reason;
    }

    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.reason = null;
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}

