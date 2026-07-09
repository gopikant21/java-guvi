package org.example.week5.controller;

import org.example.week5.dto.AuthDTO;
import org.example.week5.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO authDTO) {
        String result = authService.register(authDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO) {
        String token = authService.login(authDTO);
        return ResponseEntity.ok(token);
    }
}

