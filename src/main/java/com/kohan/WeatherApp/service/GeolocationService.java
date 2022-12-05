package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.entity.Geolocation;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.mapper.GeolocationMapper;
import com.kohan.WeatherApp.repository.GeolocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GeolocationService {

    @Autowired
    private GeolocationMapper geolocationMapper;

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Value("${test.api_key}")
    private String api_key;

    private static final String GEOLOCATION_URL = "http://api.openweathermap.org/geo/1.0/direct";

    private RestTemplate restTemplate = new RestTemplate();

    public GeolocationDto getGeolocationDtoByCityName(String cityName) {
        log.info("Start to get geolocation for city with name: " + cityName);
        try{
            Geolocation[] objects = restTemplate.getForObject(GEOLOCATION_URL + "?q={cityName}&appid=" + api_key, Geolocation[].class, cityName);
            log.info(objects.toString());
            List<Geolocation> geolocationList = Arrays.asList(objects);

            Geolocation geolocation = new Geolocation();
            geolocation.setCountry(geolocationList.get(0).getCountry());
            geolocation.setCity(cityName);
            geolocation.setLat(geolocationList.get(0).getLat());
            geolocation.setLon(geolocationList.get(0).getLon());

            if (geolocationRepository.findByCity(cityName).isEmpty()){
                geolocationRepository.save(geolocation);
            }

            GeolocationDto geolocationDto = geolocationMapper.geolocationToGeolocationDto(geolocation);
            log.info(geolocationDto.toString());
            return geolocationDto;

        } catch (Exception e) {
            log.error("Error in method getGeolocationDtoByCityName");
            throw new GeolocationNotFoundException("Not found geolocation for city with name: '" + cityName + "'!");
        }
    }
}
