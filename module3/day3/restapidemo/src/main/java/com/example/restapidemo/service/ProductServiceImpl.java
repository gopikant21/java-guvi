package com.example.restapidemo.service;

import com.example.restapidemo.dao.ProductDAO;
import com.example.restapidemo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    // ---------------- CRUD ----------------

    @Override
    public void save(Product product) {
        product.setInStock(product.getStockQuantity() > 0);
        productDAO.save(product);
    }

    @Override
    public Product getById(int id) {
        return productDAO.getById(id);
    }

    @Override
    public List<Product> getAll() {
        return productDAO.getAll();
    }

    @Override
    public void update(int id, Product product) {
        productDAO.update(id, product);
    }

    @Override
    public void delete(int id) {
        productDAO.delete(id);
    }

    // ---------------- SEARCH ----------------

    @Override
    public Product getByName(String name) {
        return productDAO.getByName(name);
    }

    @Override
    public List<Product> search(String keyword) {
        return productDAO.searchByKeyword(keyword);
    }

    // ---------------- CATEGORY / BRAND ----------------

    @Override
    public List<Product> getByCategory(String category) {
        return productDAO.getByCategory(category);
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return productDAO.getByBrand(brand);
    }

    // ---------------- PRICE ----------------

    @Override
    public List<Product> getExpensiveProducts(double price) {
        return productDAO.getAbovePrice(price);
    }

    @Override
    public List<Product> getCheapProducts(double price) {
        return productDAO.getBelowPrice(price);
    }

    @Override
    public List<Product> getByPriceRange(double min, double max) {
        return productDAO.getByPriceRange(min, max);
    }

    // ---------------- STOCK BUSINESS LOGIC ----------------

    @Override
    public List<Product> getInStock() {
        return productDAO.getInStockProducts();
    }

    @Override
    public List<Product> getOutOfStock() {
        return productDAO.getOutOfStockProducts();
    }

    @Override
    public void purchaseProduct(int id, int quantity) {
        Product product = productDAO.getById(id);

        if (product == null) return;

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        productDAO.reduceStock(id, quantity);
    }

    @Override
    public void restockProduct(int id, int quantity) {
        Product product = productDAO.getById(id);

        if (product == null) return;

        productDAO.updateStock(id, product.getStockQuantity() + quantity);
    }

    // ---------------- SORTING ----------------

    @Override
    public List<Product> sortByPriceLowToHigh() {
        return productDAO.sortByPriceAsc();
    }

    @Override
    public List<Product> sortByPriceHighToLow() {
        return productDAO.sortByPriceDesc();
    }

    @Override
    public List<Product> sortByRating() {
        return productDAO.sortByRating();
    }

    @Override
    public List<Product> sortByName() {
        return productDAO.sortByName();
    }

    // ---------------- ANALYTICS ----------------

    @Override
    public double getAveragePrice() {
        return productDAO.getAveragePrice();
    }

    @Override
    public double getMaxPrice() {
        return productDAO.getMaxPrice();
    }

    @Override
    public double getMinPrice() {
        return productDAO.getMinPrice();
    }

    @Override
    public long totalProducts() {
        return productDAO.totalProducts();
    }

    @Override
    public double totalInventoryValue() {
        return productDAO.totalInventoryValue();
    }

    @Override
    public Map<String, Long> countByCategory() {
        return productDAO.countByCategory();
    }

    @Override
    public Map<String, Long> countByBrand() {
        return productDAO.countByBrand();
    }

    // ---------------- ADVANCED FEATURES ----------------

    @Override
    public List<Product> getTopRatedProducts(int limit) {
        return productDAO.getAll()
                .stream()
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getDiscountedProducts(double minRating) {
        return productDAO.getAll()
                .stream()
                .filter(p -> p.getRating() >= minRating && p.getPrice() > 5000)
                .collect(Collectors.toList());
    }
}