package com.wheater.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "City is already created.")
public class AlreadyCreatedCityException extends RuntimeException {

    public AlreadyCreatedCityException(String message) {
        super(message);
    }
}
