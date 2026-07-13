package org.example.productjwt.services;

import org.example.productjwt.model.CustomerAuth;
import org.example.productjwt.repository.CustomerAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Database-backed UserDetailsService for seeded customers.
 * Loads user credentials from customer_auth table in PostgreSQL.
 * Supports both ROLE_USER and ROLE_ADMIN roles.
 */
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerAuthRepository customerAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load from customer_auth table in database
        CustomerAuth customerAuth = customerAuthRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build authorities from role stored in customer_auth
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = customerAuth.getRole();
        if (role != null && !role.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        // If ADMIN, also add USER role (for hasAnyRole compatibility)
        if ("ROLE_ADMIN".equals(role)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Return user with actual BCrypt password hash from database
        return User.builder()
                .username(customerAuth.getUsername())
                .password(customerAuth.getPasswordHash())  // BCrypt hash from customer_auth
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!customerAuth.getIsActive())
                .credentialsExpired(false)
                .disabled(!customerAuth.getIsActive())
                .build();
    }
}


