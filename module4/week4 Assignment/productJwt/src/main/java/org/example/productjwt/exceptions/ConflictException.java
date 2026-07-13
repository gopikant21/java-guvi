package org.example.productjwt.exceptions;

/**
 * Thrown when an operation cannot be performed due to a conflicting state or operation.
 * Returns HTTP 409 Conflict
 */
public class ConflictException extends RuntimeException {

    private final String resourceType;
    private final String reason;

    public ConflictException(String resourceType, String reason) {
        super(String.format("Conflict: %s - %s", resourceType, reason));
        this.resourceType = resourceType;
        this.reason = reason;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getReason() {
        return reason;
    }
}

