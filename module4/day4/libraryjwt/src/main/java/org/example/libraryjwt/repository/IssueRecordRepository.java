package org.example.libraryjwt.repository;

import org.example.libraryjwt.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for IssueRecord entity.
 * Task 4: JPQL Queries for complex business logic
 */
@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    /**
     * Find all issue records for a specific member
     */
    List<IssueRecord> findByMemberMemberId(Long memberId);

    /**
     * Find all issue records for a specific book
     */
    List<IssueRecord> findByBookIsbn(String isbn);

    /**
     * Find overdue records (those with OVERDUE status)
     */
    @Query("SELECT ir FROM IssueRecord ir WHERE ir.status = 'OVERDUE'")
    List<IssueRecord> findOverdueRecords();

    /**
     * Find issued records (those with ISSUED status)
     */
    @Query("SELECT ir FROM IssueRecord ir WHERE ir.status = 'ISSUED'")
    List<IssueRecord> findIssuedRecords();

    /**
     * Count issue records for a member (for analytics)
     */
    @Query("SELECT COUNT(ir) FROM IssueRecord ir WHERE ir.member.memberId = :memberId")
    long countByMemberId(@Param("memberId") Long memberId);
}

