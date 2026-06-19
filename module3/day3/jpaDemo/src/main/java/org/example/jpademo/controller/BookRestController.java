package org.example.jpademo.controller;

import org.example.jpademo.model.Book;
import org.example.jpademo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    // CREATE
    @PostMapping
    public String addBook(@RequestBody Book book) {
        bookService.save(book);
        return "Book Added Successfully";
    }

    // READ
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookService.getById(id);
    }

    @GetMapping("/title/{title}")
    public Book getBookByTitle(@PathVariable String title) {
        return bookService.getByTitle(title);
    }

    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(
            @PathVariable String author) {
        return bookService.getByAuthor(author);
    }

    @GetMapping("/publisher/{publisher}")
    public List<Book> getBooksByPublisher(
            @PathVariable String publisher) {
        return bookService.getByPublisher(publisher);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateBook(
            @PathVariable int id,
            @RequestBody Book book) {

        bookService.update(id, book);
        return "Book Updated Successfully";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.delete(id);
        return "Book Deleted Successfully";
    }

    // PRICE FILTERS
    @GetMapping("/price/above/{price}")
    public List<Book> getBooksAbovePrice(
            @PathVariable double price) {
        return bookService.getBooksAbovePrice(price);
    }

    @GetMapping("/price/below/{price}")
    public List<Book> getBooksBelowPrice(
            @PathVariable double price) {
        return bookService.getBooksBelowPrice(price);
    }

    @GetMapping("/price-range")
    public List<Book> getBooksInRange(
            @RequestParam double min,
            @RequestParam double max) {
        return bookService.getBooksInPriceRange(min, max);
    }

    // SORTING
    @GetMapping("/sort/title")
    public List<Book> sortByTitle() {
        return bookService.sortByTitle();
    }

    @GetMapping("/sort/author")
    public List<Book> sortByAuthor() {
        return bookService.sortByAuthor();
    }

    @GetMapping("/sort/price")
    public List<Book> sortByPrice() {
        return bookService.sortByPrice();
    }

    @GetMapping("/sort/price-desc")
    public List<Book> sortByPriceDesc() {
        return bookService.sortByPriceDesc();
    }

    // STATISTICS
    @GetMapping("/stats/count")
    public long getBookCount() {
        return bookService.getBookCount();
    }

    @GetMapping("/stats/average-price")
    public double getAveragePrice() {
        return bookService.getAveragePrice();
    }

    @GetMapping("/stats/max-price")
    public double getMaxPrice() {
        return bookService.getMaxPrice();
    }

    @GetMapping("/stats/min-price")
    public double getMinPrice() {
        return bookService.getMinPrice();
    }

    @GetMapping("/stats/author-count")
    public Map<String, Long> authorCount() {
        return bookService.countBooksByAuthor();
    }

    @GetMapping("/stats/publisher-count")
    public Map<String, Long> publisherCount() {
        return bookService.countBooksByPublisher();
    }
}