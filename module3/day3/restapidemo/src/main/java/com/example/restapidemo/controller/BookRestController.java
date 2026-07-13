package com.example.restapidemo.controller;

import com.example.restapidemo.model.Book;
import com.example.restapidemo.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookRestController {

    @Autowired
    private BookService bookService;

    // CREATE
    @PostMapping
    public ResponseEntity<String> addBook(@Valid @RequestBody Book book) {
        bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully");
    }

    // READ
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable @Positive int id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.getByTitle(title));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @PathVariable String author) {
        return ResponseEntity.ok(bookService.getByAuthor(author));
    }

    @GetMapping("/publisher/{publisher}")
    public ResponseEntity<List<Book>> getBooksByPublisher(
            @PathVariable String publisher) {
        return ResponseEntity.ok(bookService.getByPublisher(publisher));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(
            @PathVariable @Positive int id,
            @Valid @RequestBody Book book) {

        bookService.update(id, book);
        return ResponseEntity.ok("Book updated successfully");
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable @Positive int id) {
        bookService.delete(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    // PRICE FILTERS
    @GetMapping("/price/above/{price}")
    public ResponseEntity<List<Book>> getBooksAbovePrice(
            @PathVariable @PositiveOrZero double price) {
        return ResponseEntity.ok(bookService.getBooksAbovePrice(price));
    }

    @GetMapping("/price/below/{price}")
    public ResponseEntity<List<Book>> getBooksBelowPrice(
            @PathVariable @PositiveOrZero double price) {
        return ResponseEntity.ok(bookService.getBooksBelowPrice(price));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Book>> getBooksInRange(
            @RequestParam @PositiveOrZero double min,
            @RequestParam @PositiveOrZero double max) {
        return ResponseEntity.ok(bookService.getBooksInPriceRange(min, max));
    }

    // SORTING
    @GetMapping("/sort/title")
    public ResponseEntity<List<Book>> sortByTitle() {
        return ResponseEntity.ok(bookService.sortByTitle());
    }

    @GetMapping("/sort/author")
    public ResponseEntity<List<Book>> sortByAuthor() {
        return ResponseEntity.ok(bookService.sortByAuthor());
    }

    @GetMapping("/sort/price")
    public ResponseEntity<List<Book>> sortByPrice() {
        return ResponseEntity.ok(bookService.sortByPrice());
    }

    @GetMapping("/sort/price-desc")
    public ResponseEntity<List<Book>> sortByPriceDesc() {
        return ResponseEntity.ok(bookService.sortByPriceDesc());
    }

    // STATISTICS
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getBookCount() {
        return ResponseEntity.ok(bookService.getBookCount());
    }

    @GetMapping("/stats/average-price")
    public ResponseEntity<Double> getAveragePrice() {
        return ResponseEntity.ok(bookService.getAveragePrice());
    }

    @GetMapping("/stats/max-price")
    public ResponseEntity<Double> getMaxPrice() {
        return ResponseEntity.ok(bookService.getMaxPrice());
    }

    @GetMapping("/stats/min-price")
    public ResponseEntity<Double> getMinPrice() {
        return ResponseEntity.ok(bookService.getMinPrice());
    }

    @GetMapping("/stats/author-count")
    public ResponseEntity<Map<String, Long>> authorCount() {
        return ResponseEntity.ok(bookService.countBooksByAuthor());
    }

    @GetMapping("/stats/publisher-count")
    public ResponseEntity<Map<String, Long>> publisherCount() {
        return ResponseEntity.ok(bookService.countBooksByPublisher());
    }
}