package com.wheater.service.util;

import com.wheater.service.dto.CityDto;
import com.wheater.service.exception.WeatherDataException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeatherDataUtilTest {

    @Test(expected = WeatherDataException.class)
    public void loadData_whenJsonFileIsInvalid_shouldThrowException() throws IOException, WeatherDataException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("weather-data/max_daily_temperatures_invalid_json_file.json")){
            WeatherDataUtil.loadData(is);
        }
    }

    @Test(expected = WeatherDataException.class)
    public void loadData_whenJsonFileWithoutCitiesData_shouldThrowException() throws IOException, WeatherDataException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("weather-data/max_daily_temperatures_without_cities_data.json")){
            WeatherDataUtil.loadData(is);
        }
    }

    @Test
    public void loadData_whenJsonFileDoesNotContainCities_shouldReturnEmptyArrayOfCities() throws IOException, WeatherDataException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("weather-data/max_daily_temperatures_without_cities.json")){
            assertTrue(WeatherDataUtil.loadData(is).isEmpty());
        }
    }

    @Test
    public void loadData_whenJsonFileCitiesDoNotContainTemperatures_shouldReturnEmptyArrayOfTemperatues() throws IOException, WeatherDataException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("weather-data/max_daily_temperatures_without_temperatures.json")){
            assertTrue(WeatherDataUtil.loadData(is).get(0).getMaxDailyTemperatures().isEmpty());
        }
    }

    @Test
    public void loadData_whenJsonFileContainsAll_shouldReturnCitiesAndTheirTemperatures() throws IOException, WeatherDataException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("weather-data/max_daily_temperatures.json")){
            List<CityDto> cities = WeatherDataUtil.loadData(is);
            assertTrue(!cities.isEmpty());
            assertTrue(!cities.get(0).getMaxDailyTemperatures().isEmpty());
            assertEquals(Integer.valueOf(20), cities.get(0).getMaxDailyTemperatures().get(0).getValue());
            assertEquals(LocalDate.of(2017, 9, 19), cities.get(0).getMaxDailyTemperatures().get(0).getDate());
        }
    }
}