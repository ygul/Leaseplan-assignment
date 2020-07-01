package com.assignment.spring.service;

import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.domain.Main;
import com.assignment.spring.domain.Sys;
import com.assignment.spring.domain.WeatherResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperServiceTest {

    private MapperService mapperService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        mapperService = new MapperService();
    }

    @Test
    public void testMapToWeatherEntityShouldSucceed() {
        String country = "Netherlands";
        String city = "Amsterdam";
        double temp = 23.1;

        WeatherResponse weatherResponse = new WeatherResponse();
        Sys sys = new Sys();
        Main main = new Main();

        main.setTemp(temp);
        sys.setCountry(country);
        weatherResponse.setName(city);
        weatherResponse.setSys(sys);
        weatherResponse.setMain(main);

        WeatherEntity weatherEntity = mapperService.mapToWeatherEntity(weatherResponse);

        assertThat(weatherEntity.getCity()).isEqualTo(city);
        assertThat(weatherEntity.getCountry()).isEqualTo(country);
        assertThat(weatherEntity.getTemperature()).isEqualTo(temp);
    }

    @Test
    public void testMapToWeatherEntityOnlyCityProvided() {
        String city = "Amsterdam";

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(city);
        WeatherEntity weatherEntity = mapperService.mapToWeatherEntity(weatherResponse);

        assertThat(weatherEntity.getCity()).isEqualTo(city);
        assertThat(weatherEntity.getCountry()).isNullOrEmpty();
        assertThat(weatherEntity.getTemperature()).isNull();
    }

    @Test
    public void testMapToWeatherEntityShouldFail() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Provided weather response object is null");
        mapperService.mapToWeatherEntity(null);
    }


}

