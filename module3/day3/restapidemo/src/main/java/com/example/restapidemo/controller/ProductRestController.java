package com.example.restapidemo.controller;

import com.example.restapidemo.model.Product;
import com.example.restapidemo.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductRestController {

    @Autowired
    private ProductService productService;

    // ---------------- CRUD ----------------

    @PostMapping
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable @Positive int id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable @Positive int id,
                                @Valid @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable @Positive int id) {
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ---------------- SEARCH ----------------

    @GetMapping("/search")
    public ResponseEntity<Product> getByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.getByName(name));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.search(keyword));
    }

    // ---------------- CATEGORY & BRAND ----------------

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getByCategory(category));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(productService.getByBrand(brand));
    }

    // ---------------- PRICE FILTERS ----------------

    @GetMapping("/price/above/{price}")
    public ResponseEntity<List<Product>> getExpensive(@PathVariable @PositiveOrZero double price) {
        return ResponseEntity.ok(productService.getExpensiveProducts(price));
    }

    @GetMapping("/price/below/{price}")
    public ResponseEntity<List<Product>> getCheap(@PathVariable @PositiveOrZero double price) {
        return ResponseEntity.ok(productService.getCheapProducts(price));
    }

    @GetMapping("/price/range")
    public ResponseEntity<List<Product>> getByPriceRange(@RequestParam @PositiveOrZero double min,
                                         @RequestParam @PositiveOrZero double max) {
        return ResponseEntity.ok(productService.getByPriceRange(min, max));
    }

    // ---------------- STOCK APIs ----------------

    @GetMapping("/stock/in")
    public ResponseEntity<List<Product>> getInStock() {
        return ResponseEntity.ok(productService.getInStock());
    }

    @GetMapping("/stock/out")
    public ResponseEntity<List<Product>> getOutOfStock() {
        return ResponseEntity.ok(productService.getOutOfStock());
    }

    @PutMapping("/{id}/buy")
    public ResponseEntity<String> buyProduct(@PathVariable @Positive int id,
                             @RequestParam @Positive int quantity) {
        productService.purchaseProduct(id, quantity);
        return ResponseEntity.ok("Purchase successful");
    }

    @PutMapping("/{id}/restock")
    public ResponseEntity<String> restockProduct(@PathVariable @Positive int id,
                                 @RequestParam @Positive int quantity) {
        productService.restockProduct(id, quantity);
        return ResponseEntity.ok("Restock successful");
    }

    // ---------------- SORTING ----------------

    @GetMapping("/sort/price/asc")
    public ResponseEntity<List<Product>> sortPriceAsc() {
        return ResponseEntity.ok(productService.sortByPriceLowToHigh());
    }

    @GetMapping("/sort/price/desc")
    public ResponseEntity<List<Product>> sortPriceDesc() {
        return ResponseEntity.ok(productService.sortByPriceHighToLow());
    }

    @GetMapping("/sort/rating")
    public ResponseEntity<List<Product>> sortByRating() {
        return ResponseEntity.ok(productService.sortByRating());
    }

    @GetMapping("/sort/name")
    public ResponseEntity<List<Product>> sortByName() {
        return ResponseEntity.ok(productService.sortByName());
    }

    // ---------------- ANALYTICS ----------------

    @GetMapping("/stats/average-price")
    public ResponseEntity<Double> averagePrice() {
        return ResponseEntity.ok(productService.getAveragePrice());
    }

    @GetMapping("/stats/max-price")
    public ResponseEntity<Double> maxPrice() {
        return ResponseEntity.ok(productService.getMaxPrice());
    }

    @GetMapping("/stats/min-price")
    public ResponseEntity<Double> minPrice() {
        return ResponseEntity.ok(productService.getMinPrice());
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> totalProducts() {
        return ResponseEntity.ok(productService.totalProducts());
    }

    @GetMapping("/stats/inventory-value")
    public ResponseEntity<Double> inventoryValue() {
        return ResponseEntity.ok(productService.totalInventoryValue());
    }

    @GetMapping("/stats/category-count")
    public ResponseEntity<Map<String, Long>> categoryCount() {
        return ResponseEntity.ok(productService.countByCategory());
    }

    @GetMapping("/stats/brand-count")
    public ResponseEntity<Map<String, Long>> brandCount() {
        return ResponseEntity.ok(productService.countByBrand());
    }

    // ---------------- ADVANCED APIs ----------------

    @GetMapping("/top-rated")
    public ResponseEntity<List<Product>> topRated(@RequestParam(defaultValue = "5") @Positive int limit) {
        return ResponseEntity.ok(productService.getTopRatedProducts(limit));
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<Product>> discounted(
            @RequestParam @DecimalMin("0.0") @DecimalMax("5.0") double minRating) {
        return ResponseEntity.ok(productService.getDiscountedProducts(minRating));
    }
}