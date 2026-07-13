package org.example.productjwt.exceptions;

/**
 * Thrown when a user attempts to access a resource without proper authorization.
 * Returns HTTP 401 Unauthorized
 */
public class UnauthorizedException extends RuntimeException {

    private final String username;
    private final String resource;

    public UnauthorizedException(String message) {
        super(message);
        this.username = null;
        this.resource = null;
    }

    public UnauthorizedException(String username, String resource) {
        super(String.format("User '%s' is not authorized to access %s", username, resource));
        this.username = username;
        this.resource = resource;
    }

    public String getUsername() {
        return username;
    }

    public String getResource() {
        return resource;
    }
}

