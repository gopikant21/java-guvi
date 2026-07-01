package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Task 3: Derived Query Method
    List<Customer> findByBranch(String branch);

    // Security Helper
    Optional<Customer> findByEmail(String email);

    // Task 4: Find Rich Customers - customers whose total balance exceeds threshold
    @Query(value = "SELECT DISTINCT c FROM Customer c " +
            "LEFT JOIN FETCH c.accounts a " +
            "WHERE (SELECT COALESCE(SUM(a2.balance), 0) FROM a.customer c2 LEFT JOIN c2.accounts a2) > :threshold")
    List<Customer> findRichCustomers(@Param("threshold") double threshold);

    // Alternative approach using GROUP BY
    @Query("SELECT c FROM Customer c JOIN c.accounts a " +
            "GROUP BY c.customerId, c.customerName, c.email, c.password, c.branch " +
            "HAVING SUM(a.balance) > :threshold")
    List<Customer> findRichCustomersAlt(@Param("threshold") double threshold);

    // Task 4: Find customers having multiple accounts using COUNT and GROUP BY HAVING
    @Query("SELECT c FROM Customer c JOIN c.accounts a " +
            "GROUP BY c.customerId, c.customerName, c.email, c.password, c.branch " +
            "HAVING COUNT(a.accountNumber) > 1 " +
            "ORDER BY COUNT(a.accountNumber) DESC")
    List<Customer> findCustomersWithMultipleAccounts();

    // Task 4: Total balance per branch using GROUP BY and SUM
    @Query("SELECT c.branch, COALESCE(SUM(a.balance), 0) as totalBalance " +
            "FROM Customer c LEFT JOIN c.accounts a " +
            "GROUP BY c.branch " +
            "ORDER BY totalBalance DESC")
    List<Object[]> findTotalBalancePerBranch();

    // Task 7: Get customer summary DTO
    @Query("SELECT new org.northernarc.assessment4.dto.CustomerSummaryDTO(" +
            "c.customerName, c.branch, COUNT(a.accountNumber), COALESCE(SUM(a.balance), 0)) " +
            "FROM Customer c LEFT JOIN c.accounts a " +
            "WHERE c.customerId = :customerId " +
            "GROUP BY c.customerId, c.customerName, c.branch")
    Optional<CustomerSummaryDTO> findCustomerSummary(@Param("customerId") Long customerId);

    // Dashboard: top branch by total branch balance (single-row via Pageable)
    @Query("SELECT c.branch FROM Customer c LEFT JOIN c.accounts a " +
            "GROUP BY c.branch " +
            "ORDER BY COALESCE(SUM(a.balance), 0) DESC, c.branch ASC")
    List<String> findTopBranchByBalance(Pageable pageable);

    // Dashboard: highest balance customer based on total balance across all their accounts
    @Query("SELECT c.customerName FROM Customer c LEFT JOIN c.accounts a " +
            "GROUP BY c.customerId, c.customerName " +
            "ORDER BY COALESCE(SUM(a.balance), 0) DESC, c.customerName ASC")
    List<String> findCustomersByHighestBalance(Pageable pageable);
}
