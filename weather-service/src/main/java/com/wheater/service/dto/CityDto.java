package com.wheater.service.dto;

import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Objects;

/**
 * Data transfer object which represents city and their maximum daily temperatures.
 */
public class CityDto {

    private String name;
    private List<TemperatureDto> maxDailyTemperatures;

    public CityDto(String name, List<TemperatureDto> maxDailyTemperatures) {
        if(Strings.isBlank(name)) throw new IllegalArgumentException("City name can not be null or empty.");
        Objects.requireNonNull(maxDailyTemperatures);
        this.name = name;
        this.maxDailyTemperatures = maxDailyTemperatures;
    }

    public String getName() {
        return name;
    }

    public List<TemperatureDto> getMaxDailyTemperatures() {
        return maxDailyTemperatures;
    }
}
