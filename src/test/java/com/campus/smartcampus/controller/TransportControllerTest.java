package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BusRouteRequest;
import com.campus.smartcampus.dto.response.BusRouteResponse;
import com.campus.smartcampus.service.TransportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransportController Unit Tests")
class TransportControllerTest {

    @Mock
    private TransportService transportService;

    @InjectMocks
    private TransportController transportController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID routeId;
    private BusRouteResponse routeResponse;
    private BusRouteRequest routeRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transportController).build();
        objectMapper = new ObjectMapper();

        routeId = UUID.randomUUID();

        routeResponse = BusRouteResponse.builder()
                .id(routeId)
                .routeNumber("R-12A")
                .source("Central Railway Station")
                .destination("Smart Campus Main Gate")
                .stops("T. Nagar, Guindy, Velachery, Tambaram")
                .totalSeats(50)
                .driverName("Murugan S.")
                .driverPhone("+91-9876501234")
                .isActive(true)
                .build();

        routeRequest = BusRouteRequest.builder()
                .routeNumber("R-12A")
                .source("Central Railway Station")
                .destination("Smart Campus Main Gate")
                .stops("T. Nagar, Guindy, Velachery, Tambaram")
                .totalSeats(50)
                .driverName("Murugan S.")
                .driverPhone("+91-9876501234")
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/transport/routes should create route and return 201 Created")
    void createRoute_ReturnsCreated() throws Exception {
        when(transportService.createRoute(any(BusRouteRequest.class))).thenReturn(routeResponse);

        mockMvc.perform(post("/api/v1/transport/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bus route created successfully"))
                .andExpect(jsonPath("$.data.routeNumber").value("R-12A"))
                .andExpect(jsonPath("$.data.source").value("Central Railway Station"))
                .andExpect(jsonPath("$.data.totalSeats").value(50));

        verify(transportService).createRoute(any(BusRouteRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/transport/routes should return all active routes")
    void getAllRoutes_ReturnsList() throws Exception {
        when(transportService.getAllActiveRoutes()).thenReturn(List.of(routeResponse));

        mockMvc.perform(get("/api/v1/transport/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].routeNumber").value("R-12A"))
                .andExpect(jsonPath("$.data[0].destination").value("Smart Campus Main Gate"))
                .andExpect(jsonPath("$.data[0].driverName").value("Murugan S."));

        verify(transportService).getAllActiveRoutes();
    }

    @Test
    @DisplayName("GET /api/v1/transport/routes/{id} should return route by ID")
    void getRouteById_ReturnsRoute() throws Exception {
        when(transportService.getRouteById(routeId)).thenReturn(routeResponse);

        mockMvc.perform(get("/api/v1/transport/routes/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(routeId.toString()))
                .andExpect(jsonPath("$.data.stops").value("T. Nagar, Guindy, Velachery, Tambaram"))
                .andExpect(jsonPath("$.data.driverPhone").value("+91-9876501234"));

        verify(transportService).getRouteById(routeId);
    }

    @Test
    @DisplayName("DELETE /api/v1/transport/routes/{id} should deactivate route and return 200 OK")
    void deactivateRoute_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/transport/routes/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bus route deactivated successfully"));

        verify(transportService).deactivateRoute(routeId);
    }
}
