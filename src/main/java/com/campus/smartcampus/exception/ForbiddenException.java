package com.campus.smartcampus.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends CampusException {
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN);
    }
}
