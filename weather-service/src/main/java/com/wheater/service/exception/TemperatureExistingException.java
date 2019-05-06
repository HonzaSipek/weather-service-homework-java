package com.wheater.service.exception;

import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.util.WeatherDataUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Temperature existing exception is generated when new temperature already exists.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Temperature with this date was already added.")
public class TemperatureExistingException extends RuntimeException {

    public TemperatureExistingException(TemperatureDto temperature) {

        super(String.format("Temperature with date: %s already exists with value %d.",
                temperature.getDate().format(WeatherDataUtil.DATE_PATTERN),
                temperature.getValue()));
    }
}
