package org.example.emidefaulter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @NotNull(message = "Loan type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;

    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be positive")
    @Column(nullable = false)
    private Double loanAmount;

    @NotNull(message = "Interest rate is required")
    @PositiveOrZero(message = "Interest rate must be zero or positive")
    @Column(nullable = false)
    private Double interestRate;

    @NotNull(message = "Tenure months is required")
    @Positive(message = "Tenure months must be positive")
    @Column(nullable = false)
    private Integer tenureMonths;

    @NotNull(message = "EMI amount is required")
    @Positive(message = "EMI amount must be positive")
    @Column(nullable = false)
    private Double emiAmount;

    @NotNull(message = "Loan status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<EmiPayment> emiPayments = new ArrayList<>();
}


