package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BuildingRequest;
import com.campus.smartcampus.dto.response.BuildingResponse;
import com.campus.smartcampus.service.BuildingService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuildingController Unit Tests")
class BuildingControllerTest {

    @Mock
    private BuildingService buildingService;

    @InjectMocks
    private BuildingController buildingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID buildingId;
    private BuildingResponse buildingResponse;
    private BuildingRequest buildingRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(buildingController).build();
        objectMapper = new ObjectMapper();

        buildingId = UUID.randomUUID();

        buildingResponse = BuildingResponse.builder()
                .id(buildingId)
                .name("Innovation Centre")
                .code("IC-01")
                .address("North Campus, Gate 3")
                .totalFloors(5)
                .latitude(13.0827)
                .longitude(80.2707)
                .isActive(true)
                .roomCount(42)
                .build();

        buildingRequest = BuildingRequest.builder()
                .name("Innovation Centre")
                .code("IC-01")
                .address("North Campus, Gate 3")
                .totalFloors(5)
                .latitude(13.0827)
                .longitude(80.2707)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/buildings should register building and return 201 Created")
    void createBuilding_ReturnsCreated() throws Exception {
        when(buildingService.createBuilding(any(BuildingRequest.class))).thenReturn(buildingResponse);

        mockMvc.perform(post("/api/v1/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Building created successfully"))
                .andExpect(jsonPath("$.data.name").value("Innovation Centre"))
                .andExpect(jsonPath("$.data.code").value("IC-01"))
                .andExpect(jsonPath("$.data.totalFloors").value(5));

        verify(buildingService).createBuilding(any(BuildingRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/buildings should return all active buildings")
    void getAllBuildings_ReturnsList() throws Exception {
        when(buildingService.getAllBuildings()).thenReturn(List.of(buildingResponse));

        mockMvc.perform(get("/api/v1/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Innovation Centre"))
                .andExpect(jsonPath("$.data[0].roomCount").value(42));

        verify(buildingService).getAllBuildings();
    }

    @Test
    @DisplayName("GET /api/v1/buildings/{id} should return building by ID")
    void getBuildingById_ReturnsBuilding() throws Exception {
        when(buildingService.getBuildingById(buildingId)).thenReturn(buildingResponse);

        mockMvc.perform(get("/api/v1/buildings/{id}", buildingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(buildingId.toString()))
                .andExpect(jsonPath("$.data.address").value("North Campus, Gate 3"))
                .andExpect(jsonPath("$.data.latitude").value(13.0827));

        verify(buildingService).getBuildingById(buildingId);
    }

    @Test
    @DisplayName("PUT /api/v1/buildings/{id} should update building and return 200 OK")
    void updateBuilding_ReturnsUpdated() throws Exception {
        when(buildingService.updateBuilding(eq(buildingId), any(BuildingRequest.class))).thenReturn(buildingResponse);

        mockMvc.perform(put("/api/v1/buildings/{id}", buildingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Building updated successfully"))
                .andExpect(jsonPath("$.data.code").value("IC-01"));

        verify(buildingService).updateBuilding(eq(buildingId), any(BuildingRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/buildings/{id} should soft-delete building and return 200 OK")
    void deleteBuilding_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/buildings/{id}", buildingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Building deleted successfully"));

        verify(buildingService).deleteBuilding(buildingId);
    }
}
