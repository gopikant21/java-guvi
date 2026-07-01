package org.example.libraryjwt.service;

import org.example.libraryjwt.dto.MemberSummaryDTO;
import org.example.libraryjwt.entity.Book;
import org.example.libraryjwt.entity.FineTransaction;
import org.example.libraryjwt.entity.IssueRecord;
import org.example.libraryjwt.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Library service interface for business logic
 */
public interface LibraryService {

    // Member management
    Member addMember(Member member);
    Member getMemberById(Long memberId);
    Member getMemberByEmail(String email);
    List<Member> getMembersByBranch(String branch);
    MemberSummaryDTO getMemberSummary(Long memberId);

    // Book management
    Book addBook(Book book);
    Book getBookByIsbn(String isbn);
    List<Book> getBooksByType(String bookType);
    List<Book> getBooksWithHigherFineRate(double amount);
    List<Book> getBooksWithNoFineHistory();
    Page<Book> getBooksPaginated(Pageable pageable);

    // Issue record management
    IssueRecord issueBook(Long memberId, String isbn);
    IssueRecord returnBook(Long issueId);
    List<IssueRecord> getMemberIssueHistory(Long memberId);

    // Fine transaction management
    FineTransaction recordFinePayment(Long memberId, String isbn, Double amount, String paymentType);
    List<FineTransaction> getFineTransactionsByPaymentType(String paymentType);

    // Analytics and reporting
    List<Member> findAvidReaders(long targetCount);
    List<Object[]> getTotalFinesPaidPerBranch();
    List<Member> findMembersWithMultiGenreBooks(long genreCount);
    FineTransaction getLatestFinePayment();
    Map<String, Object> getDashboardAnalytics();

    // Batch operations
    int increaseDailyFineRates(String bookType);
}

