package org.example.productspringsecurity.repository;

import org.example.productspringsecurity.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Derived query methods
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    // Custom @Query methods

    // Find orders placed within a date range
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Get total spent by a customer (sum of all order items price * quantity)
    @Query("SELECT COALESCE(SUM(oi.product.price * oi.quantity), 0) FROM Order o JOIN o.orderItems oi WHERE o.customer.id = :customerId")
    Double getTotalSpentByCustomer(@Param("customerId") Long customerId);

    // Find high-value orders (orders where total > threshold)
    @Query("SELECT o FROM Order o WHERE (SELECT COALESCE(SUM(oi.product.price * oi.quantity), 0) FROM o.orderItems oi) > :threshold ORDER BY o.orderId DESC")
    List<Order> findHighValueOrders(@Param("threshold") Double threshold);

    // Count orders for a customer
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.id = :customerId")
    long countOrdersByCustomer(@Param("customerId") Long customerId);

    // Get average order value per customer
    @Query("SELECT AVG(oi.product.price * oi.quantity) FROM Order o JOIN o.orderItems oi WHERE o.customer.id = :customerId")
    Double getAverageOrderItemValueByCustomer(@Param("customerId") Long customerId);

    // Find orders with no items
    @Query("SELECT o FROM Order o WHERE o.id NOT IN (SELECT DISTINCT oi.order.orderId FROM OrderItem oi)")
    List<Order> findEmptyOrders();
}
