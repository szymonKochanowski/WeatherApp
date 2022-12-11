package com.kohan.WeatherApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.entity.Geolocation;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andDo(print())
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
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void addGeolocation() throws Exception {
        //Given
        String cityName = "krakow";
        GeolocationDto expectedGeolocationDto = new GeolocationDto(cityName, "PL", 50.0619474,  19.9368564);
        when(geolocationService.addNewGeolocation(expectedGeolocationDto)).thenReturn(expectedGeolocationDto);
        //When
        MvcResult mvcResult = mockMvc.perform(post("/geolocation/add")
                        .content(objectMapper.writeValueAsString(expectedGeolocationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        GeolocationDto actualGeolocationDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GeolocationDto.class);
        assertEquals(expectedGeolocationDto, actualGeolocationDto);
    }

    @Test
    void addGeolocationReturnException() throws Exception {
        //Given
        String wrongCityName = "wrongCityName";
        GeolocationDto expectedGeolocationDto = new GeolocationDto(wrongCityName, "PL", 50.0619474,  19.9368564);
        when(geolocationService.addNewGeolocation(any())).thenReturn(null);
        //When
        mockMvc.perform(post("/geolocation/add")
                        .content(objectMapper.writeValueAsString(expectedGeolocationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}