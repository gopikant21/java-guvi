package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CustomerDetailsResponse {
    UserProfileResponse customer;
    List<LoanResponse> loans;
}

