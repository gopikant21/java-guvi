package org.example.productjpa.services;

import org.example.productjpa.exceptions.ResourceNotFoundException;
import org.example.productjpa.model.Product;
import org.example.productjpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create
    public Product addProduct(Product product) {
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (product.getStocks() < 0) {
            throw new IllegalArgumentException("Product stocks cannot be negative");
        }
        return productRepository.save(product);
    }

    // Read
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Update
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getPrice() != null && productDetails.getPrice() > 0) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        if (productDetails.getBrand() != null) {
            product.setBrand(productDetails.getBrand());
        }
        if (productDetails.getStocks() >= 0) {
            product.setStocks(productDetails.getStocks());
        }

        return productRepository.save(product);
    }

    // Delete
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    // Search operations
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    public List<Product> getAvailableProductsByCategory(String category) {
        return productRepository.findAvailableProductsByCategory(category);
    }

    // Stock management
    public void updateStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        int newStock = product.getStocks() + quantity;

        if (newStock < 0) {
            throw new IllegalArgumentException("Cannot reduce stock below zero");
        }

        product.setStocks(newStock);
        productRepository.save(product);
    }

    public void reduceStock(Long productId, int quantity) {
        updateStock(productId, -quantity);
    }

    public void increaseStock(Long productId, int quantity) {
        updateStock(productId, quantity);
    }
}


