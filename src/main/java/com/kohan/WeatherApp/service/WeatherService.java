package com.kohan.WeatherApp.service;

import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.entity.Weather;
import com.kohan.WeatherApp.mapper.WeatherMapper;
import com.kohan.WeatherApp.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${test.api_key}")
    private String api_key;

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private RestTemplate restTemplate = new RestTemplate();


    public WeatherDto getWeatherForCityByGeolocation(Double latitude, Double longitude, String cityName) throws Exception {
        log.info("Start to get weather for city with latitude: {}, longitude {}, city name: {}", latitude, longitude, cityName);
        try{
            String weatherString = restTemplate.getForObject(WEATHER_URL + "?lat={latitude}&lon={longitude}&appid=" + api_key + "&units=metric", String.class, latitude, longitude);
            if (weatherString == null) {
                log.error("Error with external API responsible for get weather!");
            }
            log.info("Response: " + weatherString);
            JSONObject jsonObject = new JSONObject(weatherString);
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
            weather.setLatitude(latitude);
            weather.setLongitude(longitude);

            WeatherDto weatherDto = weatherMapper.weatherToWeatherDto(weather);
            log.info(weatherDto.toString());
            return weatherDto;

        } catch (Exception e) {
            log.error("Error in method getWeatherForCityByGeolocation!");
            throw new Exception(e.getMessage());
        }
    }

    public WeatherDto addNewWeather(WeatherDto weatherDto) throws Exception {
        log.info("Start to add new weather: " + weatherDto);
        try{
            Weather weather = weatherMapper.weatherDtoToWeather(weatherDto);
            weatherRepository.save(weather);
            return weatherDto;
        } catch (Exception e) {
            log.error("Error in method addNewWeather!");
            throw new Exception(e.getMessage());
        }
    }
}
