package org.example.productjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Maps to customer_auth table.
 * Stores customer credentials separately from Customer entity.
 * Used for database-backed authentication instead of hardcoded users.
 */
@Entity
@Table(name = "customer_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    private String role = "ROLE_USER";

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

