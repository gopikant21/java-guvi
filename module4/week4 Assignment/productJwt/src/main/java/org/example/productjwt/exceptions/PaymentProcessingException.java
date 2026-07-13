package org.example.productjwt.exceptions;

/**
 * Thrown when payment processing fails.
 * Returns HTTP 402 Payment Required
 */
public class PaymentProcessingException extends RuntimeException {

    private final String orderId;
    private final String reason;

    public PaymentProcessingException(String orderId, String reason) {
        super(String.format("Payment processing failed for Order ID %s: %s", orderId, reason));
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }
}

