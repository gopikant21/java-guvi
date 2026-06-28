package org.example.libraryjwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Book entity representing library books.
 * Task 1: Entity Mapping - Configure relationships
 * Task 2: Validation - Apply Bean Validation annotations
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank(message = "Book title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Book type cannot be blank")
    @Column(nullable = false)
    private String bookType;

    @Positive(message = "Daily fine rate must be positive")
    @Column(nullable = false)
    private Double dailyFineRate;

    /**
     * One-to-Many relationship with IssueRecord
     * Cascade all operations and use lazy loading
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<IssueRecord> issueRecords = new ArrayList<>();

    /**
     * One-to-Many relationship with FineTransaction
     * Cascade all operations and use lazy loading
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<FineTransaction> fineTransactions = new ArrayList<>();
}

