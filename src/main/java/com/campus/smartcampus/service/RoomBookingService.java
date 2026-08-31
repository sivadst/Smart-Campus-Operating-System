package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.RoomBookingRequest;
import com.campus.smartcampus.dto.response.RoomBookingResponse;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.entity.RoomBooking;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.BookingStatus;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.RoomBookingRepository;
import com.campus.smartcampus.repository.RoomRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoomBookingResponse createBooking(RoomBookingRequest request, UUID userId) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", request.getRoomId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!room.isAvailable()) {
            throw new BadRequestException("Room " + room.getRoomNumber() + " is not available for booking");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        List<RoomBooking> conflicts = bookingRepository.findConflictingBookings(
                request.getRoomId(), request.getStartTime(), request.getEndTime());
        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Room " + room.getRoomNumber() +
                    " has a conflicting booking during the requested time slot");
        }

        RoomBooking booking = RoomBooking.builder()
                .room(room)
                .bookedBy(user)
                .purpose(request.getPurpose())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .remarks(request.getRemarks())
                .attendeeCount(request.getAttendeeCount())
                .build();

        RoomBooking saved = bookingRepository.save(booking);
        log.info("Created booking for room {} by user {}", room.getRoomNumber(), user.getEmail());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RoomBookingResponse> getUserBookings(UUID userId) {
        return bookingRepository.findAllByBookedById(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomBookingResponse getBookingById(UUID id) {
        RoomBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        return mapToResponse(booking);
    }

    @Transactional
    public RoomBookingResponse approveBooking(UUID bookingId, String approvedBy) {
        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be approved");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setApprovedBy(approvedBy);
        RoomBooking saved = bookingRepository.save(booking);
        log.info("Approved booking {} for room {}", bookingId, booking.getRoom().getRoomNumber());
        return mapToResponse(saved);
    }

    @Transactional
    public RoomBookingResponse rejectBooking(UUID bookingId, String remarks) {
        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be rejected");
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setRemarks(remarks);
        RoomBooking saved = bookingRepository.save(booking);
        log.info("Rejected booking {} for room {}", bookingId, booking.getRoom().getRoomNumber());
        return mapToResponse(saved);
    }

    @Transactional
    public RoomBookingResponse cancelBooking(UUID bookingId) {
        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot cancel a completed or already cancelled booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        RoomBooking saved = bookingRepository.save(booking);
        log.info("Cancelled booking {}", bookingId);
        return mapToResponse(saved);
    }

    private RoomBookingResponse mapToResponse(RoomBooking booking) {
        return RoomBookingResponse.builder()
                .id(booking.getId())
                .roomId(booking.getRoom().getId())
                .roomName(booking.getRoom().getName())
                .roomNumber(booking.getRoom().getRoomNumber())
                .buildingName(booking.getRoom().getBuilding().getName())
                .bookedById(booking.getBookedBy().getId())
                .bookedByName(booking.getBookedBy().getFirstName() + " " + booking.getBookedBy().getLastName())
                .purpose(booking.getPurpose())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .approvedBy(booking.getApprovedBy())
                .remarks(booking.getRemarks())
                .attendeeCount(booking.getAttendeeCount())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
