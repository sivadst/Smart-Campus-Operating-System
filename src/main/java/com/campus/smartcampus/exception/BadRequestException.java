package com.campus.smartcampus.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CampusException {
    public BadRequestException(String message) {
        super(message, "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    }
}
