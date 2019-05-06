package com.wheater.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * City already created exception is generating when new city already created.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "City is already created.")
public class CityAlreadyCreatedException extends RuntimeException {

    public CityAlreadyCreatedException(String message) {
        super(message);
    }
}
