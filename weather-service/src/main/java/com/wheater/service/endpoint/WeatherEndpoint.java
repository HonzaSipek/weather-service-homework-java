package com.wheater.service.endpoint;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/cities/temperatures")
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
}
