package com.campus.smartcampus.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CampusException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public CampusException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
