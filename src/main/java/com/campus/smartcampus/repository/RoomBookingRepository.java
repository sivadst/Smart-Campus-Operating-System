package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.RoomBooking;
import com.campus.smartcampus.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, UUID> {
    List<RoomBooking> findAllByBookedById(UUID userId);
    List<RoomBooking> findAllByRoomIdAndStatus(UUID roomId, BookingStatus status);

    @Query("SELECT rb FROM RoomBooking rb WHERE rb.room.id = :roomId AND rb.status = 'CONFIRMED' AND " +
           "((rb.startTime <= :endTime AND rb.endTime >= :startTime))")
    List<RoomBooking> findConflictingBookings(
            @Param("roomId") UUID roomId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);
}
