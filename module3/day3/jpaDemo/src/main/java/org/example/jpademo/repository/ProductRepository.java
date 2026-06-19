package org.example.jpademo.repository;


import org.example.jpademo.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    // CRUD
    Object save(Product product);

    Product getById(int id);

    List<Product> getAll();

    void update(int id, Product product);

    void delete(int id);

    // Search
    Product getByName(String name);

    List<Product> searchByKeyword(String keyword);

    // Category & Brand
    List<Product> getByCategory(String category);

    List<Product> getByBrand(String brand);

    // Price filtering
    List<Product> getAbovePrice(double price);

    List<Product> getBelowPrice(double price);

    List<Product> getByPriceRange(double min, double max);

    // Stock operations
    List<Product> getInStockProducts();

    List<Product> getOutOfStockProducts();

    void updateStock(int id, int quantity);

    void reduceStock(int id, int quantity);

    // Sorting
    List<Product> sortByPriceAsc();

    List<Product> sortByPriceDesc();

    List<Product> sortByRating();

    List<Product> sortByName();

    // Analytics
    double getAveragePrice();

    double getMaxPrice();

    double getMinPrice();

    long totalProducts();

    Map<String, Long> countByCategory();

    Map<String, Long> countByBrand();

    double totalInventoryValue();
}