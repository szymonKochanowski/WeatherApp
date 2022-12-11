package com.kohan.WeatherApp.dto;

import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record WeatherDto(
        @NotNull String description,
        @NotNull String city,
        @NotNull String country,
        @NotNull int timezone,
        @NotNull double temperature,
        @NotNull double temperatureMin,
        @NotNull double temperatureMax,
        @NotNull double windSpeed,
        @NotNull int pressure,
        @NotNull int humidity,
        @NotNull int clouds,
        @NotNull Timestamp dateTime,
        @NotNull Double latitude,
        @NotNull Double longitude
) {
}
