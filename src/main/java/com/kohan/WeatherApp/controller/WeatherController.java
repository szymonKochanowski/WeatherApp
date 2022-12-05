package com.kohan.WeatherApp.controller;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import com.kohan.WeatherApp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private GeolocationService geolocationService;

    @GetMapping("/")
    public ResponseEntity<WeatherDto> getCurrentWeatherForCityByCityName(@RequestParam String cityName) {
        log.info("Start to get weather for city with name: " + cityName);
        try{
            GeolocationDto geolocationDto = geolocationService.getGeolocationDtoByCityName(cityName);
            return ResponseEntity.ok(weatherService.getWeatherForCityByGeolocation(geolocationDto.getLat(), geolocationDto.getLon(), cityName));
        } catch (GeolocationNotFoundException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
