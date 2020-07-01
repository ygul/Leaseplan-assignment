package com.assignment.spring.service;

import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.domain.WeatherResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MapperService {

    public WeatherEntity mapToWeatherEntity(WeatherResponse response) {
        if (Objects.isNull(response)) {
            throw new IllegalArgumentException("Provided weather response object is null");
        }

        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());

        if (Objects.nonNull(response.getSys())) {
            entity.setCountry(response.getSys().getCountry());
        }

        if (Objects.nonNull(response.getMain())) {
            entity.setTemperature(response.getMain().getTemp());
        }
        return entity;
    }
}


