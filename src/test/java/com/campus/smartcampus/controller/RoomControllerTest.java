package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.RoomRequest;
import com.campus.smartcampus.dto.response.RoomResponse;
import com.campus.smartcampus.enums.RoomType;
import com.campus.smartcampus.service.RoomService;
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
@DisplayName("RoomController Unit Tests")
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID roomId;
    private UUID buildingId;
    private RoomResponse roomResponse;
    private RoomRequest roomRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
        objectMapper = new ObjectMapper();

        roomId = UUID.randomUUID();
        buildingId = UUID.randomUUID();

        roomResponse = RoomResponse.builder()
                .id(roomId)
                .roomNumber("LH-301")
                .name("Advanced Computing Lab")
                .roomType(RoomType.COMPUTER_LAB)
                .capacity(60)
                .floor(3)
                .buildingId(buildingId)
                .buildingName("Innovation Centre")
                .hasProjector(true)
                .hasAC(true)
                .hasWifi(true)
                .isAvailable(true)
                .build();

        roomRequest = RoomRequest.builder()
                .roomNumber("LH-301")
                .name("Advanced Computing Lab")
                .roomType(RoomType.COMPUTER_LAB)
                .capacity(60)
                .floor(3)
                .buildingId(buildingId)
                .hasProjector(true)
                .hasAC(true)
                .hasWifi(true)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/rooms should create room and return 201 Created")
    void createRoom_ReturnsCreated() throws Exception {
        when(roomService.createRoom(any(RoomRequest.class))).thenReturn(roomResponse);

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room created successfully"))
                .andExpect(jsonPath("$.data.roomNumber").value("LH-301"))
                .andExpect(jsonPath("$.data.roomType").value("COMPUTER_LAB"))
                .andExpect(jsonPath("$.data.capacity").value(60));

        verify(roomService).createRoom(any(RoomRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/rooms/{id} should return room by ID")
    void getRoomById_ReturnsRoom() throws Exception {
        when(roomService.getRoomById(roomId)).thenReturn(roomResponse);

        mockMvc.perform(get("/api/v1/rooms/{id}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(roomId.toString()))
                .andExpect(jsonPath("$.data.name").value("Advanced Computing Lab"))
                .andExpect(jsonPath("$.data.buildingName").value("Innovation Centre"));

        verify(roomService).getRoomById(roomId);
    }

    @Test
    @DisplayName("GET /api/v1/rooms/building/{buildingId} should return rooms in building")
    void getRoomsByBuilding_ReturnsList() throws Exception {
        when(roomService.getRoomsByBuilding(buildingId)).thenReturn(List.of(roomResponse));

        mockMvc.perform(get("/api/v1/rooms/building/{buildingId}", buildingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].roomNumber").value("LH-301"))
                .andExpect(jsonPath("$.data[0].floor").value(3));

        verify(roomService).getRoomsByBuilding(buildingId);
    }

    @Test
    @DisplayName("GET /api/v1/rooms/available should return all available rooms")
    void getAvailableRooms_ReturnsList() throws Exception {
        when(roomService.getAvailableRooms()).thenReturn(List.of(roomResponse));

        mockMvc.perform(get("/api/v1/rooms/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].available").value(true));

        verify(roomService).getAvailableRooms();
    }

    @Test
    @DisplayName("GET /api/v1/rooms/type/{roomType} should return rooms by type")
    void getRoomsByType_ReturnsList() throws Exception {
        when(roomService.getRoomsByType(RoomType.COMPUTER_LAB)).thenReturn(List.of(roomResponse));

        mockMvc.perform(get("/api/v1/rooms/type/{roomType}", "COMPUTER_LAB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].roomType").value("COMPUTER_LAB"));

        verify(roomService).getRoomsByType(RoomType.COMPUTER_LAB);
    }

    @Test
    @DisplayName("GET /api/v1/rooms/capacity/{minCapacity} should return rooms with sufficient capacity")
    void getRoomsByCapacity_ReturnsList() throws Exception {
        when(roomService.getRoomsByCapacity(50)).thenReturn(List.of(roomResponse));

        mockMvc.perform(get("/api/v1/rooms/capacity/{minCapacity}", 50))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].capacity").value(60));

        verify(roomService).getRoomsByCapacity(50);
    }

    @Test
    @DisplayName("PUT /api/v1/rooms/{id} should update room and return 200 OK")
    void updateRoom_ReturnsUpdated() throws Exception {
        when(roomService.updateRoom(eq(roomId), any(RoomRequest.class))).thenReturn(roomResponse);

        mockMvc.perform(put("/api/v1/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room updated successfully"))
                .andExpect(jsonPath("$.data.roomNumber").value("LH-301"));

        verify(roomService).updateRoom(eq(roomId), any(RoomRequest.class));
    }

    @Test
    @DisplayName("PATCH /api/v1/rooms/{id}/toggle-availability should toggle room availability")
    void toggleAvailability_ReturnsOk() throws Exception {
        mockMvc.perform(patch("/api/v1/rooms/{id}/toggle-availability", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room availability toggled"));

        verify(roomService).toggleAvailability(roomId);
    }
}
