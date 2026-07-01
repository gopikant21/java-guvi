package org.example.libraryjwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.libraryjwt.dto.JwtResponseDTO;
import org.example.libraryjwt.dto.LoginRequestDTO;
import org.example.libraryjwt.dto.MemberSummaryDTO;
import org.example.libraryjwt.entity.Book;
import org.example.libraryjwt.entity.FineTransaction;
import org.example.libraryjwt.entity.IssueRecord;
import org.example.libraryjwt.entity.Member;
import org.example.libraryjwt.security.JwtUtil;
import org.example.libraryjwt.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Library Management API
 * Task 6: Pagination & Sorting for books endpoint
 * Task 8: JWT Authentication with login endpoint
 * Task 9: Role-Based Authorization with @PreAuthorize
 * Task 7: DTO Projection Mapping
 * Final Challenge: Dashboard analytics endpoint
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==================== Authentication ====================

    /**
     * Task 8: JWT Authentication - Login endpoint
     * Permit all (no authentication required)
     */
    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            Member member = libraryService.getMemberById(extractMemberIdFromAuth(authentication));
            String token = jwtUtil.generateToken(member.getEmail(), member.getMemberId(), member.getRole());

            JwtResponseDTO response = JwtResponseDTO.builder()
                    .token(token)
                    .type("Bearer")
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .build();

            log.info("User {} logged in successfully", loginRequest.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Register new member
     */
    @PostMapping("/auth/register")
    public ResponseEntity<Member> register(@Valid @RequestBody Member member) {
        log.info("Registering new member: {}", member.getEmail());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole("USER");
        Member savedMember = libraryService.addMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    // ==================== Member Management ====================

    /**
     * Get member summary with DTO projection (Task 7)
     */
    @GetMapping("/members/{memberId}/summary")
    public ResponseEntity<MemberSummaryDTO> getMemberSummary(@PathVariable Long memberId) {
        log.info("Fetching summary for member: {}", memberId);
        MemberSummaryDTO summary = libraryService.getMemberSummary(memberId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Get all members from a specific branch
     */
    @GetMapping("/members/branch/{branch}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getMembersByBranch(@PathVariable String branch) {
        log.info("Fetching members from branch: {}", branch);
        List<Member> members = libraryService.getMembersByBranch(branch);
        return ResponseEntity.ok(members);
    }

    /**
     * Get avid readers (members with most checkouts)
     */
    @GetMapping("/members/avid-readers/{targetCount}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getAvidReaders(@PathVariable long targetCount) {
        log.info("Fetching avid readers with > {} checkouts", targetCount);
        List<Member> avidReaders = libraryService.findAvidReaders(targetCount);
        return ResponseEntity.ok(avidReaders);
    }

    /**
     * Get members with multiple book genres
     */
    @GetMapping("/members/multi-genre/{genreCount}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getMembersWithMultiGenreBooks(@PathVariable long genreCount) {
        log.info("Fetching members with {} or more distinct book genres", genreCount);
        List<Member> members = libraryService.findMembersWithMultiGenreBooks(genreCount);
        return ResponseEntity.ok(members);
    }

    // ==================== Book Management ====================

    /**
     * Add new book
     * Task 9: Only ADMIN can add books
     */
    @PostMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        log.info("Adding new book: {} with ISBN: {}", book.getTitle(), book.getIsbn());
        Book savedBook = libraryService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    /**
     * Task 6: Get books with pagination and sorting
     * Sorted by dailyFineRate in descending order by default
     * Task 9: USER role can view catalog
     */
    @GetMapping("/books")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Book>> getBooks(
            @PageableDefault(size = 10, page = 0, sort = "dailyFineRate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        log.info("Fetching books with pagination: {}", pageable);
        Page<Book> books = libraryService.getBooksPaginated(pageable);
        return ResponseEntity.ok(books.getContent());
    }

    /**
     * Get books by type
     */
    @GetMapping("/books/type/{bookType}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String bookType) {
        log.info("Fetching books of type: {}", bookType);
        List<Book> books = libraryService.getBooksByType(bookType);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books with higher fine rate
     */
    @GetMapping("/books/fine-rate/{amount}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Book>> getBooksWithHigherFineRate(@PathVariable double amount) {
        log.info("Fetching books with fine rate > {}", amount);
        List<Book> books = libraryService.getBooksWithHigherFineRate(amount);
        return ResponseEntity.ok(books);
    }

    /**
     * Get books with no fine history
     */
    @GetMapping("/books/no-fines")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Book>> getBooksWithNoFineHistory() {
        log.info("Fetching books with no fine history");
        List<Book> books = libraryService.getBooksWithNoFineHistory();
        return ResponseEntity.ok(books);
    }

    /**
     * Delete book
     * Task 9: Only ADMIN can delete books
     */
    @DeleteMapping("/books/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        log.info("Deleting book with ISBN: {}", isbn);
        libraryService.getBookByIsbn(isbn); // Validate exists
        // In production, implement actual delete logic
        return ResponseEntity.ok().build();
    }

    /**
     * Task 5: Increase daily fine rates for book category
     * Task 9: Only MANAGER and ADMIN can update fine rates
     */
    @PutMapping("/books/fine-rates/increase/{bookType}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> increaseDailyFineRates(@PathVariable String bookType) {
        log.info("Increasing daily fine rates for book type: {}", bookType);
        int updatedCount = libraryService.increaseDailyFineRates(bookType);
        return ResponseEntity.ok(Map.of(
                "message", "Fine rates updated successfully",
                "updatedBooks", updatedCount
        ));
    }

    // ==================== Issue Record Management ====================

    /**
     * Issue book to member
     */
    @PostMapping("/issues/issue/{memberId}/{isbn}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<IssueRecord> issueBook(@PathVariable Long memberId, @PathVariable String isbn) {
        log.info("Issuing book {} to member {}", isbn, memberId);
        IssueRecord record = libraryService.issueBook(memberId, isbn);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    /**
     * Return book
     */
    @PutMapping("/issues/return/{issueId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<IssueRecord> returnBook(@PathVariable Long issueId) {
        log.info("Returning book for issue record: {}", issueId);
        IssueRecord record = libraryService.returnBook(issueId);
        return ResponseEntity.ok(record);
    }

    /**
     * Get member's issue history
     */
    @GetMapping("/issues/member/{memberId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<IssueRecord>> getMemberIssueHistory(@PathVariable Long memberId) {
        log.info("Fetching issue history for member: {}", memberId);
        List<IssueRecord> history = libraryService.getMemberIssueHistory(memberId);
        return ResponseEntity.ok(history);
    }

    // ==================== Fine Transaction Management ====================

    /**
     * Record fine payment
     */
    @PostMapping("/fines/record/{memberId}/{isbn}/{amount}/{paymentType}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<FineTransaction> recordFinePayment(
            @PathVariable Long memberId,
            @PathVariable String isbn,
            @PathVariable Double amount,
            @PathVariable String paymentType) {
        log.info("Recording fine payment: member={}, book={}, amount={}, type={}", memberId, isbn, amount, paymentType);
        FineTransaction transaction = libraryService.recordFinePayment(memberId, isbn, amount, paymentType);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Get fines by payment type
     */
    @GetMapping("/fines/payment-type/{paymentType}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<FineTransaction>> getFinesByPaymentType(@PathVariable String paymentType) {
        log.info("Fetching fines by payment type: {}", paymentType);
        List<FineTransaction> fines = libraryService.getFineTransactionsByPaymentType(paymentType);
        return ResponseEntity.ok(fines);
    }

    /**
     * Get latest fine payment
     */
    @GetMapping("/fines/latest")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<FineTransaction> getLatestFinePayment() {
        log.info("Fetching latest fine payment");
        FineTransaction latest = libraryService.getLatestFinePayment();
        return ResponseEntity.ok(latest);
    }

    /**
     * Get fines paid per branch
     */
    @GetMapping("/fines/per-branch")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getFinesPaidPerBranch() {
        log.info("Fetching total fines paid per branch");
        List<Object[]> finesByBranch = libraryService.getTotalFinesPaidPerBranch();
        return ResponseEntity.ok(finesByBranch);
    }

    // ==================== Analytics & Dashboard ====================

    /**
     * Final Challenge: Dashboard endpoint
     * Returns unified platform overview with minimal optimized queries
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        log.info("Generating dashboard analytics");
        Map<String, Object> dashboard = libraryService.getDashboardAnalytics();
        return ResponseEntity.ok(dashboard);
    }

    // ==================== Health Check ====================

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    // ==================== Helper Methods ====================

    private Long extractMemberIdFromAuth(Authentication authentication) {
        String email = authentication.getName();
        Member member = libraryService.getMemberByEmail(email);
        return member.getMemberId();
    }
}
