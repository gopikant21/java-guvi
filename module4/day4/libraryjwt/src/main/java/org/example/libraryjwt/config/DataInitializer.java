package org.example.libraryjwt.config;

import lombok.extern.slf4j.Slf4j;
import org.example.libraryjwt.entity.Book;
import org.example.libraryjwt.entity.FineTransaction;
import org.example.libraryjwt.entity.IssueRecord;
import org.example.libraryjwt.entity.Member;
import org.example.libraryjwt.repository.BookRepository;
import org.example.libraryjwt.repository.FineTransactionRepository;
import org.example.libraryjwt.repository.IssueRecordRepository;
import org.example.libraryjwt.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Data Initialization Component
 * Seeds the database with sample data for testing
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private FineTransactionRepository fineTransactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with sample data...");

        // Create sample members
        Member member1 = Member.builder()
                .memberName("Amit Patel")
                .email("amit@example.com")
                .password(passwordEncoder.encode("password123"))
                .membershipBranch("Central Library")
                .role("USER")
                .build();

        Member member2 = Member.builder()
                .memberName("Priya Sharma")
                .email("priya@example.com")
                .password(passwordEncoder.encode("password123"))
                .membershipBranch("Central Library")
                .role("USER")
                .build();

        Member member3 = Member.builder()
                .memberName("Rajesh Kumar")
                .email("rajesh@example.com")
                .password(passwordEncoder.encode("password123"))
                .membershipBranch("North Branch")
                .role("MANAGER")
                .build();

        Member member4 = Member.builder()
                .memberName("Admin User")
                .email("admin@example.com")
                .password(passwordEncoder.encode("password123"))
                .membershipBranch("Admin Office")
                .role("ADMIN")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        log.info("Created 4 sample members");

        // Create sample books
        Book book1 = Book.builder()
                .isbn("978-0134685991")
                .title("Effective Java")
                .bookType("ACADEMIC")
                .dailyFineRate(5.0)
                .build();

        Book book2 = Book.builder()
                .isbn("978-0596007928")
                .title("Head First Design Patterns")
                .bookType("ACADEMIC")
                .dailyFineRate(4.5)
                .build();

        Book book3 = Book.builder()
                .isbn("978-0140028219")
                .title("Dune")
                .bookType("FICTION")
                .dailyFineRate(3.0)
                .build();

        Book book4 = Book.builder()
                .isbn("978-0-06-112008-4")
                .title("To Kill a Mockingbird")
                .bookType("FICTION")
                .dailyFineRate(2.5)
                .build();

        Book book5 = Book.builder()
                .isbn("978-0007525256")
                .title("Oxford English Dictionary")
                .bookType("REFERENCE")
                .dailyFineRate(10.0)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        log.info("Created 5 sample books");

        // Create sample issue records
        IssueRecord issue1 = IssueRecord.builder()
                .member(member1)
                .book(book1)
                .issueDate(LocalDate.now().minusDays(30))
                .returnDate(LocalDate.now().minusDays(20))
                .status("RETURNED")
                .build();

        IssueRecord issue2 = IssueRecord.builder()
                .member(member1)
                .book(book3)
                .issueDate(LocalDate.now().minusDays(15))
                .returnDate(null)
                .status("ISSUED")
                .build();

        IssueRecord issue3 = IssueRecord.builder()
                .member(member2)
                .book(book2)
                .issueDate(LocalDate.now().minusDays(45))
                .returnDate(LocalDate.now().minusDays(35))
                .status("RETURNED")
                .build();

        IssueRecord issue4 = IssueRecord.builder()
                .member(member2)
                .book(book4)
                .issueDate(LocalDate.now().minusDays(10))
                .returnDate(null)
                .status("ISSUED")
                .build();

        IssueRecord issue5 = IssueRecord.builder()
                .member(member1)
                .book(book2)
                .issueDate(LocalDate.now().minusDays(50))
                .returnDate(LocalDate.now().minusDays(40))
                .status("RETURNED")
                .build();

        IssueRecord issue6 = IssueRecord.builder()
                .member(member1)
                .book(book4)
                .issueDate(LocalDate.now().minusDays(60))
                .returnDate(LocalDate.now().minusDays(50))
                .status("RETURNED")
                .build();

        issueRecordRepository.save(issue1);
        issueRecordRepository.save(issue2);
        issueRecordRepository.save(issue3);
        issueRecordRepository.save(issue4);
        issueRecordRepository.save(issue5);
        issueRecordRepository.save(issue6);
        log.info("Created 6 sample issue records");

        // Create sample fine transactions
        FineTransaction fine1 = FineTransaction.builder()
                .member(member1)
                .book(book1)
                .amount(50.0)
                .paymentType("CARD")
                .paymentDate(LocalDate.now().minusDays(15))
                .build();

        FineTransaction fine2 = FineTransaction.builder()
                .member(member1)
                .book(book3)
                .amount(25.0)
                .paymentType("CASH")
                .paymentDate(LocalDate.now().minusDays(5))
                .build();

        FineTransaction fine3 = FineTransaction.builder()
                .member(member2)
                .book(book2)
                .amount(45.0)
                .paymentType("ONLINE")
                .paymentDate(LocalDate.now().minusDays(10))
                .build();

        FineTransaction fine4 = FineTransaction.builder()
                .member(member1)
                .book(book4)
                .amount(30.0)
                .paymentType("CARD")
                .paymentDate(LocalDate.now().minusDays(2))
                .build();

        fineTransactionRepository.save(fine1);
        fineTransactionRepository.save(fine2);
        fineTransactionRepository.save(fine3);
        fineTransactionRepository.save(fine4);
        log.info("Created 4 sample fine transactions");

        log.info("Database initialization completed successfully!");
    }
}

