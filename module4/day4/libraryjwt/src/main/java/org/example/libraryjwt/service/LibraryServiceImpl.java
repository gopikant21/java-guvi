package org.example.libraryjwt.service;

import lombok.extern.slf4j.Slf4j;
import org.example.libraryjwt.dto.MemberSummaryDTO;
import org.example.libraryjwt.entity.Book;
import org.example.libraryjwt.entity.FineTransaction;
import org.example.libraryjwt.entity.IssueRecord;
import org.example.libraryjwt.entity.Member;
import org.example.libraryjwt.exception.BookNotFoundException;
import org.example.libraryjwt.exception.MemberNotFoundException;
import org.example.libraryjwt.repository.BookRepository;
import org.example.libraryjwt.repository.FineTransactionRepository;
import org.example.libraryjwt.repository.IssueRecordRepository;
import org.example.libraryjwt.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of LibraryService
 * Handles all business logic with optimized queries to prevent N+1 problems
 * Task 4: JPQL queries, Task 5: Batch updates, Task 7: DTO mapping
 * Final Challenge: Optimized analytics queries
 */
@Service
@Transactional
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private FineTransactionRepository fineTransactionRepository;

    // ==================== Member Management ====================

    @Override
    public Member addMember(Member member) {
        log.info("Adding new member: {}", member.getEmail());
        return memberRepository.save(member);
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with email: " + email));
    }

    @Override
    public List<Member> getMembersByBranch(String branch) {
        log.info("Fetching members from branch: {}", branch);
        return memberRepository.findByMembershipBranch(branch);
    }

    /**
     * Task 7: DTO Projection Mapping - Returns custom MemberSummaryDTO
     */
    @Override
    public MemberSummaryDTO getMemberSummary(Long memberId) {
        Member member = getMemberById(memberId);

        long numberOfBorrowedBooks = issueRecordRepository.countByMemberId(memberId);
        Double totalFinesPaid = fineTransactionRepository.getTotalFinesPaidByMember(memberId);

        return MemberSummaryDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .membershipBranch(member.getMembershipBranch())
                .numberOfBorrowedBooks(numberOfBorrowedBooks)
                .totalFinesPaid(totalFinesPaid != null ? totalFinesPaid : 0.0)
                .build();
    }

    // ==================== Book Management ====================

    @Override
    public Book addBook(Book book) {
        log.info("Adding new book: {} with ISBN: {}", book.getTitle(), book.getIsbn());
        return bookRepository.save(book);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    /**
     * Task 3: Derived query - Find books by type
     */
    @Override
    public List<Book> getBooksByType(String bookType) {
        log.info("Fetching books of type: {}", bookType);
        return bookRepository.findByBookType(bookType);
    }

    /**
     * Task 3: Derived query - Find books with higher fine rate
     */
    @Override
    public List<Book> getBooksWithHigherFineRate(double amount) {
        log.info("Fetching books with fine rate greater than: {}", amount);
        return bookRepository.findByDailyFineRateGreaterThan(amount);
    }

    /**
     * Task 4: JPQL Query - Find Books with No Overdue History
     */
    @Override
    public List<Book> getBooksWithNoFineHistory() {
        log.info("Fetching books with no fine transaction history");
        return bookRepository.findBooksWithNoFineHistory();
    }

    /**
     * Task 6: Pagination & Sorting - Get books with pagination support
     */
    @Override
    public Page<Book> getBooksPaginated(Pageable pageable) {
        log.info("Fetching paginated books with sort order: {}", pageable.getSort());
        return bookRepository.findAll(pageable);
    }

    // ==================== Issue Record Management ====================

    @Override
    public IssueRecord issueBook(Long memberId, String isbn) {
        Member member = getMemberById(memberId);
        Book book = getBookByIsbn(isbn);

        IssueRecord issueRecord = IssueRecord.builder()
                .member(member)
                .book(book)
                .issueDate(LocalDate.now())
                .status("ISSUED")
                .build();

        log.info("Issuing book {} to member {}", isbn, memberId);
        return issueRecordRepository.save(issueRecord);
    }

    @Override
    public IssueRecord returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue record not found with ID: " + issueId));

        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setStatus("RETURNED");

        log.info("Book returned for issue record: {}", issueId);
        return issueRecordRepository.save(issueRecord);
    }

    @Override
    public List<IssueRecord> getMemberIssueHistory(Long memberId) {
        getMemberById(memberId); // Validate member exists
        return issueRecordRepository.findByMemberMemberId(memberId);
    }

    // ==================== Fine Transaction Management ====================

    @Override
    public FineTransaction recordFinePayment(Long memberId, String isbn, Double amount, String paymentType) {
        Member member = getMemberById(memberId);
        Book book = getBookByIsbn(isbn);

        FineTransaction fineTransaction = FineTransaction.builder()
                .member(member)
                .book(book)
                .amount(amount)
                .paymentType(paymentType)
                .paymentDate(LocalDate.now())
                .build();

        log.info("Recording fine payment of {} for member {} on book {}", amount, memberId, isbn);
        return fineTransactionRepository.save(fineTransaction);
    }

    /**
     * Task 3: Derived query - Find fine transactions by payment type
     */
    @Override
    public List<FineTransaction> getFineTransactionsByPaymentType(String paymentType) {
        log.info("Fetching fine transactions by payment type: {}", paymentType);
        return fineTransactionRepository.findByPaymentType(paymentType);
    }

    // ==================== Analytics & Reporting ====================

    /**
     * Task 4: JPQL Query - Find Avid Readers
     * Return members who have checked out books more than target count
     */
    @Override
    public List<Member> findAvidReaders(long targetCount) {
        log.info("Finding avid readers with more than {} checkouts", targetCount);
        return memberRepository.findAvidReaders(targetCount);
    }

    /**
     * Task 4: JPQL Query - Find Total Fines Paid Per Branch
     * Using JOIN and GROUP BY with SUM aggregation
     */
    @Override
    public List<Object[]> getTotalFinesPaidPerBranch() {
        log.info("Calculating total fines paid per branch");
        return memberRepository.findTotalFinesPaidPerBranch();
    }

    /**
     * Task 4: JPQL Query - Find Members Holding Multi-Genre Book Types
     * Using COUNT(DISTINCT) and GROUP BY with HAVING
     */
    @Override
    public List<Member> findMembersWithMultiGenreBooks(long genreCount) {
        log.info("Finding members borrowing {} or more distinct book genres", genreCount);
        return memberRepository.findMembersWithMultiGenreBooks(genreCount);
    }

    /**
     * Task 4: JPQL Query - Find Latest Fine Payment
     */
    @Override
    public FineTransaction getLatestFinePayment() {
        log.info("Fetching latest fine payment record");
        Optional<FineTransaction> latest = fineTransactionRepository.findLatestFinePayment();
        return latest.orElse(null);
    }

    /**
     * Final Challenge: Dashboard Analytics
     * Returns optimized analytics in single execution using minimal JPQL queries
     * Constraint: Bypass N+1 query problem through optimized queries
     */
    @Override
    public Map<String, Object> getDashboardAnalytics() {
        log.info("Generating dashboard analytics");

        Map<String, Object> dashboard = new HashMap<>();

        // Query 1: Total members count
        long totalMembers = memberRepository.getTotalMembers();
        dashboard.put("totalMembers", totalMembers);

        // Query 2: Total books count
        long totalBooks = bookRepository.getTotalBooks();
        dashboard.put("totalBooks", totalBooks);

        // Query 3: Total fines collected
        Double totalFinesCollected = fineTransactionRepository.getTotalFinesCollected();
        dashboard.put("totalFinesCollected", totalFinesCollected != null ? totalFinesCollected : 0.0);

        // Query 4: Top branch by fines collected
        List<Object[]> finesByBranch = getTotalFinesPaidPerBranch();
        String topBranch = "N/A";
        Double maxFines = 0.0;

        for (Object[] row : finesByBranch) {
            String branch = (String) row[0];
            Double fines = row[1] != null ? (Double) row[1] : 0.0;

            if (fines > maxFines) {
                maxFines = fines;
                topBranch = branch;
            }
        }
        dashboard.put("topBranch", topBranch);

        // Query 5: Highest fine paying member
        Object[] highestPayer = memberRepository.findHighestFinePayingMember();
        String highestFinePayingMember = "N/A";
        if (highestPayer != null && highestPayer.length > 1) {
            highestFinePayingMember = (String) highestPayer[1];
        }
        dashboard.put("highestFinePayingMember", highestFinePayingMember);

        log.info("Dashboard analytics generated successfully");
        return dashboard;
    }

    // ==================== Batch Operations ====================

    /**
     * Task 5: JPQL Update Query - Increase Daily Fine Rates
     * Batch update for specific book category with @Modifying
     */
    @Override
    public int increaseDailyFineRates(String bookType) {
        log.info("Increasing daily fine rates by 10% for book type: {}", bookType);
        int updatedCount = bookRepository.increaseDailyFineRates(bookType);
        log.info("Updated {} books with increased fine rates", updatedCount);
        return updatedCount;
    }
}

