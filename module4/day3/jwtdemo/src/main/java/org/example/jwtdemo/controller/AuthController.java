package org.example.jwtdemo.controller;

import org.example.jwtdemo.dto.JwtRequestDTO;
import org.example.jwtdemo.dto.JwtResponseDTO;
import org.example.jwtdemo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/auth/login")
    public JwtResponseDTO login(@RequestBody JwtRequestDTO jwtRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()) );
        jwtUtil.generateToken(jwtRequest.getUsername());
        JwtResponseDTO jwtResponse =new JwtResponseDTO();
        jwtResponse.setToken(jwtUtil.generateToken(jwtRequest.getUsername()));
        return jwtResponse;
    }
}
