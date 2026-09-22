package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.response.EventResponse;
import com.campus.smartcampus.enums.EventCategory;
import com.campus.smartcampus.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventController Unit Tests")
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    private UUID eventId;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        eventId = UUID.randomUUID();

        eventResponse = EventResponse.builder()
                .id(eventId)
                .title("Spring Boot Hackathon 2026")
                .description("Build innovative campus solutions using Spring Boot")
                .category(EventCategory.HACKATHON)
                .organizerId(UUID.randomUUID())
                .organizerName("Dr. Priya Sharma")
                .venue("Innovation Lab, Block A")
                .startTime(Instant.parse("2026-10-15T09:00:00Z"))
                .endTime(Instant.parse("2026-10-15T18:00:00Z"))
                .maxAttendees(100)
                .currentAttendees(42)
                .isRegistrationOpen(true)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/events should return paginated active events")
    void getAllEvents_ReturnsPage() throws Exception {
        Page<EventResponse> page = new PageImpl<>(List.of(eventResponse));
        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Spring Boot Hackathon 2026"))
                .andExpect(jsonPath("$.data.content[0].category").value("HACKATHON"))
                .andExpect(jsonPath("$.data.content[0].currentAttendees").value(42));

        verify(eventService).getAllEvents(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/events/upcoming should return future events sorted by start time")
    void getUpcomingEvents_ReturnsPage() throws Exception {
        Page<EventResponse> page = new PageImpl<>(List.of(eventResponse));
        when(eventService.getUpcomingEvents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/events/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Spring Boot Hackathon 2026"))
                .andExpect(jsonPath("$.data.content[0].venue").value("Innovation Lab, Block A"));

        verify(eventService).getUpcomingEvents(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/events/category/{category} should return events filtered by category")
    void getEventsByCategory_ReturnsFiltered() throws Exception {
        Page<EventResponse> page = new PageImpl<>(List.of(eventResponse));
        when(eventService.getEventsByCategory(eq(EventCategory.HACKATHON), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/events/category/{category}", "HACKATHON"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].category").value("HACKATHON"));

        verify(eventService).getEventsByCategory(eq(EventCategory.HACKATHON), any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/events/{id} should return event details by ID")
    void getEventById_ReturnsEvent() throws Exception {
        when(eventService.getEventById(eventId)).thenReturn(eventResponse);

        mockMvc.perform(get("/api/v1/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(eventId.toString()))
                .andExpect(jsonPath("$.data.title").value("Spring Boot Hackathon 2026"))
                .andExpect(jsonPath("$.data.organizerName").value("Dr. Priya Sharma"))
                .andExpect(jsonPath("$.data.maxAttendees").value(100));

        verify(eventService).getEventById(eventId);
    }

    @Test
    @DisplayName("POST /api/v1/events/{id}/rsvp should register for event and return 200 OK")
    void rsvpEvent_ReturnsOk() throws Exception {
        mockMvc.perform(post("/api/v1/events/{id}/rsvp", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully registered for event"));

        verify(eventService).rsvpEvent(eventId);
    }

    @Test
    @DisplayName("DELETE /api/v1/events/{id} should cancel event and return 200 OK")
    void cancelEvent_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Event cancelled successfully"));

        verify(eventService).cancelEvent(eventId);
    }
}
