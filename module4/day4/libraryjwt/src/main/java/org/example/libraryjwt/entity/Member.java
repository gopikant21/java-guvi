package org.example.libraryjwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Member entity representing a library member.
 * Task 1: Entity Mapping - Configure relationships and cascading
 * Task 2: Validation - Apply Bean Validation annotations
 */
@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotBlank(message = "Member name cannot be blank")
    @Column(nullable = false)
    private String memberName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Membership branch cannot be blank")
    @Column(nullable = false)
    private String membershipBranch;

    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";

    /**
     * One-to-Many relationship with IssueRecord
     * Cascade all operations and use lazy loading
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<IssueRecord> issueRecords = new ArrayList<>();

    /**
     * One-to-Many relationship with FineTransaction
     * Though not explicitly modeled, we include this for completeness
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<FineTransaction> fineTransactions = new ArrayList<>();
}

