package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.EventRequest;
import com.campus.smartcampus.dto.response.EventResponse;
import com.campus.smartcampus.entity.Event;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.EventCategory;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.repository.EventRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventService Unit Tests")
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private User organizer;
    private Event event;
    private UUID organizerId;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        organizer = User.builder()
                .id(organizerId)
                .email("lead@campus.edu")
                .firstName("Alice")
                .lastName("Organizer")
                .role(UserRole.FACULTY)
                .build();

        event = Event.builder()
                .id(eventId)
                .title("Smart Campus Hackathon 2026")
                .description("Build the next-gen campus AI tools")
                .category(EventCategory.HACKATHON)
                .organizer(organizer)
                .venue("Main Auditorium")
                .startTime(Instant.now().plus(7, ChronoUnit.DAYS))
                .endTime(Instant.now().plus(9, ChronoUnit.DAYS))
                .maxAttendees(100)
                .currentAttendees(10)
                .isRegistrationOpen(true)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create event successfully")
    void createEvent_ValidRequest_ReturnsEventResponse() {
        EventRequest request = EventRequest.builder()
                .title("Smart Campus Hackathon 2026")
                .description("Build the next-gen campus AI tools")
                .category(EventCategory.HACKATHON)
                .venue("Main Auditorium")
                .startTime(Instant.now().plus(7, ChronoUnit.DAYS))
                .endTime(Instant.now().plus(9, ChronoUnit.DAYS))
                .maxAttendees(100)
                .build();

        when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponse response = eventService.createEvent(request, organizerId);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Smart Campus Hackathon 2026");
        assertThat(response.getCategory()).isEqualTo(EventCategory.HACKATHON);
    }

    @Test
    @DisplayName("Should throw when start time is after end time")
    void createEvent_InvalidTimes_ThrowsException() {
        EventRequest request = EventRequest.builder()
                .title("Invalid Event")
                .description("Test")
                .category(EventCategory.WORKSHOP)
                .startTime(Instant.now().plus(9, ChronoUnit.DAYS))
                .endTime(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));

        assertThatThrownBy(() -> eventService.createEvent(request, organizerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Start time must be before end time");
    }

    @Test
    @DisplayName("Should record RSVP successfully")
    void rsvpEvent_ValidOpenEvent_IncrementsAttendees() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.rsvpEvent(eventId);

        verify(eventRepository).save(argThat(e -> e.getCurrentAttendees() == 11));
    }
}
