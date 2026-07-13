package org.example.productjwt.exceptions;

/**
 * Thrown when attempting to create a resource that already exists with the same unique identifier.
 * Returns HTTP 409 Conflict
 */
public class DuplicateResourceException extends RuntimeException {

    private final String resourceType;
    private final String fieldName;
    private final Object fieldValue;

    public DuplicateResourceException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceType, fieldName, fieldValue));
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}

