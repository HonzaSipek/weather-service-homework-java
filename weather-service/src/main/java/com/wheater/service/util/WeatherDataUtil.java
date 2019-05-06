package com.wheater.service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheater.service.dto.CityDto;
import com.wheater.service.dto.TemperatureDto;
import com.wheater.service.exception.WeatherDataException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility for convert JSON weather data to objects.
 */
public class WeatherDataUtil {

    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final TypeReference<Map<String, List<Map<String, List<Map<String, Integer>>>>>> TYPE_REFERENCE
            = new TypeReference<Map<String, List<Map<String, List<Map<String, Integer>>>>>>() {
    };

    private WeatherDataUtil() {
    }

    /**
     * Loads data from JSON input stream.
     *
     * @param jsonDataInputStream JSON data input stream
     * @return cities with temperatures
     */
    public static List<CityDto> loadData(InputStream jsonDataInputStream) throws WeatherDataException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, List<Map<String, List<Map<String, Integer>>>>> data = mapper.readValue(jsonDataInputStream, TYPE_REFERENCE);
            return Optional.ofNullable(data.get("cities"))
                    .orElseThrow(WeatherDataException::new)
                    .stream()
                    .flatMap(WeatherDataUtil::mapToCities)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new WeatherDataException("Failed to load weather data");
        }
    }

    /**
     * Maps unspecified objects to cities.
     *
     * @param city unspecified objects
     * @return cities with temperatures
     */
    private static Stream<CityDto> mapToCities(Map<String, List<Map<String, Integer>>> city) {
        return city.entrySet().stream().map(cityEntry ->
                new CityDto(cityEntry.getKey(),
                        cityEntry.getValue().stream()
                                .flatMap(WeatherDataUtil::mapToTemperatures).collect(Collectors.toList()))
        );
    }

    /**
     * Maps unspecified objects to temperatures.
     * @param temperature unspecified objects
     * @return temperatures
     */
    private static Stream<TemperatureDto> mapToTemperatures(Map<String, Integer> temperature) {
        return temperature.entrySet().stream()
                .map(stringIntegerEntry -> new TemperatureDto(LocalDate.parse(stringIntegerEntry.getKey(), DATE_PATTERN), stringIntegerEntry.getValue()));
    }
}
