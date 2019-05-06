package com.wheater.service.dto;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class CityDtoTest {

    private static final String NAME_NULLABLE = null;
    private static final String NAME_EMPTY = "";
    private static final String NAME_NOT_EMPTY = "ostrava";
    private static final List<TemperatureDto> TEMPERATURES_NULLABLE = null;
    private static final List<TemperatureDto> TEMPERATURES_EMTPY = new ArrayList<>();
    private static final List<TemperatureDto> TEMPERATURES_NOT_EMTPY = Arrays.asList(new TemperatureDto(LocalDate.now(), 20));

    @Test(expected = IllegalArgumentException.class)
    public void createCityDto_whenNameIsNull_shouldThrowException() {
        new CityDto(NAME_NULLABLE, TEMPERATURES_EMTPY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCityDto_whenNameIsEmpty_shouldThrowException() {
        new CityDto(NAME_EMPTY, TEMPERATURES_EMTPY);
    }

    @Test
    public void createCityDto_whenNameIsNotEmpty_shouldThrowException() {
        new CityDto(NAME_NOT_EMPTY, TEMPERATURES_EMTPY);
    }

    @Test(expected = NullPointerException.class)
    public void createCityDto_whenTemperatureListIsNull_shouldThrowException() {
        new CityDto(NAME_NOT_EMPTY, TEMPERATURES_NULLABLE);
    }

    @Test
    public void createCityDto_whenTemperatureListIsEmpty_shouldReturnInstance() {
        assertNotNull(new CityDto(NAME_NOT_EMPTY, TEMPERATURES_EMTPY));
    }

    @Test
    public void createCityDto_whenCityIsNotEmptyAndTemperatureListIsNotEmpty_shouldReturnInstance() {
        assertNotNull(new CityDto(NAME_NOT_EMPTY, TEMPERATURES_NOT_EMTPY));
    }
}