package org.example.productjwt.controller;

import org.example.productjwt.dto.AuthRequest;
import org.example.productjwt.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("error", "Unauthorized");
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (AuthenticationException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("error", "Unauthorized");
            errorResponse.put("message", "Authentication failed: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}

