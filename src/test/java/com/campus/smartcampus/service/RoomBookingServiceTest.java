package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.RoomBookingRequest;
import com.campus.smartcampus.dto.response.RoomBookingResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.entity.RoomBooking;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.BookingStatus;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.repository.RoomBookingRepository;
import com.campus.smartcampus.repository.RoomRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomBookingService Unit Tests")
class RoomBookingServiceTest {

    @Mock private RoomBookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private RoomBookingService bookingService;

    private Room room;
    private User user;
    private UUID roomId, userId, bookingId;
    private Instant start, end;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        userId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
        start = Instant.now().plus(1, ChronoUnit.HOURS);
        end = Instant.now().plus(3, ChronoUnit.HOURS);

        Building building = Building.builder().id(UUID.randomUUID()).name("Auditorium Complex").build();
        room = Room.builder().id(roomId).roomNumber("AUD-1").name("Main Hall").building(building).isAvailable(true).build();
        user = User.builder().id(userId).firstName("Club").lastName("President").email("club@campus.edu").role(UserRole.STUDENT).build();
    }

    @Test
    @DisplayName("Should create booking when no conflict exists")
    void createBooking_NoConflicts_ReturnsResponse() {
        RoomBookingRequest request = RoomBookingRequest.builder()
                .roomId(roomId).purpose("Annual General Meeting").startTime(start).endTime(end)
                .build();

        RoomBooking booking = RoomBooking.builder()
                .id(bookingId).room(room).bookedBy(user).purpose("Annual General Meeting")
                .startTime(start).endTime(end).status(BookingStatus.PENDING)
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findConflictingBookings(roomId, start, end)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(RoomBooking.class))).thenReturn(booking);

        RoomBookingResponse response = bookingService.createBooking(request, userId);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(response.getPurpose()).isEqualTo("Annual General Meeting");
    }

    @Test
    @DisplayName("Should throw BadRequestException on schedule conflict")
    void createBooking_ConflictExists_ThrowsException() {
        RoomBookingRequest request = RoomBookingRequest.builder()
                .roomId(roomId).purpose("Annual General Meeting").startTime(start).endTime(end)
                .build();

        RoomBooking existing = RoomBooking.builder().id(UUID.randomUUID()).status(BookingStatus.CONFIRMED).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findConflictingBookings(roomId, start, end)).thenReturn(List.of(existing));

        assertThatThrownBy(() -> bookingService.createBooking(request, userId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("conflicting booking");
    }

    @Test
    @DisplayName("Should approve pending booking")
    void approveBooking_PendingStatus_UpdatesToConfirmed() {
        RoomBooking booking = RoomBooking.builder()
                .id(bookingId).room(room).bookedBy(user).status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(RoomBooking.class))).thenReturn(booking);

        RoomBookingResponse response = bookingService.approveBooking(bookingId, "admin@campus.edu");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }
}
