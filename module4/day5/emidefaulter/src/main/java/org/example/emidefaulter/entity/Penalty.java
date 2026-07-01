package org.example.emidefaulter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "penalties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long penaltyId;

    @NotNull(message = "Penalty amount is required")
    @Positive(message = "Penalty amount must be positive")
    @Column(nullable = false)
    private Double penaltyAmount;

    @NotBlank(message = "Penalty reason is required")
    @Column(nullable = false)
    private String penaltyReason;

    @NotNull(message = "Generated date is required")
    @Column(nullable = false)
    private LocalDate generatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @JsonIgnore
    private EmiPayment payment;
}


