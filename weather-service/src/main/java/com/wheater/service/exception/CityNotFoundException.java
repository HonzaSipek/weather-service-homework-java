package com.wheater.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * City not found exception is generated every time when city name is used in filter condition.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "City not found.")
public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String message) {
        super(message);
    }
}
