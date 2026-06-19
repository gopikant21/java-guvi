package com.example.restapidemo.dao;

import com.example.restapidemo.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookDAOImpl implements BookDAO {

    private Map<Integer, Book> books;

    @PostConstruct
    public void init() {
        books = new HashMap<>();
        books.put(1, new Book(1, "Clean Code",
                "Robert Martin", "Prentice Hall", 599));

        books.put(2, new Book(2, "Effective Java",
                "Joshua Bloch", "Addison Wesley", 799));

        books.put(3, new Book(3, "Spring in Action",
                "Craig Walls", "Manning", 899));

        books.put(4, new Book(4, "Java Concurrency",
                "Brian Goetz", "Addison Wesley", 999));

        books.put(5, new Book(5, "Head First Java",
                "Kathy Sierra", "O'Reilly", 699));
    }

    @Override
    public void save(Book book) {
        books.put(book.getId(), book);
    }

    @Override
    public Book getById(int id) {
        return books.get(id);
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public void update(int id, Book book) {
        book.setId(id);
        books.put(id, book);
    }

    @Override
    public void delete(int id) {
        books.remove(id);
    }

    @Override
    public Book getByTitle(String title) {
        return books.values()
                .stream()
                .filter(book ->
                        book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Book> getByAuthor(String author) {
        return books.values()
                .stream()
                .filter(book ->
                        book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getByPublisher(String publisher) {
        return books.values()
                .stream()
                .filter(book ->
                        book.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksAbovePrice(double price) {
        return books.values()
                .stream()
                .filter(book -> book.getPrice() > price)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksBelowPrice(double price) {
        return books.values()
                .stream()
                .filter(book -> book.getPrice() < price)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksInPriceRange(double minPrice,
                                           double maxPrice) {

        return books.values()
                .stream()
                .filter(book ->
                        book.getPrice() >= minPrice &&
                                book.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortByTitle() {
        return books.values()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortByAuthor() {
        return books.values()
                .stream()
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortByPrice() {
        return books.values()
                .stream()
                .sorted(Comparator.comparingDouble(Book::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortByPriceDesc() {
        return books.values()
                .stream()
                .sorted(Comparator.comparingDouble(Book::getPrice)
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public double getAveragePrice() {
        return books.values()
                .stream()
                .mapToDouble(Book::getPrice)
                .average()
                .orElse(0);
    }

    @Override
    public double getMaxPrice() {
        return books.values()
                .stream()
                .mapToDouble(Book::getPrice)
                .max()
                .orElse(0);
    }

    @Override
    public double getMinPrice() {
        return books.values()
                .stream()
                .mapToDouble(Book::getPrice)
                .min()
                .orElse(0);
    }

    @Override
    public long getBookCount() {
        return books.size();
    }

    @Override
    public Map<String, Long> countBooksByAuthor() {
        return books.values()
                .stream()
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.counting()));
    }

    @Override
    public Map<String, Long> countBooksByPublisher() {
        return books.values()
                .stream()
                .collect(Collectors.groupingBy(
                        Book::getPublisher,
                        Collectors.counting()));
    }

    @PreDestroy
    public void destroy() {
        books.clear();
    }
}