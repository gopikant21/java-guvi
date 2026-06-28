package org.example.libraryjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task 7: DTO Projection Mapping
 * MemberSummaryDTO - Returns custom projection without exposing domain entity layers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSummaryDTO {
    private Long memberId;
    private String memberName;
    private String membershipBranch;
    private Long numberOfBorrowedBooks;
    private Double totalFinesPaid;
}

