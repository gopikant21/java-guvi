package org.example.emidefaulter.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user details for username: {}", username);
        try {
            Customer customer = customerRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        log.warn("User not found with email: {}", username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });

            UserDetails userDetails = User.builder()
                    .username(customer.getEmail())
                    .password(customer.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole().name())))
                    .build();

            log.info("User details loaded successfully for username: {} with role: {}", username, customer.getRole());
            return userDetails;
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error loading user details for username: {}", username, e);
            throw e;
        }
    }
}

