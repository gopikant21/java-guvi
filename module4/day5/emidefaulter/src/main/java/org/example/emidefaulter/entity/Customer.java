package org.example.emidefaulter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String customerName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @Min(value = 300, message = "Credit score must be between 300 and 900")
    @Max(value = 900, message = "Credit score must be between 300 and 900")
    @Column(nullable = false)
    private Integer creditScore;

    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    @Builder.Default
    private String role = "CUSTOMER";

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Loan> loans = new ArrayList<>();
}


