package com.wheater.service.endpoint;

import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

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
        List<TemperatureDto> temperatures = restTemplate.exchange("/weather/cities/ostrava/temperatures", HttpMethod.GET, null, TEMPERATURES_RESPONSE_TYPE).getBody();
        assertTrue(!temperatures.isEmpty());
    }

    @Test
    public void getMaxDailyTemperaturesForAllCities_whenDataForCityAreNotAvailable_shouldReturnEmptyArray() {
        List<TemperatureDto> temperatures = restTemplate.exchange("/weather/cities/zlin/temperatures", HttpMethod.GET, null, TEMPERATURES_RESPONSE_TYPE).getBody();
        assertTrue(temperatures.isEmpty());
    }

}