package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.DepartmentRequest;
import com.campus.smartcampus.dto.response.DepartmentResponse;
import com.campus.smartcampus.service.DepartmentService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentController Unit Tests")
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID departmentId;
    private DepartmentResponse departmentResponse;
    private DepartmentRequest departmentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        objectMapper = new ObjectMapper();

        departmentId = UUID.randomUUID();

        departmentResponse = DepartmentResponse.builder()
                .id(departmentId)
                .name("Computer Science and Engineering")
                .code("CSE")
                .description("Department of CSE")
                .headOfDepartment("Dr. Alan Turing")
                .isActive(true)
                .courseCount(12)
                .build();

        departmentRequest = DepartmentRequest.builder()
                .name("Computer Science and Engineering")
                .code("CSE")
                .description("Department of CSE")
                .headOfDepartment("Dr. Alan Turing")
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/departments should create and return 201 Created")
    void createDepartment_ReturnsCreated() throws Exception {
        when(departmentService.createDepartment(any(DepartmentRequest.class))).thenReturn(departmentResponse);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("CSE"))
                .andExpect(jsonPath("$.data.name").value("Computer Science and Engineering"));

        verify(departmentService).createDepartment(any(DepartmentRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/departments should return list of active departments")
    void getAllDepartments_ReturnsList() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(departmentResponse));

        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("CSE"))
                .andExpect(jsonPath("$.data[0].headOfDepartment").value("Dr. Alan Turing"));

        verify(departmentService).getAllDepartments();
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id} should return department by ID")
    void getDepartmentById_ReturnsDepartment() throws Exception {
        when(departmentService.getDepartmentById(departmentId)).thenReturn(departmentResponse);

        mockMvc.perform(get("/api/v1/departments/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(departmentId.toString()))
                .andExpect(jsonPath("$.data.code").value("CSE"));

        verify(departmentService).getDepartmentById(departmentId);
    }

    @Test
    @DisplayName("PUT /api/v1/departments/{id} should update and return department")
    void updateDepartment_ReturnsUpdated() throws Exception {
        when(departmentService.updateDepartment(eq(departmentId), any(DepartmentRequest.class)))
                .thenReturn(departmentResponse);

        mockMvc.perform(put("/api/v1/departments/{id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.headOfDepartment").value("Dr. Alan Turing"));

        verify(departmentService).updateDepartment(eq(departmentId), any(DepartmentRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/departments/{id} should soft delete department")
    void deleteDepartment_ReturnsSuccess() throws Exception {
        doNothing().when(departmentService).deleteDepartment(departmentId);

        mockMvc.perform(delete("/api/v1/departments/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Department deleted successfully"));

        verify(departmentService).deleteDepartment(departmentId);
    }
}
