package com.example.restapidemo.model;

import java.time.LocalDateTime;

public class Product {

    private int id;
    private String name;
    private String description;

    // Pricing
    private double price;
    private double discountPercentage;
    private double taxPercentage;

    // Category & brand
    private String category;
    private String brand;

    // Inventory
    private int stockQuantity;
    private boolean inStock;

    // Ratings & reviews
    private double rating;
    private int reviewCount;

    // Tracking
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Status
    private boolean active;
    private String sku; // Stock Keeping Unit

    public Product() {}

    public Product(String name, String description, double price, double discountPercentage, double taxPercentage, String category, String brand, int stockQuantity, boolean inStock, double rating, int reviewCount, LocalDateTime createdAt, LocalDateTime updatedAt, boolean active, String sku){
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.taxPercentage = taxPercentage;
        this.category = category;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.sku = sku;
    }

    public Product(int id, String name, String description, double price, double discountPercentage, double taxPercentage, String category, String brand, int stockQuantity, boolean inStock, double rating, int reviewCount, LocalDateTime createdAt, LocalDateTime updatedAt, boolean active, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.taxPercentage = taxPercentage;
        this.category = category;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.sku = sku;
    }

    public Product(int id, String name, String description, double price,
                   String category, String brand, int stockQuantity,
                   boolean inStock, double rating){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discountPercentage=" + discountPercentage +
                ", taxPercentage=" + taxPercentage +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", inStock=" + inStock +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                ", sku='" + sku + '\'' +
                '}';
    }
}
