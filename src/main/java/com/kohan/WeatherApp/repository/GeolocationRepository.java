package com.kohan.WeatherApp.repository;

import com.kohan.WeatherApp.entity.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeolocationRepository extends JpaRepository<Geolocation, Integer> {

    List<Geolocation> findByCity(String cityName);
}
