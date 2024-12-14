package io.rons.book.history;

import io.rons.book.book.BorrowedBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {


    @Query("""
            select history
            from BookTransactionHistory history
            where history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            select history
            from BookTransactionHistory history
            where history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
            select
            (count(*) > 0) As isBorrowed
            from  BookTransactionHistory history
            where history.user.id = :userId
            and history.book.id = :bookId
            and history.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
            select t
            from BookTransactionHistory t
            where t.user.id = :userId
            and t.book.id = :bookId
            and t.returned = false
            and t.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
            select t
            form BookTransactionHistory t
            where t.book.id = :bookId
            and t.returned = true
            and t.returnApproved = false
            """)
    Optional <BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer id);
}
