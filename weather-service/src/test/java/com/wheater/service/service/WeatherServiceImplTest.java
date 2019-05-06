package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.exception.AlreadyCreatedCityException;
import com.wheater.service.exception.CityNotFoundException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

public class WeatherServiceImplTest {

    private static String DEFAULT_CITY_NAME = "Ostrava";
    private static final String NEW_CITY_NAME = "Zlin";

    private static LocalDate EXPECTED_TEMPERATURE_DATE = LocalDate.of(2019, 5, 6);
    private static Integer EXPECTED_TEMPERATURE_VALUE = 20;
    private static List<CityDto> DEFAULT_CITIES;
    private static WeatherService weatherService;

    @BeforeClass
    public static void setUp() throws Exception {

        weatherService = Mockito.mock(WeatherServiceImpl.class);
        DEFAULT_CITIES = new ArrayList<>();
        DEFAULT_CITIES.add(new CityDto(DEFAULT_CITY_NAME.toLowerCase(), Arrays.asList(
                new TemperatureDto(EXPECTED_TEMPERATURE_DATE, EXPECTED_TEMPERATURE_VALUE))));
        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", DEFAULT_CITIES);
        Mockito.when(weatherService.getMaxDailyTemperaturesForAllCities()).thenCallRealMethod();
        Mockito.when(weatherService.getMaxDailyTemperatures(anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(weatherService).createCity(anyString());
    }

    @After
    public void cleanUp(){
        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", DEFAULT_CITIES);
    }

    @Test
    public void getMaxDailyTemperaturesForAllCities_shouldReturnDataForFourCities() {
        List<CityDto> cities = weatherService.getMaxDailyTemperaturesForAllCities();
        assertEquals(DEFAULT_CITY_NAME.toLowerCase(), cities.get(0).getName());
        assertEquals(EXPECTED_TEMPERATURE_DATE, cities.get(0).getMaxDailyTemperatures().get(0).getDate());
        assertEquals(EXPECTED_TEMPERATURE_VALUE, cities.get(0).getMaxDailyTemperatures().get(0).getValue());
    }

    @Test(expected = CityNotFoundException.class)
    public void getMaxDailyTemperatures_whenCityIsNotAvailable_shouldReturnEmptyArray() {
        weatherService.getMaxDailyTemperatures("Brno");
    }

    @Test
    public void getMaxDailyTemperatures_whenCityIsAvailable_shouldReturnTemperatures() {
        List<TemperatureDto> temperatures = weatherService.getMaxDailyTemperatures("Ostrava");
        assertEquals(EXPECTED_TEMPERATURE_DATE, temperatures.get(0).getDate());
        assertEquals(EXPECTED_TEMPERATURE_VALUE, temperatures.get(0).getValue());
    }

    @Test
    public void createCity_whenCityDoesNotExistYet_shouldCreateCity() {
        weatherService.createCity(NEW_CITY_NAME);
        assertTrue(weatherService.getMaxDailyTemperatures(NEW_CITY_NAME).isEmpty());
    }

    @Test(expected = AlreadyCreatedCityException.class)
    public void createCity_whenCityIsAlreadyCreated_shouldThrowException() {
        weatherService.createCity(DEFAULT_CITY_NAME);
    }
}