package com.kohan.WeatherApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import com.kohan.WeatherApp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private GeolocationService geolocationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getCurrentWeatherForCityByCityName() throws Exception {
        //Given
        String cityName = "Krakow";
        Double lat = 50.072136;
        Double lon = 19.947226;
        GeolocationDto geolocationDto = new GeolocationDto(cityName, "PL", lat, lon);
        WeatherDto expectedWeatherDto = new WeatherDto("Smog", cityName, "PL", 3600, 2.65, -2.1, 4.4, 12.2, 1004, 946, 90, Timestamp.valueOf(LocalDateTime.now()));
        when(geolocationService.getGeolocationDtoByCityName(cityName)).thenReturn(geolocationDto);
        when(weatherService.getWeatherForCityByGeolocation(lat, lon, cityName)).thenReturn(expectedWeatherDto);
        //When
        MvcResult result = mockMvc.perform(get("/weather/")
                        .param("cityName", cityName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //Then
        WeatherDto actualWeatherDto = objectMapper.readValue(result.getResponse().getContentAsString(), WeatherDto.class);
        assertEquals(expectedWeatherDto.city(), actualWeatherDto.city());
        assertEquals(expectedWeatherDto.country(), actualWeatherDto.country());
        assertEquals(expectedWeatherDto.temperature(), actualWeatherDto.temperature());
    }

    @Test
    void getCurrentWeatherForCityByCityNameReturnGeolocationNotFoundException() throws Exception {
        //Given
        String cityName = "Krakow";
        when(geolocationService.getGeolocationDtoByCityName(cityName)).thenThrow(GeolocationNotFoundException.class);
        //When
        mockMvc.perform(get("/weather/")
                        .param("cityName", cityName))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getCurrentWeatherForCityByCityNameReturnException() throws Exception {
        //Given
        String cityName = "Krakow";
        Double lat = 50.072136;
        Double lon = 19.947226;
        GeolocationDto geolocationDto = new GeolocationDto(cityName, "PL", lat, lon);
        when(geolocationService.getGeolocationDtoByCityName(cityName)).thenReturn(geolocationDto);
        when(weatherService.getWeatherForCityByGeolocation(lat, lon, cityName)).thenThrow(Exception.class);
        //When
        mockMvc.perform(get("/weather/")
                        .param("cityName", cityName))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}