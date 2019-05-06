package com.wheater.service.exception;

/**
 * Weather data exception is generated when something during data loading failed.
 */
public class WeatherDataException extends Exception {

    public WeatherDataException() {
    }

    public WeatherDataException(String message) {
        super(message);
    }
}
