package org.example.productjwt.dto;

/**
 * Record representing the total amount for an order.
 * @param orderId the order ID
 * @param total the total amount for the order
 */
public record OrderTotalResponseDto(Long orderId, Double total) {}

