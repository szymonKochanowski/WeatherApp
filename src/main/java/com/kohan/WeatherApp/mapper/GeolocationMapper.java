package com.kohan.WeatherApp.mapper;

import com.kohan.WeatherApp.dto.GeolocationDto;
import com.kohan.WeatherApp.entity.Geolocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeolocationMapper {

    GeolocationDto geolocationToGeolocationDto(Geolocation geolocation);

}
