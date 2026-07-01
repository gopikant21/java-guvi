package org.northernarc.assessment4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String customerName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Branch cannot be blank")
    @Size(min = 2, max = 50, message = "Branch name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String branch;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
}