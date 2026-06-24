package org.example.productspringsecurity.repository;

import org.example.productspringsecurity.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Derived query methods
    Page<OrderItem> findByOrderOrderId(Long orderId, Pageable pageable);

    // Custom @Query methods

    // Find best-selling products (most ordered by quantity)
    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) as totalSold FROM OrderItem oi GROUP BY oi.product.id, oi.product.name ORDER BY totalSold DESC")
    List<Object[]> findBestSellingProducts();

    // Find order items by product with high quantity orders
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId AND oi.quantity > :minQuantity ORDER BY oi.quantity DESC")
    List<OrderItem> findHighQuantityOrdersByProduct(@Param("productId") Long productId, @Param("minQuantity") int minQuantity);

    // Get total quantity ordered for a specific product
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    long getTotalQuantityOrderedForProduct(@Param("productId") Long productId);

    // Get total revenue for a product
    @Query("SELECT COALESCE(SUM(oi.product.price * oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Double getTotalRevenueForProduct(@Param("productId") Long productId);

    // Find products with average order quantity above threshold
    @Query("SELECT oi.product.id, oi.product.name, AVG(oi.quantity) as avgQuantity FROM OrderItem oi GROUP BY oi.product.id, oi.product.name HAVING AVG(oi.quantity) > :threshold ORDER BY avgQuantity DESC")
    List<Object[]> findProductsWithHighAverageQuantity(@Param("threshold") double threshold);

    // Find most expensive order items (by single item price * quantity)
    @Query("SELECT oi FROM OrderItem oi ORDER BY (oi.product.price * oi.quantity) DESC")
    List<OrderItem> findMostExpensiveOrderItems(Pageable pageable);

    // Get total items count ordered in a date range (needs Order.orderDate)
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate")
    long countItemsOrderedInDateRange(@Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
}
