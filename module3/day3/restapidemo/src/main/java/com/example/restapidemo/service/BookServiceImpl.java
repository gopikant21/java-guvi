package com.example.restapidemo.service;

import com.example.restapidemo.dao.BookDAO;
import com.example.restapidemo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Override
    public void save(Book book) {
        bookDAO.save(book);
    }

    @Override
    public Book getById(int id) {
        return bookDAO.getById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDAO.getAll();
    }

    @Override
    public void update(int id, Book book) {
        bookDAO.update(id, book);
    }

    @Override
    public void delete(int id) {
        bookDAO.delete(id);
    }

    @Override
    public Book getByTitle(String title) {
        return bookDAO.getByTitle(title);
    }

    @Override
    public List<Book> getByAuthor(String author) {
        return bookDAO.getByAuthor(author);
    }

    @Override
    public List<Book> getByPublisher(String publisher) {
        return bookDAO.getByPublisher(publisher);
    }

    @Override
    public List<Book> getBooksAbovePrice(double price) {
        return bookDAO.getBooksAbovePrice(price);
    }

    @Override
    public List<Book> getBooksBelowPrice(double price) {
        return bookDAO.getBooksBelowPrice(price);
    }

    @Override
    public List<Book> getBooksInPriceRange(double minPrice, double maxPrice) {
        return bookDAO.getBooksInPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Book> sortByTitle() {
        return bookDAO.sortByTitle();
    }

    @Override
    public List<Book> sortByAuthor() {
        return bookDAO.sortByAuthor();
    }

    @Override
    public List<Book> sortByPrice() {
        return bookDAO.sortByPrice();
    }

    @Override
    public List<Book> sortByPriceDesc() {
        return bookDAO.sortByPriceDesc();
    }

    @Override
    public double getAveragePrice() {
        return bookDAO.getAveragePrice();
    }

    @Override
    public double getMaxPrice() {
        return bookDAO.getMaxPrice();
    }

    @Override
    public double getMinPrice() {
        return bookDAO.getMinPrice();
    }

    @Override
    public long getBookCount() {
        return bookDAO.getBookCount();
    }

    @Override
    public Map<String, Long> countBooksByAuthor() {
        return bookDAO.countBooksByAuthor();
    }

    @Override
    public Map<String, Long> countBooksByPublisher() {
        return bookDAO.countBooksByPublisher();
    }
}