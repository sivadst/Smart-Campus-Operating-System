package com.campus.smartcampus.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CampusException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value), "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
