package com.assignment.spring.controller;

import com.assignment.spring.exception.CityNotFoundException;
import com.assignment.spring.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestExceptionHandlerIT {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testNotFoundExceptionHandler() throws Exception {
        String city = "amsterdam";
        String errorMessage = "{\"message\":\"City: " + city + " not found\"}";

        when(weatherService.getWeather(city)).thenThrow(new CityNotFoundException("City: amsterdam not found"));
        this.mockMvc.perform(get("/weather?city=" + city))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testAnyUnknownRuntimeException() throws Exception {
        String city = "a city";
        String errorMessage = "{\"message\":\"General error\"}";

        when(weatherService.getWeather(city)).thenThrow(new RuntimeException("A unknown and unhandled runtime exception in application"));

        this.mockMvc.perform(get("/weather?city=" + city))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }
}
