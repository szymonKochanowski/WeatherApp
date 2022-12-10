package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.entity.Weather;
import com.kohan.WeatherApp.mapper.WeatherMapper;
import com.kohan.WeatherApp.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getWeatherForCityByGeolocation() throws Exception {
        //Given
        String response = "{\"coord\":{\"lon\":19.945,\"lat\":50.0647},\"weather\":[{\"id\":803,\"" +
                "main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"" +
                "main\":{\"temp\":3.59,\"feels_like\":0.05,\"temp_min\":0.9,\"temp_max\":3.9,\"pressure\":1011,\"humidity\":89},\"" +
                "visibility\":10000,\"wind\":{\"speed\":4.12,\"deg\":240},\"clouds\":{\"all\":75},\"dt\":1670348617,\"" +
                "sys\":{\"type\":2,\"id\":2074307,\"country\":\"PL\",\"sunrise\":1670307805,\"sunset\":1670337556},\"" +
                "timezone\":3600,\"id\":3085041,\"name\":\"Śródmieście\",\"cod\":200}";
        String cityName = "krakow";
        Double lat = 50.072136;
        Double lon = 19.947226;
        Weather weather = new Weather(1, "Smog", cityName, "PL", 3600, 2.65, -2.1, 4.4, 12.2, 1004, 946, 90, Timestamp.valueOf(LocalDateTime.now()), lat, lon);
        WeatherDto expectedWeatherDto = new WeatherDto("Smog", cityName, "PL", 3600, 2.65, -2.1, 4.4, 12.2, 1004, 946, 90, Timestamp.valueOf(LocalDateTime.now()), lat, lon );

        when(restTemplate.getForObject(anyString(), any(), anyDouble(), anyDouble())).thenReturn(response);
        when(weatherRepository.save(weather)).thenReturn(weather);
        when(weatherMapper.weatherToWeatherDto(any())).thenReturn(expectedWeatherDto);

        //When
        WeatherDto actualWeatherDto = weatherService.getWeatherForCityByGeolocation(lat, lon, cityName);

        //Then
        assertEquals(expectedWeatherDto.city(), actualWeatherDto.city());
        assertEquals(expectedWeatherDto.country(), actualWeatherDto.country());
        assertEquals(expectedWeatherDto.timezone(), actualWeatherDto.timezone());
        assertEquals(expectedWeatherDto.windSpeed(), actualWeatherDto.windSpeed());
        assertEquals(expectedWeatherDto.temperature(), actualWeatherDto.temperature());
    }

    @Test
    void getWeatherForCityByGeolocationReturnException() throws Exception {
        //Given
        String wrongCityName = "krakow";
        Double wrongLat = 9999999.99999;
        Double wrongLon = 9999999.99999;
        when(restTemplate.getForObject(anyString(), any(), anyDouble(), anyDouble())).thenReturn(null);
        //When
        //Then
        assertThrows(Exception.class, () -> weatherService.getWeatherForCityByGeolocation(wrongLat, wrongLon, wrongCityName));
    }
}