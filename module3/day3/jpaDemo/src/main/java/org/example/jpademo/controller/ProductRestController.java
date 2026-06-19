package org.example.jpademo.controller;

import org.example.jpademo.service.ProductService;
import org.example.jpademo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    // ---------------- CRUD ----------------

    @PostMapping
    public String addProduct(@RequestBody Product product) {
        productService.save(product);
        return "Product added successfully";
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return ResponseEntity.status(200).body(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,
                                @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.status(200).body("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        productService.delete(id);
        return ResponseEntity.status(200).body("Product deleted successfully");
    }

    // ---------------- SEARCH ----------------

    @GetMapping("/search")
    public ResponseEntity<Product> getByName(@RequestParam String name) {
        return ResponseEntity.status(200).body(productService.getByName(name));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword) {
        return ResponseEntity.status(200).body(productService.search(keyword));
    }

    // ---------------- CATEGORY & BRAND ----------------

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.status(200).body(productService.getByCategory(category));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getByBrand(@PathVariable String brand) {
        return ResponseEntity.status(200).body(productService.getByBrand(brand));
    }

    // ---------------- PRICE FILTERS ----------------

    @GetMapping("/price/above/{price}")
    public ResponseEntity<List<Product>> getExpensive(@PathVariable double price) {
        return ResponseEntity.status(200).body(productService.getExpensiveProducts(price));
    }

    @GetMapping("/price/below/{price}")
    public ResponseEntity<List<Product>> getCheap(@PathVariable double price) {
        return ResponseEntity.status(200).body(productService.getCheapProducts(price));
    }

    @GetMapping("/price/range")
    public ResponseEntity<List<Product>> getByPriceRange(@RequestParam double min,
                                         @RequestParam double max) {
        return ResponseEntity.status(200).body(productService.getByPriceRange(min, max));
    }

    // ---------------- STOCK APIs ----------------

    @GetMapping("/stock/in")
    public ResponseEntity<List<Product>> getInStock() {
        return ResponseEntity.status(200).body(productService.getInStock());
    }

    @GetMapping("/stock/out")
    public ResponseEntity<List<Product>> getOutOfStock() {
        return ResponseEntity.status(200).body(productService.getOutOfStock());
    }

    @PutMapping("/{id}/buy")
    public ResponseEntity<String> buyProduct(@PathVariable int id,
                             @RequestParam int quantity) {
        productService.purchaseProduct(id, quantity);
        return ResponseEntity.status(200).body("Purchase successful");
    }

    @PutMapping("/{id}/restock")
    public ResponseEntity<String> restockProduct(@PathVariable int id,
                                 @RequestParam int quantity) {
        productService.restockProduct(id, quantity);
        return ResponseEntity.status(200).body("Restock successful");
    }

    // ---------------- SORTING ----------------

    @GetMapping("/sort/price/asc")
    public ResponseEntity<List<Product>> sortPriceAsc() {
        return ResponseEntity.status(200).body(productService.sortByPriceLowToHigh());
    }

    @GetMapping("/sort/price/desc")
    public ResponseEntity<List<Product>> sortPriceDesc() {
        return ResponseEntity.status(200).body(productService.sortByPriceHighToLow());
    }

    @GetMapping("/sort/rating")
    public ResponseEntity<List<Product>> sortByRating() {
        return ResponseEntity.status(200).body(productService.sortByRating());
    }

    @GetMapping("/sort/name")
    public ResponseEntity<List<Product>> sortByName() {
        return ResponseEntity.status(200).body(productService.sortByName());
    }

    // ---------------- ANALYTICS ----------------

    @GetMapping("/stats/average-price")
    public ResponseEntity<Double> averagePrice() {
        return ResponseEntity.status(200).body(productService.getAveragePrice());
    }

    @GetMapping("/stats/max-price")
    public ResponseEntity<Double> maxPrice() {
        return ResponseEntity.status(200).body(productService.getMaxPrice());
    }

    @GetMapping("/stats/min-price")
    public ResponseEntity<Double> minPrice() {
        return ResponseEntity.status(200).body(productService.getMinPrice());
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> totalProducts() {
        return ResponseEntity.status(200).body(productService.totalProducts());
    }

    @GetMapping("/stats/inventory-value")
    public ResponseEntity<Double> inventoryValue() {
        return ResponseEntity.status(200).body(productService.totalInventoryValue());
    }

    @GetMapping("/stats/category-count")
    public ResponseEntity<Map<String, Long>> categoryCount() {
        return ResponseEntity.status(200).body(productService.countByCategory());
    }

    @GetMapping("/stats/brand-count")
    public ResponseEntity<Map<String, Long>> brandCount() {
        return ResponseEntity.status(200).body(productService.countByBrand());
    }

    // ---------------- ADVANCED APIs ----------------

    @GetMapping("/top-rated")
    public ResponseEntity<List<Product>> topRated(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.status(200).body(productService.getTopRatedProducts(limit));
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<Product>> discounted(@RequestParam double minRating) {
        return ResponseEntity.status(200).body(productService.getDiscountedProducts(minRating));
    }
}