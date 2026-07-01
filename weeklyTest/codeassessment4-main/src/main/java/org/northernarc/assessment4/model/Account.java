package org.northernarc.assessment4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @NotBlank(message = "Account type cannot be blank")
    @Column(nullable = false)
    private String accountType;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be positive or zero")
    @Column(nullable = false)
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer cannot be null")
    @JsonIgnoreProperties({"accounts", "password"})
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
}