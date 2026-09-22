package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.response.RoomBookingResponse;
import com.campus.smartcampus.enums.BookingStatus;
import com.campus.smartcampus.service.RoomBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomBookingController Unit Tests")
class RoomBookingControllerTest {

    @Mock
    private RoomBookingService bookingService;

    @InjectMocks
    private RoomBookingController bookingController;

    private MockMvc mockMvc;

    private UUID bookingId;
    private RoomBookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        bookingId = UUID.randomUUID();

        bookingResponse = RoomBookingResponse.builder()
                .id(bookingId)
                .roomId(UUID.randomUUID())
                .roomName("Seminar Hall A")
                .roomNumber("SH-101")
                .buildingName("Main Block")
                .bookedById(UUID.randomUUID())
                .bookedByName("Prof. Lakshmi Nair")
                .purpose("Guest Lecture on AI Ethics")
                .startTime(Instant.parse("2026-10-20T10:00:00Z"))
                .endTime(Instant.parse("2026-10-20T12:00:00Z"))
                .status(BookingStatus.PENDING)
                .attendeeCount(80)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/bookings/{id} should return booking by ID")
    void getBookingById_ReturnsBooking() throws Exception {
        when(bookingService.getBookingById(bookingId)).thenReturn(bookingResponse);

        mockMvc.perform(get("/api/v1/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(bookingId.toString()))
                .andExpect(jsonPath("$.data.roomName").value("Seminar Hall A"))
                .andExpect(jsonPath("$.data.purpose").value("Guest Lecture on AI Ethics"))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.attendeeCount").value(80));

        verify(bookingService).getBookingById(bookingId);
    }

    @Test
    @DisplayName("PUT /api/v1/bookings/{id}/reject should reject booking and return 200 OK")
    void rejectBooking_ReturnsRejected() throws Exception {
        RoomBookingResponse rejectedResponse = RoomBookingResponse.builder()
                .id(bookingId)
                .roomName("Seminar Hall A")
                .roomNumber("SH-101")
                .purpose("Guest Lecture on AI Ethics")
                .status(BookingStatus.REJECTED)
                .remarks("Room under maintenance")
                .build();

        when(bookingService.rejectBooking(eq(bookingId), isNull())).thenReturn(rejectedResponse);

        mockMvc.perform(put("/api/v1/bookings/{id}/reject", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking rejected"))
                .andExpect(jsonPath("$.data.status").value("REJECTED"));

        verify(bookingService).rejectBooking(eq(bookingId), isNull());
    }

    @Test
    @DisplayName("PUT /api/v1/bookings/{id}/cancel should cancel booking and return 200 OK")
    void cancelBooking_ReturnsCancelled() throws Exception {
        RoomBookingResponse cancelledResponse = RoomBookingResponse.builder()
                .id(bookingId)
                .roomName("Seminar Hall A")
                .roomNumber("SH-101")
                .purpose("Guest Lecture on AI Ethics")
                .status(BookingStatus.CANCELLED)
                .build();

        when(bookingService.cancelBooking(bookingId)).thenReturn(cancelledResponse);

        mockMvc.perform(put("/api/v1/bookings/{id}/cancel", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking cancelled"))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));

        verify(bookingService).cancelBooking(bookingId);
    }
}
