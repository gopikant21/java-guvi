package org.example.practicetest.dto.order;

import jakarta.validation.constraints.NotNull;

public record CancelOrderRequest(@NotNull Long orderId) {
}

