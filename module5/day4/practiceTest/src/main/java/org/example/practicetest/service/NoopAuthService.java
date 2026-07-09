package org.example.practicetest.service;

import org.example.practicetest.dto.auth.LoginRequest;
import org.example.practicetest.dto.auth.LoginResponse;
import org.example.practicetest.dto.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class NoopAuthService implements AuthService {
    @Override
    public void register(RegisterRequest request) {
        // no-op placeholder for TDD-first project structure
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return new LoginResponse("stub-token");
    }
}

