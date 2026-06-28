package org.example.libraryjwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * FineTransaction entity representing fine payment records.
 * Task 1: Entity Mapping - Configure relationships
 * Task 2: Validation - Apply Bean Validation annotations
 */
@Entity
@Table(name = "fine_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private Double amount;

    @NotBlank(message = "Payment type cannot be blank")
    @Column(nullable = false)
    private String paymentType; // CASH, CARD, ONLINE

    @NotNull(message = "Payment date cannot be null")
    @Column(nullable = false)
    private LocalDate paymentDate;

    /**
     * Many-to-One relationship with Book
     * Lazy loading to prevent N+1 queries
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn", nullable = false)
    @NotNull(message = "Book cannot be null")
    private Book book;

    /**
     * Many-to-One relationship with Member
     * Lazy loading to prevent N+1 queries
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull(message = "Member cannot be null")
    private Member member;
}

