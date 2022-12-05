package com.kohan.WeatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeolocationDto {

    private String city;
    private String country;
    private Double lat;
    private Double lon;
}
