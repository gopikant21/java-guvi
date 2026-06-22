package org.example.productjpa.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException productNotFound(Long id) {
        return new ResourceNotFoundException("Product not found with id: " + id);
    }

    public static ResourceNotFoundException customerNotFound(Long id) {
        return new ResourceNotFoundException("Customer not found with id: " + id);
    }

    public static ResourceNotFoundException orderNotFound(Long id) {
        return new ResourceNotFoundException("Order not found with id: " + id);
    }

    public static ResourceNotFoundException orderItemNotFound(Long id) {
        return new ResourceNotFoundException("Order Item not found with id: " + id);
    }
}

