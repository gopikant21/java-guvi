package com.example.restapidemo.service;

import com.example.restapidemo.dao.ProductDAO;
import com.example.restapidemo.exception.BadRequestException;
import com.example.restapidemo.exception.InsufficientStockException;
import com.example.restapidemo.exception.ResourceNotFoundException;
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
        validatePositiveId(id, "Product ID");
        return getProductOrThrow(id);
    }

    @Override
    public List<Product> getAll() {
        return productDAO.getAll();
    }

    @Override
    public void update(int id, Product product) {
        validatePositiveId(id, "Product ID");
        getProductOrThrow(id);
        product.setInStock(product.getStockQuantity() > 0);
        productDAO.update(id, product);
    }

    @Override
    public void delete(int id) {
        validatePositiveId(id, "Product ID");
        getProductOrThrow(id);
        productDAO.delete(id);
    }

    // ---------------- SEARCH ----------------

    @Override
    public Product getByName(String name) {
        validateText(name, "Name");
        Product product = productDAO.getByName(name);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with name: " + name);
        }
        return product;
    }

    @Override
    public List<Product> search(String keyword) {
        validateText(keyword, "Keyword");
        return productDAO.searchByKeyword(keyword);
    }

    // ---------------- CATEGORY / BRAND ----------------

    @Override
    public List<Product> getByCategory(String category) {
        validateText(category, "Category");
        return productDAO.getByCategory(category);
    }

    @Override
    public List<Product> getByBrand(String brand) {
        validateText(brand, "Brand");
        return productDAO.getByBrand(brand);
    }

    // ---------------- PRICE ----------------

    @Override
    public List<Product> getExpensiveProducts(double price) {
        validateNonNegative(price, "Price");
        return productDAO.getAbovePrice(price);
    }

    @Override
    public List<Product> getCheapProducts(double price) {
        validateNonNegative(price, "Price");
        return productDAO.getBelowPrice(price);
    }

    @Override
    public List<Product> getByPriceRange(double min, double max) {
        validateNonNegative(min, "Min price");
        validateNonNegative(max, "Max price");
        if (min > max) {
            throw new BadRequestException("Min price cannot be greater than max price");
        }
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
        validatePositiveId(id, "Product ID");
        validatePositiveNumber(quantity, "Quantity");
        Product product = getProductOrThrow(id);

        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Not enough stock available. Current stock: " + product.getStockQuantity());
        }

        productDAO.reduceStock(id, quantity);
    }

    @Override
    public void restockProduct(int id, int quantity) {
        validatePositiveId(id, "Product ID");
        validatePositiveNumber(quantity, "Quantity");
        Product product = getProductOrThrow(id);

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
        validatePositiveNumber(limit, "Limit");
        return productDAO.getAll()
                .stream()
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getDiscountedProducts(double minRating) {
        if (minRating < 0 || minRating > 5) {
            throw new BadRequestException("Min rating must be between 0 and 5");
        }
        return productDAO.getAll()
                .stream()
                .filter(p -> p.getRating() >= minRating && p.getPrice() > 5000)
                .collect(Collectors.toList());
    }

    private Product getProductOrThrow(int id) {
        Product product = productDAO.getById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return product;
    }

    private void validatePositiveId(int id, String fieldName) {
        if (id <= 0) {
            throw new BadRequestException(fieldName + " must be greater than 0");
        }
    }

    private void validatePositiveNumber(int value, String fieldName) {
        if (value <= 0) {
            throw new BadRequestException(fieldName + " must be greater than 0");
        }
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " must not be blank");
        }
    }

    private void validateNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new BadRequestException(fieldName + " cannot be negative");
        }
    }
}