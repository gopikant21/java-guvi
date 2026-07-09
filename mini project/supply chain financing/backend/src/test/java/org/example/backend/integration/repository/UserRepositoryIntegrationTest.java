package org.example.backend.integration.repository;

import org.example.backend.entity.AppUser;
import org.example.backend.entity.Role;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailAndRoleShouldWork() {
        AppUser admin = AppUser.builder()
                .name("Admin")
                .email("admin-int@test.local")
                .password("encoded")
                .role(Role.ADMIN)
                .phone("9000000000")
                .createdAt(LocalDateTime.now())
                .build();

        AppUser customer = AppUser.builder()
                .name("Customer")
                .email("customer-int@test.local")
                .password("encoded")
                .role(Role.CUSTOMER)
                .phone("9999999999")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        userRepository.save(customer);

        Optional<AppUser> found = userRepository.findByEmail("customer-int@test.local");
        List<AppUser> customers = userRepository.findByRole(Role.CUSTOMER);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Customer");
        assertThat(customers).extracting(AppUser::getEmail).contains("customer-int@test.local");
        assertThat(userRepository.existsByEmail("admin-int@test.local")).isTrue();
    }
}

