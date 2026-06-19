package org.example.jpademo.service;

import org.example.jpademo.repository.ProductRepository;
import org.example.jpademo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ---------------- CRUD ----------------

    @Override
    public void save(Product product) {
        product.setInStock(product.getStockQuantity() > 0);
        productRepository.save(product);
    }

    @Override
    public Product getById(int id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    @Override
    public void update(int id, Product product) {
        productRepository.update(id, product);
    }

    @Override
    public void delete(int id) {
        productRepository.delete(id);
    }

    // ---------------- SEARCH ----------------

    @Override
    public Product getByName(String name) {
        return productRepository.getByName(name);
    }

    @Override
    public List<Product> search(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    // ---------------- CATEGORY / BRAND ----------------

    @Override
    public List<Product> getByCategory(String category) {
        return productRepository.getByCategory(category);
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return productRepository.getByBrand(brand);
    }

    // ---------------- PRICE ----------------

    @Override
    public List<Product> getExpensiveProducts(double price) {
        return productRepository.getAbovePrice(price);
    }

    @Override
    public List<Product> getCheapProducts(double price) {
        return productRepository.getBelowPrice(price);
    }

    @Override
    public List<Product> getByPriceRange(double min, double max) {
        return productRepository.getByPriceRange(min, max);
    }

    // ---------------- STOCK BUSINESS LOGIC ----------------

    @Override
    public List<Product> getInStock() {
        return productRepository.getInStockProducts();
    }

    @Override
    public List<Product> getOutOfStock() {
        return productRepository.getOutOfStockProducts();
    }

    @Override
    public void purchaseProduct(int id, int quantity) {
        Product product = productRepository.getById(id);

        if (product == null) return;

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        productRepository.reduceStock(id, quantity);
    }

    @Override
    public void restockProduct(int id, int quantity) {
        Product product = productRepository.getById(id);

        if (product == null) return;

        productRepository.updateStock(id, product.getStockQuantity() + quantity);
    }

    // ---------------- SORTING ----------------

    @Override
    public List<Product> sortByPriceLowToHigh() {
        return productRepository.sortByPriceAsc();
    }

    @Override
    public List<Product> sortByPriceHighToLow() {
        return productRepository.sortByPriceDesc();
    }

    @Override
    public List<Product> sortByRating() {
        return productRepository.sortByRating();
    }

    @Override
    public List<Product> sortByName() {
        return productRepository.sortByName();
    }

    // ---------------- ANALYTICS ----------------

    @Override
    public double getAveragePrice() {
        return productRepository.getAveragePrice();
    }

    @Override
    public double getMaxPrice() {
        return productRepository.getMaxPrice();
    }

    @Override
    public double getMinPrice() {
        return productRepository.getMinPrice();
    }

    @Override
    public long totalProducts() {
        return productRepository.totalProducts();
    }

    @Override
    public double totalInventoryValue() {
        return productRepository.totalInventoryValue();
    }

    @Override
    public Map<String, Long> countByCategory() {
        return productRepository.countByCategory();
    }

    @Override
    public Map<String, Long> countByBrand() {
        return productRepository.countByBrand();
    }

    // ---------------- ADVANCED FEATURES ----------------

    @Override
    public List<Product> getTopRatedProducts(int limit) {
        return productRepository.getAll()
                .stream()
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getDiscountedProducts(double minRating) {
        return productRepository.getAll()
                .stream()
                .filter(p -> p.getRating() >= minRating && p.getPrice() > 5000)
                .collect(Collectors.toList());
    }
}