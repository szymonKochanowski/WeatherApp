package com.kohan.WeatherApp.controller;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.service.GeolocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/geolocation")
public class GeolocationController {

    @Autowired
    private GeolocationService geolocationService;

    @GetMapping("/")
    public ResponseEntity<GeolocationDto> getGeolocationByCityName(@RequestParam String cityName) {
        log.info("Start to get geolocation for city: " + cityName);
        try {
            return ResponseEntity.ok(geolocationService.getGeolocationDtoByCityName(cityName));
        } catch (GeolocationNotFoundException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GeolocationDto> addGeolocation(@RequestBody GeolocationDto geolocationDto) {
        log.info("Start to add geolocation for city: " + geolocationDto.city());
        try {
            return new ResponseEntity(geolocationService.addNewGeolocation(geolocationDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
