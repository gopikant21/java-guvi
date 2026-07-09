package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;
import org.example.backend.entity.Role;

import java.time.LocalDateTime;

@Value
@Builder
public class UserProfileResponse {
    Long id;
    String name;
    String email;
    String phone;
    Role role;
    LocalDateTime createdAt;
}

