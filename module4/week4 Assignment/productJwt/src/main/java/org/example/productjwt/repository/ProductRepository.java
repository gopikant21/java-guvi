package org.example.productjwt.repository;

import org.example.productjwt.model.Product;
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
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Derived query methods
    Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<Product> findByBrandIgnoreCase(String brand, Pageable pageable);
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByStocksGreaterThan(int stocks, Pageable pageable);
    Page<Product> findByCategoryIgnoreCaseAndStocksGreaterThan(String category, int stocks, Pageable pageable);

    // Custom @Query methods

    // Find products with stock below threshold
    @Query("SELECT p FROM Product p WHERE p.stocks < :threshold ORDER BY p.stocks ASC")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    // Find products by category with low stock
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category) AND p.stocks < :threshold")
    List<Product> findLowStockByCategory(@Param("category") String category, @Param("threshold") int threshold);

    // Count total products by category
    @Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    long countByCategory(@Param("category") String category);

    // Find products not in any orders
    @Query("SELECT p FROM Product p WHERE p.id NOT IN (SELECT DISTINCT oi.product.id FROM OrderItem oi)")
    List<Product> findUnorderedProducts();

    // UPDATE query: Increase price by percentage for all products in a category
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * (1 + :percentage / 100) WHERE LOWER(p.category) = LOWER(:category)")
    int updatePriceByPercentageForCategory(@Param("category") String category, @Param("percentage") Double percentage);

    // UPDATE query: Set stock to zero for products in a category
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stocks = 0 WHERE LOWER(p.category) = LOWER(:category)")
    int clearStockByCategory(@Param("category") String category);

    // UPDATE query: Restock products with quantity
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stocks = p.stocks + :quantity WHERE p.id = :productId")
    int restockProduct(@Param("productId") Long productId, @Param("quantity") int quantity);
}
