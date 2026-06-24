package org.example.productjpa.repository;

import org.example.productjpa.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Custom @Query methods

    // Find customers who have never placed any orders
    @Query("SELECT c FROM Customer c WHERE c.id NOT IN (SELECT DISTINCT o.customer.id FROM Order o)")
    List<Customer> findCustomersWithoutOrders();

    // Find customers by email domain (e.g., gmail.com)
    @Query("SELECT c FROM Customer c WHERE c.email LIKE %:domain")
    List<Customer> findByEmailDomain(@Param("domain") String domain);

    // Find top customers by order count
    @Query("SELECT c FROM Customer c ORDER BY (SELECT COUNT(o) FROM Order o WHERE o.customer.id = c.id) DESC")
    List<Customer> findTopCustomersByOrderCount();

    // Count total orders per customer with at least N orders
    @Query("SELECT c FROM Customer c WHERE (SELECT COUNT(o) FROM Order o WHERE o.customer.id = c.id) >= :minOrders")
    List<Customer> findCustomersWithMinOrders(@Param("minOrders") long minOrders);

    // Check if email exists (case-insensitive)
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE LOWER(c.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    // UPDATE query: Update customer address
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.address = :address WHERE c.id = :customerId")
    int updateCustomerAddress(@Param("customerId") Long customerId, @Param("address") String address);

    // UPDATE query: Update multiple customer contact fields
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.phone = :phone, c.address = :address WHERE c.id = :customerId")
    int updateCustomerContactInfo(@Param("customerId") Long customerId, @Param("phone") String phone, @Param("address") String address);
}
