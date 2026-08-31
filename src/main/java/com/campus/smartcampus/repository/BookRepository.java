package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Book;
import com.campus.smartcampus.enums.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Page<Book> findAllByIsActiveTrue(Pageable pageable);
    Page<Book> findAllByCategoryAndIsActiveTrue(BookCategory category, Pageable pageable);
    Page<Book> findAllByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);
}
