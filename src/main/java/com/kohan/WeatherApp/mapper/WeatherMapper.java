package com.kohan.WeatherApp.mapper;

import com.kohan.WeatherApp.dto.WeatherDto;
import com.kohan.WeatherApp.entity.Weather;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDto weatherToWeatherDto(Weather weather);

    Weather weatherDtoToWeather(WeatherDto weatherDto);

}
