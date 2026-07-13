package com.example.restapidemo.service;

import com.example.restapidemo.dao.BookDAO;
import com.example.restapidemo.exception.BadRequestException;
import com.example.restapidemo.exception.ResourceNotFoundException;
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
        validatePositiveId(id, "Book ID");
        Book book = bookDAO.getById(id);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        return book;
    }

    @Override
    public List<Book> getAll() {
        return bookDAO.getAll();
    }

    @Override
    public void update(int id, Book book) {
        validatePositiveId(id, "Book ID");
        if (bookDAO.getById(id) == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookDAO.update(id, book);
    }

    @Override
    public void delete(int id) {
        validatePositiveId(id, "Book ID");
        if (bookDAO.getById(id) == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookDAO.delete(id);
    }

    @Override
    public Book getByTitle(String title) {
        validateText(title, "Title");
        Book book = bookDAO.getByTitle(title);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with title: " + title);
        }
        return book;
    }

    @Override
    public List<Book> getByAuthor(String author) {
        validateText(author, "Author");
        return bookDAO.getByAuthor(author);
    }

    @Override
    public List<Book> getByPublisher(String publisher) {
        validateText(publisher, "Publisher");
        return bookDAO.getByPublisher(publisher);
    }

    @Override
    public List<Book> getBooksAbovePrice(double price) {
        validateNonNegative(price, "Price");
        return bookDAO.getBooksAbovePrice(price);
    }

    @Override
    public List<Book> getBooksBelowPrice(double price) {
        validateNonNegative(price, "Price");
        return bookDAO.getBooksBelowPrice(price);
    }

    @Override
    public List<Book> getBooksInPriceRange(double minPrice, double maxPrice) {
        validateNonNegative(minPrice, "Min price");
        validateNonNegative(maxPrice, "Max price");
        if (minPrice > maxPrice) {
            throw new BadRequestException("Min price cannot be greater than max price");
        }
        return bookDAO.getBooksInPriceRange(minPrice, maxPrice);
    }

    private void validatePositiveId(int id, String fieldName) {
        if (id <= 0) {
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