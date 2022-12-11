package com.kohan.WeatherApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.entity.Geolocation;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GeolocationControllerTest {

    @Mock
    private GeolocationService geolocationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getGeolocationByCityName() throws Exception {
        //Given
        String cityName = "krakow";
        Geolocation geolocation = new Geolocation(1, cityName, "PL", 50.0619474,  19.9368564);
        GeolocationDto expectedGeolocationDto = new GeolocationDto(cityName, "PL", 50.0619474,  19.9368564);
        when(geolocationService.getGeolocationDtoByCityName(cityName)).thenReturn(expectedGeolocationDto);
        //When
        MvcResult mvcResult = mockMvc.perform(get("/geolocation/")
                        .param("cityName", cityName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        //Then
        GeolocationDto actualGeolocationDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GeolocationDto.class);
        assertEquals(expectedGeolocationDto, actualGeolocationDto);
    }

    @Test
    void getGeolocationByCityNameReturnGeolocationNotFoundException() throws Exception {
        //Given
        String wrongCityName = "wrongCityName";
        GeolocationDto expectedGeolocationDto = new GeolocationDto(wrongCityName, "PL", 50.0619474,  19.9368564);
        when(geolocationService.getGeolocationDtoByCityName(wrongCityName)).thenThrow(GeolocationNotFoundException.class);
        //When
        mockMvc.perform(get("/geolocation/")
                        .param("cityName", wrongCityName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getGeolocationByCityNameReturnExceptionn() throws Exception {
        //Given
        String wrongCityName = "wrongCityName";
        GeolocationDto expectedGeolocationDto = new GeolocationDto(wrongCityName, "PL", 50.0619474,  19.9368564);
        when(geolocationService.getGeolocationDtoByCityName(wrongCityName)).thenThrow(HttpServerErrorException.InternalServerError.class);
        //When
        mockMvc.perform(get("/geolocation/")
                        .param("cityName", wrongCityName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void addGeolocation() {
//        //Given
//        String cityName = "krakow";
//        Geolocation geolocation = new Geolocation(1, cityName, "PL", 50.0619474,  19.9368564);
//        GeolocationDto expectedGeolocationDto = new GeolocationDto(cityName, "PL", 50.0619474,  19.9368564);
//        when(geolocationService.getGeolocationDtoByCityName(cityName)).thenReturn(expectedGeolocationDto);
//        //When
//        MvcResult mvcResult = mockMvc.perform(get("/geolocation/")
//                        .param("cityName", cityName))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn();
//        //Then
//        GeolocationDto actualGeolocationDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GeolocationDto.class);
//        assertEquals(expectedGeolocationDto, actualGeolocationDto);
    }
}