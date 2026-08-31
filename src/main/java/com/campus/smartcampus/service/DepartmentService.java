package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.DepartmentRequest;
import com.campus.smartcampus.dto.response.DepartmentResponse;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.DepartmentRepository;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Department", "code", request.getCode());
        }

        Department department = Department.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .headOfDepartment(request.getHeadOfDepartment())
                .build();

        Department saved = departmentRepository.save(department);
        log.info("Created department: {} ({})", saved.getName(), saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAllByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return mapToResponse(department);
    }

    @Transactional
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setHeadOfDepartment(request.getHeadOfDepartment());

        Department saved = departmentRepository.save(department);
        log.info("Updated department: {}", saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        department.setActive(false);
        departmentRepository.save(department);
        log.info("Soft-deleted department: {}", department.getCode());
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .headOfDepartment(department.getHeadOfDepartment())
                .isActive(department.isActive())
                .courseCount(department.getCourses() != null ? department.getCourses().size() : 0)
                .build();
    }
}
