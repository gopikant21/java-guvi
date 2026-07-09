package org.example.emidefaulter.repository;

import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {

    List<EmiPayment> findByPaymentStatus(PaymentStatus status);

    @Query("""
            select e
            from EmiPayment e
            where e.dueDate < :today
            and e.paymentDate is null
            and e.paymentStatus = org.example.emidefaulter.entity.PaymentStatus.PENDING
            """)
    List<EmiPayment> findOverduePendingPayments(@Param("today") LocalDate today);

    @Query("""
            select e
            from EmiPayment e
            join fetch e.loan l
            join l.customer c
            where c.email = :email
            """)
    List<EmiPayment> findEmiHistoryByCustomerEmail(@Param("email") String email);

    @Query("select coalesce(sum(e.amountPaid), 0) from EmiPayment e")
    Double sumTotalAmountPaid();

    @Query("select coalesce(sum(l.emiAmount), 0) from EmiPayment e join e.loan l")
    Double sumTotalDueAmount();
}

