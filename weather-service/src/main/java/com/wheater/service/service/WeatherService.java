package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;

import java.time.LocalDate;
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

    /**
     * Create city with empty temperature list.
     *
     * @param cityName city name
     */
    void createCity(String cityName);

    /**
     * Added maximum daily temperature for specific city.
     *
     * @param cityName city name
     * @param date     date
     * @param value    temperature integer value
     */
    void addMaxDailyTemperature(String cityName, LocalDate date, Integer value);
}
