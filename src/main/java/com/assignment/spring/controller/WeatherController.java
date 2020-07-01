package com.assignment.spring.controller;

import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/weather")
    public ResponseEntity<WeatherEntity> weather(HttpServletRequest request) {
        String city = request.getParameter("city");

        if (Objects.isNull(city)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        WeatherEntity weatherEntity = weatherService.getWeather(city);

        return ResponseEntity.status(HttpStatus.OK).body(weatherEntity);
    }


}
