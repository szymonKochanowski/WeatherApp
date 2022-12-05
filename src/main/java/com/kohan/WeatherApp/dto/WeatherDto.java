package com.kohan.WeatherApp.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {

    private String description;
    private String city;
    private String country;
    private int timezone;
    private double temperature;

    @Column(name = "temperature_min")
    private double temperatureMin;

    @Column(name = "temperature_max")
    private double temperatureMax;

    @Column(name = "wind_speed")
    private double windSpeed;
    private int pressure;
    private int humidity;
    private int clouds;

    @Column(name = "date_time")
    private Timestamp dateTime;
}
