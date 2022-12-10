package com.kohan.WeatherApp.controller;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import com.kohan.WeatherApp.service.WeatherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;

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
            return ResponseEntity.ok(weatherService.getWeatherForCityByGeolocation(geolocationDto.latitude(), geolocationDto.longitude(), cityName));
        } catch (ServiceUnavailableException exc) {
            return new ResponseEntity(exc.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (GeolocationNotFoundException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<WeatherDto> addNewWeather(@Valid @RequestBody WeatherDto weatherDto) throws Exception {
        log.info("Start to add new weather: " + weatherDto);
        try{
            geolocationService.addNewGeolocationBasedOnWeather(weatherDto);
            return new ResponseEntity(weatherService.addNewWeather(weatherDto), HttpStatus.CREATED) ;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
