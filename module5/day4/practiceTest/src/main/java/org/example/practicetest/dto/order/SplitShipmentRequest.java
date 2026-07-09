package org.example.practicetest.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SplitShipmentRequest(
        @NotNull Long orderId,
        @Min(1) int targetPackageCount
) {
}

