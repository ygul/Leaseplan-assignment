package com.assignment.spring.service;

import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.domain.WeatherResponse;
import com.assignment.spring.exception.CityNotFoundException;
import com.assignment.spring.repo.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    private RestTemplate restTemplate;
    private WeatherRepository weatherRepository;
    private MapperService mapperService;
    private String apiKey;
    private String openWeatherUrl;

    public WeatherService(RestTemplate restTemplate,
                          WeatherRepository weatherRepository,
                          MapperService mapperService,
                          @Value("${openweather.api-key}") String apiKey,
                          @Value("${openweather.url}") String openWeatherUrl) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        this.mapperService = mapperService;
        this.apiKey = apiKey;
        this.openWeatherUrl = openWeatherUrl;
    }

    public WeatherEntity getWeather(String city) {
        String url = openWeatherUrl.replace("{city}", city).replace("{appid}", apiKey);
        ResponseEntity<WeatherResponse> response;

        try {
            response = restTemplate.getForEntity(url, WeatherResponse.class);
        } catch (HttpClientErrorException exp) {
            log.warn("Request to get stats about {} failed", city);
            throw new CityNotFoundException(String.format("City %s not found", city));
        }

        WeatherResponse weatherResponse = response.getBody();
        WeatherEntity weatherEntity = mapperService.mapToWeatherEntity(weatherResponse);
        weatherRepository.save(weatherEntity);
        return weatherEntity;
    }

}
