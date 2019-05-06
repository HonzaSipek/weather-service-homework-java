package com.wheater.service.service;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.exception.CityAlreadyCreatedException;
import com.wheater.service.exception.CityNotFoundException;
import com.wheater.service.exception.TemperatureExistingException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

public class WeatherServiceImplTest {

    private static String DEFAULT_CITY_NAME = "Ostrava";
    private static final String NEW_CITY_NAME = "Zlin";
    private static final String NULLABLE_CITY_NAME = null;

    private static LocalDate ALREADY_ADDED_TEMPERATURE_DATE = LocalDate.of(2019, 5, 6);
    private static Integer ALREADY_ADDED_TEMPERATURE_VALUE = 20;
    private static LocalDate NEW_TEMPERATURE_DATE = LocalDate.of(2015, 5, 6);
    private static Integer NEW_ADDED_TEMPERATURE_VALUE = 10;
    private static LocalDate NULLABLE_TEMPERATURE_DATE = null;
    private static Integer NULLABLE_TEMPERATURE_VALUE = null;
    private static List<CityDto> DEFAULT_CITIES;
    private static WeatherService weatherService;

    @BeforeClass
    public static void setUp() throws Exception {

        weatherService = Mockito.mock(WeatherServiceImpl.class);
        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", buildDefaultCities());
        Mockito.when(weatherService.getMaxDailyTemperaturesForAllCities()).thenCallRealMethod();
        Mockito.when(weatherService.getMaxDailyTemperatures(any())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(weatherService).createCity(any());
        Mockito.doCallRealMethod().when(weatherService).addMaxDailyTemperature(any(), any(), any());
    }

    @After
    public void cleanUp(){
        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", buildDefaultCities());
    }

    @Test
    public void getMaxDailyTemperaturesForAllCities_shouldReturnDataForFourCities() {
        List<CityDto> cities = weatherService.getMaxDailyTemperaturesForAllCities();
        assertEquals(DEFAULT_CITY_NAME.toLowerCase(), cities.get(0).getName());
        assertEquals(ALREADY_ADDED_TEMPERATURE_DATE, cities.get(0).getMaxDailyTemperatures().get(0).getDate());
        assertEquals(ALREADY_ADDED_TEMPERATURE_VALUE, cities.get(0).getMaxDailyTemperatures().get(0).getValue());
    }

    @Test(expected = CityNotFoundException.class)
    public void getMaxDailyTemperatures_whenCityDoesNotExist_shouldThrowException() {
        weatherService.getMaxDailyTemperatures(NEW_CITY_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void getMaxDailyTemperatures_whenCityIsNull_shouldThrowException() {
        weatherService.getMaxDailyTemperatures(NULLABLE_CITY_NAME);
    }

    @Test
    public void getMaxDailyTemperatures_whenCityExists_shouldReturnTemperatures() {
        List<TemperatureDto> temperatures = weatherService.getMaxDailyTemperatures(DEFAULT_CITY_NAME);
        assertEquals(ALREADY_ADDED_TEMPERATURE_DATE, temperatures.get(0).getDate());
        assertEquals(ALREADY_ADDED_TEMPERATURE_VALUE, temperatures.get(0).getValue());
    }

    @Test
    public void createCity_whenCityDoesNotExistYet_shouldCreateCity() {
        weatherService.createCity(NEW_CITY_NAME);
        assertTrue(weatherService.getMaxDailyTemperatures(NEW_CITY_NAME).isEmpty());
    }

    @Test(expected = CityAlreadyCreatedException.class)
    public void createCity_whenCityIsAlreadyCreated_shouldThrowException() {
        weatherService.createCity(DEFAULT_CITY_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void createCity_whenCityIsNull_shouldThrowException() {
        weatherService.createCity(NULLABLE_CITY_NAME);
    }

    @Test(expected = CityNotFoundException.class)
    public void addMaxDailyTemperature_whenCityDoesNotExist_shouldThrowException(){
        weatherService.addMaxDailyTemperature(NEW_CITY_NAME, NEW_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void addMaxDailyTemperature_whenCityIsNull_shouldThrowException(){
        weatherService.addMaxDailyTemperature(NULLABLE_CITY_NAME, NEW_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void addMaxDailyTemperature_whenDateIsNull_shouldThrowException(){
        weatherService.addMaxDailyTemperature(DEFAULT_CITY_NAME, NULLABLE_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void addMaxDailyTemperature_whenValueIsNull_shouldThrowException(){
        weatherService.addMaxDailyTemperature(DEFAULT_CITY_NAME, NEW_TEMPERATURE_DATE, NULLABLE_TEMPERATURE_VALUE);
    }

    @Test(expected = TemperatureExistingException.class)
    public void addMaxDailyTemperature_whenTemporaryWithDateAlreadyExist_shouldThrowException(){
        weatherService.addMaxDailyTemperature(DEFAULT_CITY_NAME, ALREADY_ADDED_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE);
    }

    @Test
    public void addMaxDailyTemperature_whenTemporaryWithNotAlreadyExistingDate_shouldAddNew(){
        weatherService.addMaxDailyTemperature(DEFAULT_CITY_NAME, NEW_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE);
    }

    private static List<CityDto> buildDefaultCities(){
        List<CityDto> defaultCities = new ArrayList<>();
        List<TemperatureDto> defaultTemperatures = new ArrayList<>();
        defaultTemperatures.add(new TemperatureDto(ALREADY_ADDED_TEMPERATURE_DATE, ALREADY_ADDED_TEMPERATURE_VALUE));
        defaultCities.add(new CityDto(DEFAULT_CITY_NAME.toLowerCase(), defaultTemperatures));
        return defaultCities;
    }
}