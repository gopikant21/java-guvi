package org.example.productjwt.enums;

import java.time.LocalDateTime;

/**
 * Record representing a standard API response with metadata.
 * @param timestamp when the response was generated
 * @param status HTTP status code
 * @param message response message
 * @param data optional response data
 * @param <T> type of data
 */
public record ApiResponse<T>(
    LocalDateTime timestamp,
    int status,
    String message,
    T data
) {}

