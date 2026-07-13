package org.example.productjwt.enums;

/**
 * Enum representing user roles in the system.
 * Used for role-based access control (RBAC) and authorization.
 */
public enum UserRole {
    ADMIN("Admin", "Full system access - Create, Read, Update, Delete, Bulk operations"),
    USER("User", "Limited access - Read operations, Create/Update own resources");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if role has admin privileges
     * @return true if role is ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
}

