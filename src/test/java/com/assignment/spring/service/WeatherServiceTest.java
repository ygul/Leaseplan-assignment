package com.assignment.spring.service;

import com.assignment.spring.domain.Main;
import com.assignment.spring.domain.Sys;
import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.domain.WeatherResponse;
import com.assignment.spring.exception.CityNotFoundException;
import com.assignment.spring.repo.WeatherRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private WeatherRepository weatherRepositoryMock;

    @Mock
    private MapperService mapperServiceMock;

    private final String apiKey = "anApiKey";
    private final String openWeatherUrl = "http://localhost:8080/data/2.5/weather?q={city}&APPID={appid}";

    private WeatherService weatherService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        weatherService = new WeatherService(restTemplateMock, weatherRepositoryMock, mapperServiceMock, apiKey, openWeatherUrl);
    }

    @Test
    public void testGetWeather() {
        String city = "Amsterdam";
        String country = "NL";
        double temperature = 444.0;

        WeatherResponse weatherResponse = new WeatherResponse();
        Sys sys = new Sys();
        sys.setCountry(country);
        Main main = new Main();
        main.setTemp(temperature);

        weatherResponse.setName(city);
        weatherResponse.setMain(main);
        weatherResponse.setSys(sys);

        ResponseEntity<WeatherResponse> responseResponseEntity = new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setCity(city);
        weatherEntity.setCountry(country);
        weatherEntity.setTemperature(temperature);

        String url = openWeatherUrl.replace("{city}", city).replace("{appid}", apiKey);
        when(restTemplateMock.getForEntity(url, WeatherResponse.class)).thenReturn(responseResponseEntity);
        when(mapperServiceMock.mapToWeatherEntity(weatherResponse)).thenReturn(weatherEntity);

        WeatherEntity actualWeather = weatherService.getWeather(city);

        assertThat(actualWeather.getCity()).isEqualTo(city);
        assertThat(actualWeather.getCountry()).isEqualTo(country);
        assertThat(actualWeather.getTemperature()).isEqualTo(temperature);

        verify(weatherRepositoryMock, timeout(1)).save(actualWeather);
        verify(mapperServiceMock, times(1)).mapToWeatherEntity(weatherResponse);
        verify(weatherRepositoryMock, times(1)).save(weatherEntity);
    }

    @Test
    public void testGetWeatherReturnsCityNotFound() {
        String city = "xxx";
        String url = openWeatherUrl.replace("{city}", city).replace("{appid}", apiKey);
        ResponseEntity<WeatherResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        when(restTemplateMock.getForEntity(url, WeatherResponse.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        exceptionRule.expect(CityNotFoundException.class);
        exceptionRule.expectMessage("City xxx not found");
        weatherService.getWeather(city);
    }


}
