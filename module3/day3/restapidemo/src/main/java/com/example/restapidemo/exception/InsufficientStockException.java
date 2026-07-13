package com.example.restapidemo.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}

