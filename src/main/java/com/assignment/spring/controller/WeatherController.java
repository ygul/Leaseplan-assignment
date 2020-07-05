package com.assignment.spring.controller;

import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherEntity> weather(@RequestParam String city) {
        if (Objects.isNull(city)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        WeatherEntity weatherEntity = weatherService.getWeather(city);

        return ResponseEntity.status(HttpStatus.OK).body(weatherEntity);
    }
}
