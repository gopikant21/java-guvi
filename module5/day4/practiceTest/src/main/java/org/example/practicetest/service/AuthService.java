package org.example.practicetest.service;

import org.example.practicetest.dto.auth.LoginRequest;
import org.example.practicetest.dto.auth.LoginResponse;
import org.example.practicetest.dto.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}

