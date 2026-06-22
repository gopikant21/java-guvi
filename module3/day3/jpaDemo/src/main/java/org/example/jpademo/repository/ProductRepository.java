package org.example.jpademo.repository;

import jakarta.transaction.Transactional;
import org.example.jpademo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Query methods
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product getById(@Param("id") int id);

    @Query("SELECT p FROM Product p")
    List<Product> getAll();

    default void update(int id, Product product) {
        product.setId(id);
        product.setInStock(product.getStockQuantity() > 0);
        save(product);
    }

    @Transactional
    default void delete(int id) {
        deleteById(id);
    }

    Product getByName(String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    List<Product> getByCategory(String category);

    List<Product> getByBrand(String brand);

    @Query("SELECT p FROM Product p WHERE p.price > :price")
    List<Product> getAbovePrice(@Param("price") double price);

    @Query("SELECT p FROM Product p WHERE p.price < :price")
    List<Product> getBelowPrice(@Param("price") double price);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> getByPriceRange(@Param("min") double min, @Param("max") double max);

    @Query("SELECT p FROM Product p WHERE p.inStock = true")
    List<Product> getInStockProducts();

    @Query("SELECT p FROM Product p WHERE p.inStock = false")
    List<Product> getOutOfStockProducts();

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity, p.inStock = CASE WHEN (p.stockQuantity - :quantity) > 0 THEN true ELSE false END WHERE p.id = :id")
    void reduceStock(@Param("id") int id, @Param("quantity") int quantity);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = :stockQuantity, p.inStock = CASE WHEN :stockQuantity > 0 THEN true ELSE false END WHERE p.id = :id")
    void updateStock(@Param("id") int id, @Param("stockQuantity") int stockQuantity);

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> sortByPriceAsc();

    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> sortByPriceDesc();

    @Query("SELECT p FROM Product p ORDER BY p.rating DESC")
    List<Product> sortByRating();

    @Query("SELECT p FROM Product p ORDER BY p.name ASC")
    List<Product> sortByName();

    @Query("SELECT AVG(p.price) FROM Product p")
    double getAveragePrice();

    @Query("SELECT MAX(p.price) FROM Product p")
    double getMaxPrice();

    @Query("SELECT MIN(p.price) FROM Product p")
    double getMinPrice();

    @Query("SELECT COUNT(p) FROM Product p")
    long totalProducts();

    @Query("SELECT SUM(p.price * p.stockQuantity) FROM Product p")
    double totalInventoryValue();

    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    Map<String, Long> countByCategory();

    @Query("SELECT p.brand, COUNT(p) FROM Product p GROUP BY p.brand")
    Map<String, Long> countByBrand();
}