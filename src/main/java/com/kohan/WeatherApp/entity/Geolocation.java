package com.kohan.WeatherApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "geolocation")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Geolocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
}
