package com.example.restapidemo.dao;

import com.example.restapidemo.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductDAOImpl implements ProductDAO {

    private Map<Integer, Product> products;

    @PostConstruct
    public void init() {
        products = new HashMap<>();
        products.put(1, new Product(
                1,
                "iPhone 15",
                "Latest Apple smartphone with A16 chip",
                79999,
                "Electronics",
                "Apple",
                10,
                true,
                4.8
        ));

        products.put(2, new Product(
                2,
                "Samsung Galaxy S24",
                "Flagship Android phone with AI features",
                74999,
                "Electronics",
                "Samsung",
                15,
                true,
                4.7
        ));

        products.put(3, new Product(
                3,
                "Dell XPS 13",
                "Premium ultrabook laptop",
                119999,
                "Laptops",
                "Dell",
                5,
                true,
                4.6
        ));

        products.put(4, new Product(
                4,
                "HP Pavilion",
                "Budget friendly laptop for students",
                55999,
                "Laptops",
                "HP",
                8,
                true,
                4.3
        ));

        products.put(5, new Product(
                5,
                "Sony WH-1000XM5",
                "Noise cancelling wireless headphones",
                29999,
                "Audio",
                "Sony",
                20,
                true,
                4.9
        ));

        products.put(6, new Product(
                6,
                "Boat Airdopes 141",
                "Affordable wireless earbuds",
                1499,
                "Audio",
                "Boat",
                50,
                true,
                4.2
        ));

        products.put(7, new Product(
                7,
                "Nike Air Max",
                "Comfortable running shoes",
                8999,
                "Footwear",
                "Nike",
                30,
                true,
                4.5
        ));

        products.put(8, new Product(
                8,
                "Adidas Running Shoes",
                "Lightweight sports shoes",
                7499,
                "Footwear",
                "Adidas",
                25,
                true,
                4.4
        ));

        products.put(9, new Product(
                9,
                "Samsung 55-inch TV",
                "4K Ultra HD Smart TV",
                54999,
                "Electronics",
                "Samsung",
                7,
                true,
                4.6
        ));

        products.put(10, new Product(
                10,
                "Apple Watch Series 9",
                "Smartwatch with health tracking",
                41999,
                "Wearables",
                "Apple",
                12,
                true,
                4.8
        ));
    }



    // ---------------- CRUD ----------------

    @Override
    public void save(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        products.put(product.getId(), product);
    }

    @Override
    public Product getById(int id) {
        return products.get(id);
    }

    @Override
    public List<Product> getAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void update(int id, Product product) {
        if (products.containsKey(id)) {
            product.setId(id);
            product.setUpdatedAt(LocalDateTime.now());
            products.put(id, product);
        }
    }

    @Override
    public void delete(int id) {
        products.remove(id);
    }

    // ---------------- SEARCH ----------------

    @Override
    public Product getByName(String name) {
        return products.values()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        return products.values()
                .stream()
                .filter(p ->
                        p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ---------------- CATEGORY / BRAND ----------------

    @Override
    public List<Product> getByCategory(String category) {
        return products.values()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return products.values()
                .stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    // ---------------- PRICE FILTERS ----------------

    @Override
    public List<Product> getAbovePrice(double price) {
        return products.values()
                .stream()
                .filter(p -> p.getPrice() > price)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getBelowPrice(double price) {
        return products.values()
                .stream()
                .filter(p -> p.getPrice() < price)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getByPriceRange(double min, double max) {
        return products.values()
                .stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
    }

    // ---------------- STOCK ----------------

    @Override
    public List<Product> getInStockProducts() {
        return products.values()
                .stream()
                .filter(Product::isInStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getOutOfStockProducts() {
        return products.values()
                .stream()
                .filter(p -> !p.isInStock())
                .collect(Collectors.toList());
    }

    @Override
    public void updateStock(int id, int quantity) {
        Product p = products.get(id);
        if (p != null) {
            p.setStockQuantity(quantity);
            p.setInStock(quantity > 0);
        }
    }

    @Override
    public void reduceStock(int id, int quantity) {
        Product p = products.get(id);
        if (p != null) {
            int newQty = p.getStockQuantity() - quantity;
            p.setStockQuantity(newQty);
            p.setInStock(newQty > 0);
        }
    }

    // ---------------- SORTING ----------------

    @Override
    public List<Product> sortByPriceAsc() {
        return products.values()
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByPriceDesc() {
        return products.values()
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByRating() {
        return products.values()
                .stream()
                .sorted(Comparator.comparingDouble(Product::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByName() {
        return products.values()
                .stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
    }

    // ---------------- ANALYTICS ----------------

    @Override
    public double getAveragePrice() {
        return products.values()
                .stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0);
    }

    @Override
    public double getMaxPrice() {
        return products.values()
                .stream()
                .mapToDouble(Product::getPrice)
                .max()
                .orElse(0);
    }

    @Override
    public double getMinPrice() {
        return products.values()
                .stream()
                .mapToDouble(Product::getPrice)
                .min()
                .orElse(0);
    }

    @Override
    public long totalProducts() {
        return products.size();
    }

    @Override
    public Map<String, Long> countByCategory() {
        return products.values()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()));
    }

    @Override
    public Map<String, Long> countByBrand() {
        return products.values()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getBrand,
                        Collectors.counting()));
    }

    @Override
    public double totalInventoryValue() {
        return products.values()
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
    }

    @PreDestroy
    public void destroy() {
        products.clear();
    }
}