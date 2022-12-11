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
import org.springframework.web.client.HttpServerErrorException;
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
        GeolocationDto expectedGeolocationDto = new GeolocationDto("krakow", "PL", 50.072136,  19.947226);
        String geolocationResponse = "[{\"name\":\"Krakow\",\"local_names\":{\"eu\":\"Krakovia\",\"la\":\"Cracovia\",\"mk\":\"Краков\",\"ro\":\"Cracovia\",\"eo\":\"Krakovo\",\"lt\":\"Krokuva\",\"ja\":\"クラクフ\",\"fa\":\"کراکوف\",\"mt\":\"Krakovja\",\"ca\":\"Cracòvia\",\"zh\":\"克拉科夫\",\"it\":\"Cracovia\",\"fr\":\"Cracovie\",\"pt\":\"Cracóvia\",\"el\":\"Κρακοβία\",\"sr\":\"Краков\",\"en\":\"Krakow\",\"fi\":\"Krakova\",\"lv\":\"Krakova\",\"sl\":\"Krakov\",\"nl\":\"Krakau\",\"sk\":\"Krakov\",\"ar\":\"كراكوف\",\"hr\":\"Krakov\",\"hu\":\"Krakkó\",\"uk\":\"Краків\",\"pl\":\"Kraków\",\"de\":\"Krakau\",\"be\":\"Кракаў\",\"ru\":\"Краков\",\"es\":\"Cracovia\",\"cs\":\"Krakov\"},\"lat\":50.0619474,\"lon\":19.9368564,\"country\":\"PL\",\"state\":\"Lesser Poland Voivodeship\"}]";

        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(geolocationResponse);
        when(geolocationMapper.geolocationToGeolocationDto(any())).thenReturn(expectedGeolocationDto);

        //When
        GeolocationDto actualGeolocationDto = geolocationService.getGeolocationDtoByCityName(cityName);

        //Then
        assertEquals(expectedGeolocationDto.city(), actualGeolocationDto.city());
        assertEquals(expectedGeolocationDto.country(), actualGeolocationDto.country());
        assertEquals(expectedGeolocationDto.longitude(), actualGeolocationDto.longitude());
        assertEquals(expectedGeolocationDto.latitude(), actualGeolocationDto.latitude());
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

    @Test
    void addNewGeolocation() throws Exception {
        //Given
        Geolocation geolocation = new Geolocation(1, "krakow", "PL", 50.072136,  19.947226);
        GeolocationDto expectedGeolocationDto = new GeolocationDto("krakow", "PL", 50.072136,  19.947226);
        when(geolocationRepository.save(any())).thenReturn(geolocation);
        when(geolocationMapper.geolocationToGeolocationDto(any())).thenReturn(expectedGeolocationDto);
        //When
        GeolocationDto actualGeolocationDto = geolocationService.addNewGeolocation(expectedGeolocationDto);
        //Then
        assertEquals(expectedGeolocationDto.city(), actualGeolocationDto.city());
    }

    @Test
    void addNewGeolocationReturnException() throws Exception {
        //Given
        GeolocationDto expectedGeolocationDto = new GeolocationDto("krakow", "PL", 50.072136,  19.947226);
        when(geolocationRepository.save(any())).thenReturn(null);
        when(geolocationMapper.geolocationToGeolocationDto(any())).thenThrow(HttpServerErrorException.InternalServerError.class);
        //When
        //Then
        assertThrows(Exception.class, () -> geolocationService.addNewGeolocation(expectedGeolocationDto));
    }

}