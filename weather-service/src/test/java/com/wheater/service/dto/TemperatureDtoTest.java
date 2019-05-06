package com.wheater.service.dto;

import org.junit.Test;

import java.time.LocalDate;

public class TemperatureDtoTest {

    private static final LocalDate DATE_NULLABLE = null;
    private static final LocalDate DATE_VALID = LocalDate.now();
    private static final Integer VALUE_NULLABLE = null;
    private static final Integer VALUE_VALID = 20;

    @Test(expected = NullPointerException.class)
    public void createTemperatureDto_whenDateIsNull_shouldThrowException() {
        new TemperatureDto(DATE_NULLABLE, VALUE_VALID);
    }

    @Test(expected = NullPointerException.class)
    public void createTemperatureDto_whenValueIsNull_shouldThrowException() {
        new TemperatureDto(DATE_VALID, VALUE_NULLABLE);
    }

    @Test
    public void createTemperatureDto_whenDateAndValueAreNotNull_shouldReturnInstance() {
        new TemperatureDto(DATE_VALID, VALUE_VALID);
    }
}