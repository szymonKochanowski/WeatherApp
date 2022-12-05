package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.entity.Weather;
import com.kohan.WeatherApp.mapper.WeatherMapper;
import com.kohan.WeatherApp.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
@Slf4j
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherMapper weatherMapper;

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "d36b0893439f6ed9ed2f452508ffeed8";

    private RestTemplate restTemplate = new RestTemplate();


    public WeatherDto getWeatherForCityByGeolocation(Double lat, Double lon, String cityName) throws Exception {
        log.info("Start to get weather for city with lat: {}, lon {}, city name: {}", lat, lon, cityName);
        try{
            String response = restTemplate.getForObject(WEATHER_URL + "?lat={lat}&lon={lon}&appid=" + API_KEY + "&units=metric", String.class, lat, lon);
            JSONObject jsonObject = new JSONObject(response);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Weather weather = new Weather();
            weather.setDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            weather.setCity(cityName);
            weather.setCountry(jsonObject.getJSONObject("sys").getString("country"));
            weather.setTimezone(jsonObject.getInt("timezone"));
            weather.setTemperature(jsonObject.getJSONObject("main").getDouble("temp"));
            weather.setTemperatureMax(jsonObject.getJSONObject("main").getDouble("temp_max"));
            weather.setTemperatureMin(jsonObject.getJSONObject("main").getDouble("temp_min"));
            weather.setWindSpeed(jsonObject.getJSONObject("wind").getInt("speed"));
            weather.setHumidity(jsonObject.getJSONObject("main").getInt("humidity"));
            weather.setPressure(jsonObject.getJSONObject("main").getInt("pressure"));
            weather.setClouds(jsonObject.getJSONObject("clouds").getInt("all"));
            weather.setDateTime(timestamp);
            weatherRepository.save(weather);

            WeatherDto weatherDto = weatherMapper.weatherToWeatherDto(weather);
            log.info(weatherDto.toString());
            return weatherDto;

        } catch (Exception e) {
            log.error("Error in method getWeatherForCityByGeolocation!");
            throw new Exception(e.getMessage());
        }
    }
}
