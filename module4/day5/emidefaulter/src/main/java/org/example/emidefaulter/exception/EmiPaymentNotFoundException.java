package org.example.emidefaulter.exception;

public class EmiPaymentNotFoundException extends RuntimeException {
    public EmiPaymentNotFoundException(String message) {
        super(message);
    }
}

