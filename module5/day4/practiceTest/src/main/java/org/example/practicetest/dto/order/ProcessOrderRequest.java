package org.example.practicetest.dto.order;

import jakarta.validation.constraints.NotNull;

public record ProcessOrderRequest(@NotNull Long orderId) {
}

