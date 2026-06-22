package org.example.productjpa.repository;

import org.example.productjpa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.orderId DESC")
    List<Order> findOrdersByCustomerIdOrderByNewest(@Param("customerId") Long customerId);

    Long countByCustomerId(Long customerId);
}

