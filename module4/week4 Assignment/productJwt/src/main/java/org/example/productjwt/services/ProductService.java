package org.example.productjwt.services;

import org.example.productjwt.dto.ProductRequestDto;
import org.example.productjwt.dto.ProductResponseDto;
import org.example.productjwt.exceptions.ResourceNotFoundException;
import org.example.productjwt.mapper.ProductMapper;
import org.example.productjwt.model.Product;
import org.example.productjwt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // Create
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (productRequestDto.getStocks() != null && productRequestDto.getStocks() < 0) {
            throw new IllegalArgumentException("Product stocks cannot be negative");
        }
        Product product = productMapper.toEntity(productRequestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    // Read
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));
        return productMapper.toResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productMapper.toResponseDtoList(productRepository.findAll());
    }

    // Update
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));

        if (productRequestDto.getPrice() != null && productRequestDto.getPrice() > 0) {
            if (productRequestDto.getPrice() <= 0) {
                throw new IllegalArgumentException("Product price must be greater than zero");
            }
        }

        productMapper.updateEntityFromDto(productRequestDto, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponseDto(updatedProduct);
    }

    // Delete
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));
        productRepository.delete(product);
    }

    // Search operations
    public List<ProductResponseDto> getProductsByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductsByBrand(String brand) {
        return productRepository.findAll().stream()
                .filter(p -> brand.equalsIgnoreCase(p.getBrand()))
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> searchProductsByName(String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getAvailableProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStocks() > 0)
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getAvailableProductsByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()) && p.getStocks() > 0)
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Stock management
    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));
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

    // Custom Query Methods

    // Get low stock products below threshold
    public List<ProductResponseDto> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get low stock products in specific category
    public List<ProductResponseDto> getLowStockByCategory(String category, int threshold) {
        return productRepository.findLowStockByCategory(category, threshold).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get count of products in category
    public long getProductCountByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    // Get products that have never been ordered
    public List<ProductResponseDto> getUnorderedProducts() {
        return productRepository.findUnorderedProducts().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // UPDATE: Increase price by percentage for all products in a category
    public int updatePriceByPercentageForCategory(String category, Double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Percentage cannot be negative");
        }
        return productRepository.updatePriceByPercentageForCategory(category, percentage);
    }

    // UPDATE: Clear stock for all products in a category
    public int clearStockByCategory(String category) {
        return productRepository.clearStockByCategory(category);
    }

    // UPDATE: Restock a product by quantity
    public int restockProduct(Long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        // Verify product exists
        getProductById(productId);
        return productRepository.restockProduct(productId, quantity);
    }

    // Internal method to get entity (used by OrderService)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));
    }
}
