package com.kohan.WeatherApp.dto;

import java.sql.Timestamp;

public record WeatherDto(
        String description,
        String city,
        String country,
        int timezone,
        double temperature,
        double temperatureMin,
        double temperatureMax,
        double windSpeed,
        int pressure,
        int humidity,
        int clouds,
        Timestamp dateTime
) {
}
