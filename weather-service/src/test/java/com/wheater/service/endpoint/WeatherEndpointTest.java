package com.wheater.service.endpoint;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.service.WeatherService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-it.properties")
public class WeatherEndpointTest {

    private static final ParameterizedTypeReference<List<CityDto>> CITIES_RESPONSE_TYPE = new ParameterizedTypeReference<List<CityDto>>() {
    };
    private static final ParameterizedTypeReference<List<TemperatureDto>> TEMPERATURES_RESPONSE_TYPE = new ParameterizedTypeReference<List<TemperatureDto>>() {
    };
    private static final Class NO_RESPONSE_TYPE = Object.class;

    private static final String DEFAULT_CITY_NAME = "Ostrava";
    private static final String NEW_CITY_NAME = "Zlin";
    private static final String ALREADY_CREATE_CITY_NAME = "Olomouc";
    private static final String NOT_EXISTING_CITY_NAME = "Znojmo";

    private static LocalDate ALREADY_ADDED_TEMPERATURE_DATE = LocalDate.of(2017, 9, 19);
    private static Integer ALREADY_ADDED_TEMPERATURE_VALUE = 20;
    private static LocalDate NEW_TEMPERATURE_DATE = LocalDate.of(2015, 5, 6);
    private static Integer NEW_ADDED_TEMPERATURE_VALUE = 10;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void cleanUp() throws Exception {

        ReflectionTestUtils.setField(weatherService, "maxDailyTemperatures", ReflectionTestUtils.invokeGetterMethod(weatherService, "loadMaxDailyTemperatures"));
    }

    /**
     * Checkpoint 1
     */
    @Test
    public void getMaxDailyTemperaturesForAllCities_shouldReturnFourCities() {
        List<CityDto> cities = restTemplate.exchange("/weather/cities", HttpMethod.GET, null, CITIES_RESPONSE_TYPE).getBody();
        assertEquals(4, cities.size());
    }

    /**
     * Checkpoint 1
     */
    @Test
    public void getMaxDailyTemperaturesForAllCities_whenDataForCityAreAvailable_shouldReturnTemperatures() {
        List<TemperatureDto> temperatures = restTemplate.exchange(String.format("/weather/cities/%s/temperatures", DEFAULT_CITY_NAME), HttpMethod.GET, null, TEMPERATURES_RESPONSE_TYPE).getBody();
        assertTrue(!temperatures.isEmpty());
    }

    /**
     * Checkpoint 2
     */
    @Test
    public void createCity_whenCityDoesNotExistYet_shouldReturnAllCities() {
        List<CityDto> cities = restTemplate.exchange("/weather/cities", HttpMethod.POST, new HttpEntity(NEW_CITY_NAME), CITIES_RESPONSE_TYPE).getBody();
        assertTrue(cities.stream().filter(cityDto -> cityDto.getName().equalsIgnoreCase(NEW_CITY_NAME)).findFirst().isPresent());
        assertTrue(cities.stream().filter(cityDto -> cityDto.getName().equalsIgnoreCase(NEW_CITY_NAME)).findFirst().get().getMaxDailyTemperatures().isEmpty());
    }

    /**
     * Checkpoint 2
     */
    @Test
    public void createCity_whenCityIsAlreadyCreated_shouldReturn409Code() {
        List<CityDto> cities = restTemplate.exchange("/weather/cities", HttpMethod.POST, new HttpEntity(ALREADY_CREATE_CITY_NAME), CITIES_RESPONSE_TYPE).getBody();
        assertTrue(cities.stream().filter(cityDto -> cityDto.getName().equalsIgnoreCase(ALREADY_CREATE_CITY_NAME)).findFirst().isPresent());
        assertTrue(cities.stream().filter(cityDto -> cityDto.getName().equalsIgnoreCase(ALREADY_CREATE_CITY_NAME)).findFirst().get().getMaxDailyTemperatures().isEmpty());
        assertEquals(HttpStatus.CONFLICT, restTemplate.exchange("/weather/cities", HttpMethod.POST, new HttpEntity(ALREADY_CREATE_CITY_NAME), NO_RESPONSE_TYPE).getStatusCode());
    }

    /**
     * Checkpoint 2
     */
    @Test
    public void getMaxDailyTemperaturesForCity_whenCityExists_shouldReturnTemperatures() {
        List<TemperatureDto> temperatures = restTemplate.exchange(String.format("/weather/cities/%s/temperatures", DEFAULT_CITY_NAME), HttpMethod.GET, null, TEMPERATURES_RESPONSE_TYPE).getBody();
        assertTrue(!temperatures.isEmpty());
    }

    /**
     * Checkpoint 2
     */
    @Test
    public void getMaxDailyTemperaturesForCity_whenCityDoesNotExist_shouldReturn404Code() {
        assertEquals(HttpStatus.NOT_FOUND, restTemplate.exchange(String.format("/weather/cities/%s/temperatures", NOT_EXISTING_CITY_NAME), HttpMethod.GET, null, NO_RESPONSE_TYPE).getStatusCode());
    }

    /**
     * Checkpoint 3
     */
    @Test
    public void addTemperature_whenDateIsAlreadyExist_shouldReturn409Code(){
        assertEquals(HttpStatus.CONFLICT, restTemplate.exchange(String.format("/weather/cities/%s/temperatures", DEFAULT_CITY_NAME), HttpMethod.POST,
                new HttpEntity(new TemperatureDto(ALREADY_ADDED_TEMPERATURE_DATE, ALREADY_ADDED_TEMPERATURE_VALUE)), NO_RESPONSE_TYPE).getStatusCode());
    }

    /**
     * Checkpoint 3
     */
    @Test
    public void addTemperature_whenDateDoesNotExist_shouldReturnAllTemperatures(){
        List<TemperatureDto> temperatures = restTemplate.exchange(String.format("/weather/cities/%s/temperatures", DEFAULT_CITY_NAME), HttpMethod.POST,
                new HttpEntity(new TemperatureDto(NEW_TEMPERATURE_DATE, NEW_ADDED_TEMPERATURE_VALUE)), TEMPERATURES_RESPONSE_TYPE).getBody();
        assertTrue(temperatures.stream().anyMatch(temperatureDto -> temperatureDto.getDate().equals(NEW_TEMPERATURE_DATE)));
    }

}