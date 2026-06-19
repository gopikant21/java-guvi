package org.example.jpademo.service;

import org.example.jpademo.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    // CRUD
    void save(Product product);

    Product getById(int id);

    List<Product> getAll();

    void update(int id, Product product);

    void delete(int id);

    // Search
    Product getByName(String name);

    List<Product> search(String keyword);

    // Category / Brand
    List<Product> getByCategory(String category);

    List<Product> getByBrand(String brand);

    // Price operations
    List<Product> getExpensiveProducts(double price);

    List<Product> getCheapProducts(double price);

    List<Product> getByPriceRange(double min, double max);

    // Stock management
    List<Product> getInStock();

    List<Product> getOutOfStock();

    void purchaseProduct(int id, int quantity);   // reduces stock

    void restockProduct(int id, int quantity);    // increases stock

    // Sorting
    List<Product> sortByPriceLowToHigh();

    List<Product> sortByPriceHighToLow();

    List<Product> sortByRating();

    List<Product> sortByName();

    // Analytics (business intelligence)
    double getAveragePrice();

    double getMaxPrice();

    double getMinPrice();

    long totalProducts();

    double totalInventoryValue();

    Map<String, Long> countByCategory();

    Map<String, Long> countByBrand();

    // Advanced business logic
    List<Product> getTopRatedProducts(int limit);

    List<Product> getDiscountedProducts(double minRating);
}