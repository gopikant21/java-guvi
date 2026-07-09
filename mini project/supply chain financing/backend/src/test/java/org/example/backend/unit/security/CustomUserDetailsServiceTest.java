package org.example.backend.unit.security;

import org.example.backend.entity.AppUser;
import org.example.backend.entity.Role;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameShouldReturnSpringSecurityUser() {
        AppUser appUser = AppUser.builder()
                .id(1L)
                .name("Admin")
                .email("admin@test.local")
                .password("encoded")
                .role(Role.ADMIN)
                .phone("9000000000")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("admin@test.local")).thenReturn(Optional.of(appUser));

        UserDetails details = customUserDetailsService.loadUserByUsername("admin@test.local");

        assertThat(details.getUsername()).isEqualTo("admin@test.local");
        assertThat(details.getAuthorities()).extracting("authority").contains("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsernameShouldThrowForUnknownUser() {
        when(userRepository.findByEmail("missing@test.local")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("missing@test.local"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}

