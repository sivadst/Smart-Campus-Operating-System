package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Notice;
import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, UUID> {
    Page<Notice> findAllByIsPublishedTrueOrderByIsPinnedDescPublishedAtDesc(Pageable pageable);
    Page<Notice> findAllByCategory(NoticeCategory category, Pageable pageable);
    List<Notice> findAllByAuthorId(UUID authorId);

    @Query("SELECT n FROM Notice n WHERE n.isPublished = true AND (n.targetRole IS NULL OR n.targetRole = :role) AND (n.expiresAt IS NULL OR n.expiresAt > :now) ORDER BY n.isPinned DESC, n.publishedAt DESC")
    Page<Notice> findActiveNoticesForRole(@Param("role") UserRole role, @Param("now") Instant now, Pageable pageable);
}
