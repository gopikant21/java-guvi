package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectLoanRequest {
    @NotBlank
    private String reason;
}

