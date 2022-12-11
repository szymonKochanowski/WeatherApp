package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.entity.Geolocation;
import com.kohan.WeatherApp.exception.GeolocationNotFoundException;
import com.kohan.WeatherApp.mapper.GeolocationMapper;
import com.kohan.WeatherApp.repository.GeolocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;

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

    public GeolocationDto getGeolocationDtoByCityName(String cityName) throws GeolocationNotFoundException {
        log.info("Start to get geolocation for city with name: " + cityName);
        try{
            String geolocationString = restTemplate.getForObject(GEOLOCATION_URL + "?q={cityName}&appid=" + api_key, String.class, cityName);
            if (geolocationString == null) {
                log.error("Error with external API responsible for get geolocation!");
            }
            log.info("Response: " + geolocationString);
            String substring = geolocationString.substring(1, (geolocationString.length() - 1));

            JSONObject jsonObject = new JSONObject(substring);
            Geolocation geolocation = new Geolocation();
            geolocation.setCountry(jsonObject.getString("country"));
            geolocation.setCity(cityName);
            geolocation.setLatitude(jsonObject.getDouble("lat"));
            geolocation.setLongitude(jsonObject.getDouble("lon"));
            return geolocationMapper.geolocationToGeolocationDto(geolocation);
        } catch (Exception e) {
            log.error("Error in method getGeolocationDtoByCityName");
            throw new GeolocationNotFoundException("Not found geolocation for city with name: '" + cityName + "'!");
        }
    }

    public GeolocationDto addNewGeolocation(GeolocationDto geolocationDto) throws Exception {
        log.info("Start to add new geolocation for city: " + geolocationDto.city());
        try {
            Geolocation geolocation = new Geolocation();
            geolocation.setCity(geolocationDto.city());
            geolocation.setCountry(geolocationDto.country());
            geolocation.setLatitude(geolocationDto.latitude());
            geolocation.setLongitude(geolocationDto.longitude());
            geolocationRepository.save(geolocation);
            return geolocationMapper.geolocationToGeolocationDto(geolocation);
        } catch (Exception e) {
            log.error("Error in method addNewGeolocation!");
            throw new Exception(e.getMessage());
        }
    }
}
