package com.kohan.WeatherApp.dto;

import jakarta.validation.constraints.NotNull;

public record GeolocationDto(
        @NotNull  String city,
        @NotNull String country,
        @NotNull Double lat,
        @NotNull Double lon) {
}
