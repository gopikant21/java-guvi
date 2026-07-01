package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Task 3: Derived Query Method
    List<Transaction> findByTransactionType(String transactionType);

    // Task 4: Latest transaction using pageable (for tests calling findLatestTransaction(PageRequest...))
    @Query("SELECT t FROM Transaction t ORDER BY t.transactionDate DESC")
    List<Transaction> findLatestTransaction(Pageable pageable);

    // Task 4: Latest transaction as single optional item (for service usage)
    Optional<Transaction> findTopByOrderByTransactionDateDesc();

    // Find transactions by account number
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByAccountNumber(String accountNumber);
}
