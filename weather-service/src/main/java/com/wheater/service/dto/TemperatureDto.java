package com.wheater.service.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Data transfer object which represents date and its maximum temperature integer value.
 */
public class TemperatureDto {

    private LocalDate date;
    private Integer value;

    public TemperatureDto(LocalDate date, Integer value) {
        Objects.requireNonNull(date);
        Objects.requireNonNull(value);
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getValue() {
        return value;
    }
}
