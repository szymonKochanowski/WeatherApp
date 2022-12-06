package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.entity.Geolocation;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.mapper.GeolocationMapper;
import com.kohan.WeatherApp.repository.GeolocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GeolocationServiceTest {

    @InjectMocks
    private GeolocationService geolocationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GeolocationMapper geolocationMapper;

    @Mock
    private GeolocationRepository geolocationRepository;

    @Test
    void getGeolocationDtoByCityName() throws GeolocationNotFoundException {
        //Given
        String cityName = "krakow";
        Geolocation geolocation = new Geolocation(1, "krakow", "PL", 50.072136,  19.947226);
        GeolocationDto expectedGeolocationDto = new GeolocationDto("krakow", "PL", 50.072136,  19.947226);
        Geolocation[] geolocations = new Geolocation[1];
        geolocations[0] = geolocation;

        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(geolocations);
        when(geolocationRepository.findByCity(cityName)).thenReturn(Collections.emptyList());
        when(geolocationRepository.save(geolocation)).thenReturn(geolocation);
        when(geolocationMapper.geolocationToGeolocationDto(any())).thenReturn(expectedGeolocationDto);

        //When
        GeolocationDto actualGeolocationDto = geolocationService.getGeolocationDtoByCityName(cityName);

        //Then
        assertEquals(expectedGeolocationDto.city(), actualGeolocationDto.city());
        assertEquals(expectedGeolocationDto.country(), actualGeolocationDto.country());
        assertEquals(expectedGeolocationDto.lon(), actualGeolocationDto.lon());
        assertEquals(expectedGeolocationDto.lat(), actualGeolocationDto.lat());
    }

    @Test
    void getGeolocationDtoByCityNameReturnGeolocationNotFoundException() throws GeolocationNotFoundException {
        //Given
        String wrongCityName = "wrongCityName";
        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(null);
        //When
        //Then
        assertThrows(GeolocationNotFoundException.class, ()-> geolocationService.getGeolocationDtoByCityName(wrongCityName),
                "Not found geolocation for city with name: 'krakow'!" );
    }

}