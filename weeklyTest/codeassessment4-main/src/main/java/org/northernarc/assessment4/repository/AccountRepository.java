package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    // Task 3: Derived Query Methods
    List<Account> findByAccountType(String accountType);
    List<Account> findByBalanceGreaterThan(double amount);

    // Task 4: Find accounts with no transactions (LEFT JOIN)
    @Query("SELECT a FROM Account a LEFT JOIN a.transactions t " +
            "WHERE t.transactionId IS NULL")
    List<Account> findAccountsWithNoTransactions();

    // Task 4: Get latest transaction's account
    @Query("SELECT a FROM Account a JOIN a.transactions t " +
            "ORDER BY t.transactionDate DESC LIMIT 1")
    List<Account> findAccountsWithLatestTransaction();

    // Task 5: Update account balance using @Modifying
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    int increaseBalance(@Param("accountNumber") String accountNumber, @Param("amount") double amount);

    // Task 6: Pagination and Sorting - Get all accounts paginated, sorted by balance DESC
    @Query("SELECT a FROM Account a")
    Page<Account> findAllAccounts(Pageable pageable);

    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a")
    Double getTotalBalance();

}
