package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Event;
import com.campus.smartcampus.enums.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findAllByIsActiveTrue(Pageable pageable);
    Page<Event> findAllByCategoryAndIsActiveTrue(EventCategory category, Pageable pageable);
    Page<Event> findAllByStartTimeAfterAndIsActiveTrueOrderByStartTimeAsc(Instant now, Pageable pageable);
}
