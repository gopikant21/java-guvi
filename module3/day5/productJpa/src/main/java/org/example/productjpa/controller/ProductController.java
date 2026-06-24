package org.example.productjpa.controller;

import jakarta.validation.Valid;
import org.example.productjpa.dto.ProductRequestDto;
import org.example.productjpa.dto.ProductResponseDto;
import org.example.productjpa.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    // Create product
    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto newProduct = productService.addProduct(productRequestDto);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Search by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // Search by brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrand(@PathVariable String brand) {
        List<ProductResponseDto> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }

    // Search by price range
    @GetMapping("/price")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductResponseDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam String name) {
        List<ProductResponseDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    // Get available products
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProducts() {
        List<ProductResponseDto> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    // Get available products by category
    @GetMapping("/available/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProductsByCategory(@PathVariable String category) {
        List<ProductResponseDto> products = productService.getAvailableProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // Reduce stock
    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<String> reduceStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.reduceStock(id, quantity);
        return ResponseEntity.ok("Stock reduced by " + quantity);
    }

    // Increase stock
    @PutMapping("/{id}/increase-stock")
    public ResponseEntity<String> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.increaseStock(id, quantity);
        return ResponseEntity.ok("Stock increased by " + quantity);
    }

    // Custom Query Endpoints

    // Get low stock products below threshold
    @GetMapping("/low-stock/{threshold}")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts(@PathVariable int threshold) {
        List<ProductResponseDto> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    // Get low stock products by category
    @GetMapping("/low-stock/category/{category}/{threshold}")
    public ResponseEntity<List<ProductResponseDto>> getLowStockByCategory(
            @PathVariable String category,
            @PathVariable int threshold) {
        List<ProductResponseDto> products = productService.getLowStockByCategory(category, threshold);
        return ResponseEntity.ok(products);
    }

    // Get product count by category
    @GetMapping("/count/category/{category}")
    public ResponseEntity<Long> getProductCountByCategory(@PathVariable String category) {
        long count = productService.getProductCountByCategory(category);
        return ResponseEntity.ok(count);
    }

    // Get products never ordered
    @GetMapping("/unordered")
    public ResponseEntity<List<ProductResponseDto>> getUnorderedProducts() {
        List<ProductResponseDto> products = productService.getUnorderedProducts();
        return ResponseEntity.ok(products);
    }

    // UPDATE: Increase price by percentage for category
    @PutMapping("/bulk-update/price-by-percentage")
    public ResponseEntity<String> updatePriceByPercentageForCategory(
            @RequestParam String category,
            @RequestParam Double percentage) {
        int updated = productService.updatePriceByPercentageForCategory(category, percentage);
        return ResponseEntity.ok("Product prices updated by " + percentage + "% for category: " + category + ". Records affected: " + updated);
    }

    // UPDATE: Clear stock for category
    @PutMapping("/bulk-update/clear-stock/{category}")
    public ResponseEntity<String> clearStockByCategory(@PathVariable String category) {
        int updated = productService.clearStockByCategory(category);
        return ResponseEntity.ok("Stock cleared for category: " + category + ". Records affected: " + updated);
    }

    // UPDATE: Restock product
    @PutMapping("/{id}/restock")
    public ResponseEntity<String> restockProduct(
            @PathVariable Long id,
            @RequestParam int quantity) {
        int updated = productService.restockProduct(id, quantity);
        return ResponseEntity.ok("Product restocked with " + quantity + " units. Records affected: " + updated);
    }
}
