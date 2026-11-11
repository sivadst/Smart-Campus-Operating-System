package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Book;
import com.campus.smartcampus.enums.BookCategory;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") UUID id);

    Page<Book> findAllByIsActiveTrue(Pageable pageable);
    Page<Book> findAllByCategoryAndIsActiveTrue(BookCategory category, Pageable pageable);
    Page<Book> findAllByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);
}
