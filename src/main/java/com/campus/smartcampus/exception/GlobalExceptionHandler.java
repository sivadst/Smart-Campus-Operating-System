package com.campus.smartcampus.exception;

import com.campus.smartcampus.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampusException.class)
    public ResponseEntity<ErrorResponse> handleCampusException(CampusException ex) {
        log.error("Campus exception: {}", ex.getMessage(), ex);
        return createErrorResponse(ex.getCode(), ex.getMessage(), null, ex.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return createErrorResponse("NOT_FOUND", ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return createErrorResponse("DUPLICATE_RESOURCE", ex.getMessage(), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage());
        return createErrorResponse("UNAUTHORIZED", ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        log.warn("Validation failed: {}", details);
        return createErrorResponse("VALIDATION_ERROR", "Invalid input data", details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
        log.error("Unknown error occurred", ex);
        return createErrorResponse(
                "INTERNAL_SERVER_ERROR", 
                "An unexpected error occurred", 
                null, 
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            String code, String message, List<String> details, HttpStatus status) {
        
        ErrorResponse.ErrorDetail detail = ErrorResponse.ErrorDetail.builder()
                .code(code)
                .message(message)
                .details(details)
                .build();
                
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .error(detail)
                .build();
                
        return new ResponseEntity<>(response, status);
    }
}
