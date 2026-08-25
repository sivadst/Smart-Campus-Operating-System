package com.campus.smartcampus.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CampusException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
}
