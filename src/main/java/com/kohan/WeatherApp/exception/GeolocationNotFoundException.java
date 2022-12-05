package com.kohan.WeatherApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeolocationNotFoundException extends RuntimeException {

    public GeolocationNotFoundException(String message) {
        super(message);
    }
}
