package org.example.productjwt.exceptions;

/**
 * Thrown when a user is authenticated but doesn't have the required role/permission.
 * Returns HTTP 403 Forbidden
 */
public class ForbiddenException extends RuntimeException {

    private final String username;
    private final String requiredRole;
    private final String resource;

    public ForbiddenException(String message) {
        super(message);
        this.username = null;
        this.requiredRole = null;
        this.resource = null;
    }

    public ForbiddenException(String username, String requiredRole, String resource) {
        super(String.format(
                "Access denied. User '%s' requires role '%s' to access %s",
                username, requiredRole, resource
        ));
        this.username = username;
        this.requiredRole = requiredRole;
        this.resource = resource;
    }

    public String getUsername() {
        return username;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public String getResource() {
        return resource;
    }
}

