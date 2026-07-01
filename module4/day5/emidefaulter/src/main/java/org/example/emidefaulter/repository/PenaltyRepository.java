package org.example.emidefaulter.repository;

import org.example.emidefaulter.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Query("""
            select p
            from Penalty p
            order by p.generatedDate desc, p.penaltyId desc
            """)
    java.util.List<Penalty> findLatestPenaltyRecords(org.springframework.data.domain.Pageable pageable);

    Optional<Penalty> findFirstByPaymentPaymentId(Long paymentId);

    @Query("select coalesce(sum(p.penaltyAmount), 0) from Penalty p")
    Double sumTotalPenaltyCollected();
}

