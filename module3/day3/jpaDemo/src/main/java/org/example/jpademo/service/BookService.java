package org.example.jpademo.service;

import org.example.jpademo.model.Book;

import java.util.List;
import java.util.Map;

public interface BookService {

    void save(Book book);

    Book getById(int id);

    List<Book> getAll();

    void update(int id, Book book);

    void delete(int id);

    Book getByTitle(String title);

    List<Book> getByAuthor(String author);

    List<Book> getByPublisher(String publisher);

    List<Book> getBooksAbovePrice(double price);

    List<Book> getBooksBelowPrice(double price);

    List<Book> getBooksInPriceRange(double minPrice, double maxPrice);

    List<Book> sortByTitle();

    List<Book> sortByAuthor();

    List<Book> sortByPrice();

    List<Book> sortByPriceDesc();

    double getAveragePrice();

    double getMaxPrice();

    double getMinPrice();

    long getBookCount();

    Map<String, Long> countBooksByAuthor();

    Map<String, Long> countBooksByPublisher();
}