package org.example.libraryjwt.repository;

import org.example.libraryjwt.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for Book entity.
 * Task 3: Spring Data JPA Derived Queries
 * Task 4: JPQL Queries
 * Task 5: JPQL Update Query
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    /**
     * Task 3: Derived query - Find books by book type (genre)
     */
    List<Book> findByBookType(String bookType);

    /**
     * Task 3: Derived query - Find books by daily fine rate greater than specified amount
     */
    List<Book> findByDailyFineRateGreaterThan(double amount);

    /**
     * Task 4: JPQL Query - Find Books with No Overdue History
     * Retrieve all books that have zero fine transaction entries using LEFT JOIN
     */
    @Query("SELECT b FROM Book b LEFT JOIN b.fineTransactions ft " +
           "WHERE ft.transactionId IS NULL")
    List<Book> findBooksWithNoFineHistory();

    /**
     * Task 5: JPQL Update Query - Increase Daily Fine Rates
     * Update daily fine rates for specific book types using @Modifying
     */
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.dailyFineRate = b.dailyFineRate * 1.1 " +
           "WHERE b.bookType = :bookType")
    int increaseDailyFineRates(@Param("bookType") String bookType);

    /**
     * Task 6: Support for Pagination & Sorting
     * Get books with pagination support (sorted by daily fine rate in desc order by default)
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Get total book count for analytics
     */
    @Query("SELECT COUNT(b) FROM Book b")
    long getTotalBooks();
}

