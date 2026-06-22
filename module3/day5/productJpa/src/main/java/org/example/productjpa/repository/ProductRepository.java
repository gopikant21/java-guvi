package org.example.productjpa.repository;

import org.example.productjpa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findByNameContainingIgnoreCase(String name);
    Optional<Product> findByNameAndBrand(String name, String brand);

    @Query("SELECT p FROM Product p WHERE p.stocks > 0")
    List<Product> findAvailableProducts();

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.stocks > 0")
    List<Product> findAvailableProductsByCategory(@Param("category") String category);
}

