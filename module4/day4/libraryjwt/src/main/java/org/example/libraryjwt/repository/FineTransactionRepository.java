package org.example.libraryjwt.repository;

import org.example.libraryjwt.entity.FineTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for FineTransaction entity.
 * Task 3: Spring Data JPA Derived Queries
 * Task 4: JPQL Queries
 */
@Repository
public interface FineTransactionRepository extends JpaRepository<FineTransaction, Long> {

    /**
     * Task 3: Derived query - Find fine transactions by payment type
     */
    List<FineTransaction> findByPaymentType(String paymentType);

    /**
     * Find fine transactions for a specific member
     */
    List<FineTransaction> findByMemberMemberId(Long memberId);

    /**
     * Find fine transactions for a specific book
     */
    List<FineTransaction> findByBookIsbn(String isbn);

    /**
     * Task 4: JPQL Query - Find Latest Fine Payment
     * Returns the single most recent FineTransaction entry using ORDER BY DESC and LIMIT 1
     */
    @Query(value = "SELECT ft FROM FineTransaction ft " +
                   "ORDER BY ft.paymentDate DESC LIMIT 1",
           nativeQuery = false)
    Optional<FineTransaction> findLatestFinePayment();

    /**
     * Get total fines collected for analytics
     */
    @Query("SELECT SUM(ft.amount) FROM FineTransaction ft")
    Double getTotalFinesCollected();

    /**
     * Get total fines paid by a member
     */
    @Query("SELECT SUM(ft.amount) FROM FineTransaction ft WHERE ft.member.memberId = :memberId")
    Double getTotalFinesPaidByMember(@Param("memberId") Long memberId);
}

