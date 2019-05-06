package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.exception.CityAlreadyCreatedException;
import com.wheater.service.exception.CityNotFoundException;
import com.wheater.service.exception.TemperatureExistingException;
import com.wheater.service.exception.WeatherDataException;
import com.wheater.service.util.WeatherDataUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String WEATHER_DATA_FILE_NAME = "max_daily_temperatures.json";

    private List<CityDto> maxDailyTemperatures;

    public WeatherServiceImpl() throws WeatherDataException {
        this.maxDailyTemperatures = loadMaxDailyTemperatures();
    }

    @Override
    public List<CityDto> getMaxDailyTemperaturesForAllCities() {
        return maxDailyTemperatures;
    }

    @Override
    public List<TemperatureDto> getMaxDailyTemperatures(String cityName) {
        Objects.requireNonNull(cityName);
        return this.maxDailyTemperatures.stream()
                .filter(city -> city.getName().equalsIgnoreCase(cityName))
                .findFirst()
                .map(CityDto::getMaxDailyTemperatures)
                .orElseThrow(() -> new CityNotFoundException(String.format("City with name: %s could not be found", cityName)));

    }

    @Override
    public void createCity(String cityName) {
        Objects.requireNonNull(cityName);
        if (this.maxDailyTemperatures.stream().anyMatch(city -> city.getName().equalsIgnoreCase(cityName))) {
            throw new CityAlreadyCreatedException(String.format("City with name: %s is already created", cityName));
        }
        this.maxDailyTemperatures.add(new CityDto(cityName.toLowerCase(), Collections.emptyList()));
    }

    @Override
    public void addMaxDailyTemperature(String cityName, LocalDate date, Integer value) {
        Objects.requireNonNull(cityName);
        List<TemperatureDto> temperatures = this.maxDailyTemperatures.stream()
                .filter(city -> city.getName().equalsIgnoreCase(cityName))
                .findFirst()
                .orElseThrow(() -> new CityNotFoundException(String.format("City with name: %s could not be found", cityName)))
                .getMaxDailyTemperatures();
        temperatures.stream()
                .filter(temperatureDto -> temperatureDto.getDate().equals(date))
                .findFirst()
                .ifPresent(temperatureDto -> {
                    throw new TemperatureExistingException(temperatureDto);
                });
        temperatures.add(new TemperatureDto(date, value));
    }

    /**
     * Loads maximum daily temperatures for cities from static JSON file.
     *
     * @return cities with their maximum daily temperatures
     * @throws WeatherDataException if something during data loading failed
     */
    private List<CityDto> loadMaxDailyTemperatures() throws WeatherDataException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(WEATHER_DATA_FILE_NAME)) {
            return WeatherDataUtil.loadData(is);
        } catch (IOException e) {
            throw new WeatherDataException("Failed to load weather data");
        }
    }
}
