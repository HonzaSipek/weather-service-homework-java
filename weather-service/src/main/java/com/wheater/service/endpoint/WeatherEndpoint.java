package com.wheater.service.endpoint;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Weather endpoint provides REST CRUD methods for maximum daily temperatures of cities.
 */
@RestController()
@RequestMapping("weather")
public class WeatherEndpoint {

    private final WeatherService weatherService;

    @Autowired
    public WeatherEndpoint(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Gets maximum daily temperatures for all cities (Example 1).
     *
     * @return cities and their maximum daily temperatures
     */
    @GetMapping(value = "/cities")
    public List<CityDto> getMaxDailyTemperaturesForAllCities() {
        return this.weatherService.getMaxDailyTemperaturesForAllCities();
    }

    /**
     * Gets maximum daily temperatures for specific city (Example 1).
     *
     * @param city city name
     * @return maximum daily temperatures
     */
    @GetMapping(value = "/cities/{city}/temperatures")
    public List<TemperatureDto> getMaxDailyTemperaturesForCity(@PathVariable("city") String city) {
        return this.weatherService.getMaxDailyTemperatures(city);
    }

    /**
     * Create new city.
     *
     * @param city city name
     * @return all cities with their daily maximum temperatures.
     */
    @PostMapping(value = "/cities")
    public List<CityDto> createCity(@RequestBody String city) {
        this.weatherService.createCity(city);
        return this.weatherService.getMaxDailyTemperaturesForAllCities();
    }

    /**
     * Add new temperature for specific city-
     *
     * @param city        city name
     * @param temperature maximum daily temperature
     * @return all maximum daily temperatures for specific city
     */
    @PostMapping(value = "/cities/{city}/temperatures")
    public List<TemperatureDto> addTemperature(@PathVariable("city") String city, @RequestBody TemperatureDto temperature) {
        this.weatherService.addMaxDailyTemperature(city, temperature.getDate(), temperature.getValue());
        return this.weatherService.getMaxDailyTemperatures(city);
    }
}
