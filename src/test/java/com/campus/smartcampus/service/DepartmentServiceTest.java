package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.DepartmentRequest;
import com.campus.smartcampus.dto.response.DepartmentResponse;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentService Unit Tests")
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentRequest validRequest;
    private Department department;
    private UUID departmentId;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        validRequest = DepartmentRequest.builder()
                .name("Computer Science")
                .code("CSE")
                .description("CS Department")
                .headOfDepartment("Dr. Smith")
                .build();

        department = Department.builder()
                .id(departmentId)
                .name("Computer Science")
                .code("CSE")
                .description("CS Department")
                .headOfDepartment("Dr. Smith")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create department successfully")
    void createDepartment_ValidRequest_ReturnsDepartmentResponse() {
        when(departmentRepository.existsByCode("CSE")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentResponse response = departmentService.createDepartment(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Computer Science");
        assertThat(response.getCode()).isEqualTo("CSE");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when code exists")
    void createDepartment_DuplicateCode_ThrowsException() {
        when(departmentRepository.existsByCode("CSE")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.createDepartment(validRequest))
                .isInstanceOf(DuplicateResourceException.class);

        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return all active departments")
    void getAllDepartments_ReturnsActiveOnly() {
        when(departmentRepository.findAllByIsActiveTrue()).thenReturn(List.of(department));

        List<DepartmentResponse> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("CSE");
    }

    @Test
    @DisplayName("Should return department by ID")
    void getDepartmentById_ExistingId_ReturnsDepartment() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        DepartmentResponse result = departmentService.getDepartmentById(departmentId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(departmentId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for invalid ID")
    void getDepartmentById_InvalidId_ThrowsException() {
        UUID invalidId = UUID.randomUUID();
        when(departmentRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartmentById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should soft-delete department")
    void deleteDepartment_ExistingId_SetsInactive() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        departmentService.deleteDepartment(departmentId);

        verify(departmentRepository).save(argThat(dept -> !dept.isActive()));
    }
}
