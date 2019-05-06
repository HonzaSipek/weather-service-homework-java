package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;

import java.util.List;

/**
 * Weather service provides methods for manipulating with cities and their maximum daily temperatures.
 */
public interface WeatherService {

    /**
     * Gets maximum daily temperatures for all cities.
     *
     * @return cities with their maximum daily temperatures.
     */
    List<CityDto> getMaxDailyTemperaturesForAllCities();

    /**
     * Gets maximum daily temperatures for specific city.
     *
     * @param city city name
     * @return maximum daily temperatures
     */
    List<TemperatureDto> getMaxDailyTemperatures(String city);
}
