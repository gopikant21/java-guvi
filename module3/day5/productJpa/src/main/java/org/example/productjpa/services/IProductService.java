package org.example.productjpa.services;

import org.example.productjpa.dto.ProductRequestDto;
import org.example.productjpa.dto.ProductResponseDto;

import java.util.List;

public interface IProductService {

    // Create
    ProductResponseDto addProduct(ProductRequestDto productRequestDto);

    // Read
    ProductResponseDto getProductById(Long id);

    List<ProductResponseDto> getAllProducts();

    // Update
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    // Delete
    void deleteProduct(Long id);

    // Search operations
    List<ProductResponseDto> getProductsByCategory(String category);

    List<ProductResponseDto> getProductsByBrand(String brand);

    List<ProductResponseDto> getProductsByPriceRange(Double minPrice, Double maxPrice);

    List<ProductResponseDto> searchProductsByName(String name);

    List<ProductResponseDto> getAvailableProducts();

    List<ProductResponseDto> getAvailableProductsByCategory(String category);

    // Stock management
    void updateStock(Long productId, int quantity);

    void reduceStock(Long productId, int quantity);

    void increaseStock(Long productId, int quantity);

    // Custom Query Methods
    List<ProductResponseDto> getLowStockProducts(int threshold);

    List<ProductResponseDto> getLowStockByCategory(String category, int threshold);

    long getProductCountByCategory(String category);

    List<ProductResponseDto> getUnorderedProducts();

    // UPDATE queries
    int updatePriceByPercentageForCategory(String category, Double percentage);

    int clearStockByCategory(String category);

    int restockProduct(Long productId, int quantity);
}

