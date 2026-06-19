package org.example.jpademo.service;

import org.example.jpademo.model.Book;
import org.example.jpademo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Override
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getByTitle(String title) {
        return bookRepository.getByTitle(title);
    }

    @Override
    public List<Book> getByAuthor(String author) {
        return bookRepository.getByAuthor(author);
    }

    @Override
    public List<Book> getByPublisher(String publisher) {
        return bookRepository.getByPublisher(publisher);
    }

    @Override
    public List<Book> getBooksAbovePrice(double price) {
        return bookRepository.getBooksAbovePrice(price);
    }

    @Override
    public List<Book> getBooksBelowPrice(double price) {
        return bookRepository.getBooksBelowPrice(price);
    }

    @Override
    public List<Book> getBooksInPriceRange(double minPrice, double maxPrice) {
        return bookRepository.getBooksInPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Book> sortByTitle() {
        return bookRepository.sortByTitle();
    }

    @Override
    public List<Book> sortByAuthor() {
        return bookRepository.sortByAuthor();
    }

    @Override
    public List<Book> sortByPrice() {
        return bookRepository.sortByPrice();
    }

    @Override
    public List<Book> sortByPriceDesc() {
        return bookRepository.sortByPriceDesc();
    }

    @Override
    public double getAveragePrice() {
        return bookRepository.getAveragePrice();
    }

    @Override
    public double getMaxPrice() {
        return bookRepository.getMaxPrice();
    }

    @Override
    public double getMinPrice() {
        return bookRepository.getMinPrice();
    }

    @Override
    public long getBookCount() {
        return bookRepository.getBookCount();
    }

    @Override
    public Map<String, Long> countBooksByAuthor() {
        return bookRepository.countBooksByAuthor();
    }

    @Override
    public Map<String, Long> countBooksByPublisher() {
        return bookRepository.countBooksByPublisher();
    }
}