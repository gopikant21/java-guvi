package org.example.libraryjwt.repository;

import org.example.libraryjwt.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Member entity.
 * Task 3: Spring Data JPA Derived Queries
 * Task 4: JPQL Queries for complex business logic
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Task 3: Derived query - Find members by membership branch
     */
    List<Member> findByMembershipBranch(String branch);

    /**
     * Find member by email
     */
    Optional<Member> findByEmail(String email);

    /**
     * Task 4: JPQL Query - Find Avid Readers
     * Return members who have checked out books more than a specific target count
     */
    @Query("SELECT DISTINCT m FROM Member m JOIN m.issueRecords ir " +
           "GROUP BY m.memberId " +
           "HAVING COUNT(ir.issueId) > :targetCount")
    List<Member> findAvidReaders(@Param("targetCount") long targetCount);

    /**
     * Task 4: JPQL Query - Find Members Holding Multi-Genre Book Types
     * Identify members borrowing multiple distinct categories of books
     */
    @Query("SELECT DISTINCT m FROM Member m JOIN m.issueRecords ir JOIN ir.book b " +
           "GROUP BY m.memberId " +
           "HAVING COUNT(DISTINCT b.bookType) > :genreCount")
    List<Member> findMembersWithMultiGenreBooks(@Param("genreCount") long genreCount);

    /**
     * Task 4: JPQL Query - Find Total Fines Paid Per Branch
     * Returns each library branch with total fines collected
     * Returns as Object array: [branch, totalFines]
     */
    @Query("SELECT m.membershipBranch, SUM(ft.amount) FROM Member m " +
           "LEFT JOIN m.fineTransactions ft " +
           "GROUP BY m.membershipBranch")
    List<Object[]> findTotalFinesPaidPerBranch();

    /**
     * Get member count for analytics
     */
    @Query("SELECT COUNT(m) FROM Member m")
    long getTotalMembers();

    /**
     * Get highest fine paying member - Returns member and total fine amount
     */
    @Query(value = "SELECT m.member_id, m.member_name, SUM(ft.amount) as total_fine " +
                   "FROM members m " +
                   "LEFT JOIN fine_transactions ft ON m.member_id = ft.member_id " +
                   "GROUP BY m.member_id, m.member_name " +
                   "ORDER BY total_fine DESC " +
                   "LIMIT 1",
           nativeQuery = true)
    Object[] findHighestFinePayingMember();
}

