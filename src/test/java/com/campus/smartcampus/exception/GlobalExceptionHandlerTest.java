package com.campus.smartcampus.exception;

import com.campus.smartcampus.dto.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should return 404 NOT_FOUND for ResourceNotFoundException")
    void handleResourceNotFound_Returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Course", "id", "abc-123");

        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("NOT_FOUND", response.getBody().getError().getCode());
        assertTrue(response.getBody().getError().getMessage().contains("Course"));
    }

    @Test
    @DisplayName("Should return 409 CONFLICT for DuplicateResourceException")
    void handleDuplicateResource_Returns409() {
        DuplicateResourceException ex = new DuplicateResourceException("User", "email", "test@campus.edu");

        ResponseEntity<ErrorResponse> response = handler.handleDuplicateResourceException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DUPLICATE_RESOURCE", response.getBody().getError().getCode());
        assertTrue(response.getBody().getError().getMessage().contains("User"));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST for BadRequestException")
    void handleBadRequest_Returns400() {
        BadRequestException ex = new BadRequestException("Start time must be before end time");

        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("BAD_REQUEST", response.getBody().getError().getCode());
        assertEquals("Start time must be before end time", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("Should return 401 UNAUTHORIZED for UnauthorizedException")
    void handleUnauthorized_Returns401() {
        UnauthorizedException ex = new UnauthorizedException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = handler.handleUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UNAUTHORIZED", response.getBody().getError().getCode());
    }

    @Test
    @DisplayName("Should return 403 FORBIDDEN for ForbiddenException")
    void handleForbidden_Returns403() {
        ForbiddenException ex = new ForbiddenException("Insufficient privileges");

        ResponseEntity<ErrorResponse> response = handler.handleForbiddenException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("FORBIDDEN", response.getBody().getError().getCode());
    }

    @Test
    @DisplayName("Should return 403 FORBIDDEN for Spring AccessDeniedException")
    void handleAccessDenied_Returns403() {
        AccessDeniedException ex = new AccessDeniedException("Access is denied");

        ResponseEntity<ErrorResponse> response = handler.handleAccessDeniedException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("FORBIDDEN", response.getBody().getError().getCode());
        assertEquals("Access denied: insufficient permissions", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("Should return 409 CONFLICT for DataIntegrityViolationException")
    void handleDataIntegrityViolation_Returns409() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Unique constraint violated");

        ResponseEntity<ErrorResponse> response = handler.handleDataIntegrityViolation(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DATA_INTEGRITY_VIOLATION", response.getBody().getError().getCode());
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST for MissingServletRequestParameterException")
    void handleMissingParameter_Returns400() throws Exception {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("grade", "String");

        ResponseEntity<ErrorResponse> response = handler.handleMissingParameter(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("MISSING_PARAMETER", response.getBody().getError().getCode());
        assertTrue(response.getBody().getError().getMessage().contains("grade"));
    }

    @Test
    @DisplayName("Should return 500 INTERNAL_SERVER_ERROR for uncaught exceptions")
    void handleUncaughtException_Returns500() {
        Exception ex = new RuntimeException("Something went terribly wrong");

        ResponseEntity<ErrorResponse> response = handler.handleAllUncaughtException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError().getCode());
        assertEquals("An unexpected error occurred", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("Should return custom status for CampusException")
    void handleCampusException_ReturnsCustomStatus() {
        CampusException ex = new CampusException("Custom campus error", "CUSTOM_ERROR", HttpStatus.UNPROCESSABLE_ENTITY);

        ResponseEntity<ErrorResponse> response = handler.handleCampusException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CUSTOM_ERROR", response.getBody().getError().getCode());
        assertEquals("Custom campus error", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("All error responses should have success=false")
    void allResponses_HaveSuccessFalse() {
        ResponseEntity<ErrorResponse> r1 = handler.handleBadRequestException(new BadRequestException("bad"));
        ResponseEntity<ErrorResponse> r2 = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("X", "id", "1"));
        ResponseEntity<ErrorResponse> r3 = handler.handleAllUncaughtException(new RuntimeException("oops"));

        assertFalse(r1.getBody().isSuccess());
        assertFalse(r2.getBody().isSuccess());
        assertFalse(r3.getBody().isSuccess());
    }
}
