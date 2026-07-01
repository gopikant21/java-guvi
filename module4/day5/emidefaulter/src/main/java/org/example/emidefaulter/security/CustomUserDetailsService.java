package org.example.emidefaulter.security;

import lombok.RequiredArgsConstructor;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole())))
                .build();
    }
}

