package org.example.jpademo.repository;

import org.example.jpademo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

public interface BookRepository extends JpaRepository<Book, Integer> {

    // Query methods
    Book getByTitle(String title);

    List<Book> getByAuthor(String author);

    List<Book> getByPublisher(String publisher);

    @Query("SELECT b FROM Book b WHERE b.price > :price")
    List<Book> getBooksAbovePrice(@Param("price") double price);

    @Query("SELECT b FROM Book b WHERE b.price < :price")
    List<Book> getBooksBelowPrice(@Param("price") double price);

    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> getBooksInPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query("SELECT b FROM Book b ORDER BY b.title ASC")
    List<Book> sortByTitle();

    @Query("SELECT b FROM Book b ORDER BY b.author ASC")
    List<Book> sortByAuthor();

    @Query("SELECT b FROM Book b ORDER BY b.price ASC")
    List<Book> sortByPrice();

    @Query("SELECT b FROM Book b ORDER BY b.price DESC")
    List<Book> sortByPriceDesc();

    @Query("SELECT AVG(b.price) FROM Book b")
    double getAveragePrice();

    @Query("SELECT MAX(b.price) FROM Book b")
    double getMaxPrice();

    @Query("SELECT MIN(b.price) FROM Book b")
    double getMinPrice();

    @Query("SELECT COUNT(b) FROM Book b")
    long getBookCount();

    @Query("SELECT b.author, COUNT(b) FROM Book b GROUP BY b.author")
    Map<String, Long> countBooksByAuthor();

    @Query("SELECT b.publisher, COUNT(b) FROM Book b GROUP BY b.publisher")
    Map<String, Long> countBooksByPublisher();
}