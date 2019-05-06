package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

public class WeatherServiceImplTest {

    private static String EXPECTED_CITY_NAME = "ostrava";
    private static LocalDate EXPECTED_TEMPERATURE_DATE = LocalDate.of(2019, 5, 6);
    private static Integer EXPECTED_TEMPERATURE_VALUE = 20;
    private static WeatherService weatherService;

    @BeforeClass
    public static void setUp() throws Exception {

        weatherService = Mockito.mock(WeatherServiceImpl.class);
        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", Arrays.asList(
                new CityDto(EXPECTED_CITY_NAME, Arrays.asList(
                        new TemperatureDto(EXPECTED_TEMPERATURE_DATE, EXPECTED_TEMPERATURE_VALUE)))));
        Mockito.when(weatherService.getMaxDailyTemperaturesForAllCities()).thenCallRealMethod();
        Mockito.when(weatherService.getMaxDailyTemperatures(anyString())).thenCallRealMethod();
    }

    @Test
    public void getMaxDailyTemperaturesForAllCities_shouldReturnDataForFourCities() {
        List<CityDto> cities = weatherService.getMaxDailyTemperaturesForAllCities();
        assertEquals(EXPECTED_CITY_NAME, cities.get(0).getName());
        assertEquals(EXPECTED_TEMPERATURE_DATE, cities.get(0).getMaxDailyTemperatures().get(0).getDate());
        assertEquals(EXPECTED_TEMPERATURE_VALUE, cities.get(0).getMaxDailyTemperatures().get(0).getValue());
    }

    @Test
    public void getMaxDailyTemperatures_whenCityIsNotAvailable_shouldReturnEmptyArray() {
        List<TemperatureDto> temperatures = weatherService.getMaxDailyTemperatures("Brno");
        assertTrue(temperatures.isEmpty());
    }

    @Test
    public void getMaxDailyTemperatures_whenCityIsAvailable_shouldReturnTemperatures() {
        List<TemperatureDto> temperatures = weatherService.getMaxDailyTemperatures("Ostrava");
        assertEquals(EXPECTED_TEMPERATURE_DATE, temperatures.get(0).getDate());
        assertEquals(EXPECTED_TEMPERATURE_VALUE, temperatures.get(0).getValue());
    }
}