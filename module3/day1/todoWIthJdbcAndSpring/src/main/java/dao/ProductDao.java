package dao;

import entity.Product;
import java.util.List;

public interface ProductDao {

    // Create
    void save(Product product);

    // Read
    Product findById(int id);
    List<Product> findAll();

    // Update
    void update(Product product);

    // Delete
    void delete(int id);

    // Additional Queries
    List<Product> findByName(String name);
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);

    List<Product> findAvailableProducts();

    List<Product> findByPriceRange(double minPrice, double maxPrice);

    // Count
    long countProducts();
}
