package org.example.jwtdemo.dto;

import lombok.Data;

@Data
public class JwtRequestDTO {
	private String username;
	private String password;
}
