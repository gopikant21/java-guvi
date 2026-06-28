package org.example.libraryjwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * IssueRecord entity representing book circulation records.
 * Task 1: Entity Mapping - Configure relationships with proper cascading
 * Task 2: Validation - Apply Bean Validation annotations
 */
@Entity
@Table(name = "issue_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @NotNull(message = "Issue date cannot be null")
    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = true)
    private LocalDate returnDate;

    @NotBlank(message = "Status cannot be blank")
    @Column(nullable = false)
    private String status; // ISSUED, RETURNED, OVERDUE

    /**
     * Many-to-One relationship with Member
     * Lazy loading to prevent N+1 queries
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull(message = "Member cannot be null")
    private Member member;

    /**
     * Many-to-One relationship with Book
     * Lazy loading to prevent N+1 queries
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn", nullable = false)
    @NotNull(message = "Book cannot be null")
    private Book book;
}

