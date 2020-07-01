package com.assignment.spring.controller;


import com.assignment.spring.domain.WeatherEntity;
import com.assignment.spring.repo.WeatherRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();


    @Before
    public void cleanInMemoryDb() {
        weatherRepository.deleteAll();
    }

    @Test
    public void testGetWeather() throws Exception {
        String city = "Amsterdam";
        String country = "NL";
        String temperature = "44.4";

        String responseForAmsterdam = generateWeatherResponse(city, country, temperature);

        stubFor(WireMock.get(urlMatching("/data/2.5/weather\\?q=amsterdam&APPID=anApi"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseForAmsterdam)));


        this.mockMvc.perform(get("/weather?city=amsterdam"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(city))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(country))
                .andExpect(MockMvcResultMatchers.jsonPath("$.temperature").exists());

        List<WeatherEntity> weatherEntityList = weatherRepository.findAll();

        assertThat(weatherEntityList.size()).isEqualTo(1);
    }

    @Test
    public void testGetWeatherMultipleTimes() throws Exception {
        String cityOne = "Amsterdam";
        String cityTwo = "Deventer";
        String country = "NL";
        String temperatureCityOne = "44.4";
        String temperatureCityTwo = "33.4";


        String responseForAmsterdam = generateWeatherResponse(cityOne, country, temperatureCityOne);

        stubFor(WireMock.get(urlMatching("/data/2.5/weather\\?q=amsterdam&APPID=anApi"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseForAmsterdam)));


        String responseForDeventer = generateWeatherResponse(cityTwo, country, temperatureCityTwo);

        stubFor(WireMock.get(urlMatching("/data/2.5/weather\\?q=deventer&APPID=anApi"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseForDeventer)));

        this.mockMvc.perform(get("/weather?city=amsterdam"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(cityOne))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(country))
                .andExpect(MockMvcResultMatchers.jsonPath("$.temperature").value(temperatureCityOne));

        this.mockMvc.perform(get("/weather?city=deventer"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(cityTwo))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(country))
                .andExpect(MockMvcResultMatchers.jsonPath("$.temperature").value(temperatureCityTwo));

        List<WeatherEntity> weatherEntityList = weatherRepository.findAll();

        assertThat(weatherEntityList.size()).isEqualTo(2);
    }

    @Test
    public void testGetWeatherFails() throws Exception {
        stubFor(WireMock.get(urlMatching("/data/2.5/weather\\?q=xxx&APPID=anApi"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")));

        this.mockMvc.perform(get("/weather?city=xxx"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testGetWeatherReturnsBadRequest() throws Exception {
        this.mockMvc.perform(get("/weather"))
                .andExpect(status().isBadRequest());
    }


    private String generateWeatherResponse(String city, String country, String temperature) {
        String template = "{\"coord\":" +
                "{\"lon\":4.89,\"lat\":52.37}," +
                "\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}]," +
                "\"base\":\"stations\"," +
                "\"main\":{\"temp\":{temperature},\"feels_like\":291.92,\"temp_min\":294.82,\"temp_max\":297.59,\"pressure\":1017,\"humidity\":53}," +
                "\"visibility\":10000," +
                "\"wind\":{\"speed\":7.7,\"deg\":210}," +
                "\"clouds\":{\"all\":40}," +
                "\"dt\":1592744813," +
                "\"sys\":{\"type\":1,\"id\":1524,\"country\":\"{country}\",\"sunrise\":1592709488,\"sunset\":1592769984}," +
                "\"timezone\":7200," +
                "\"id\":2759794," +
                "\"name\":\"{city}\"," +
                "\"cod\":200}";

        return template.replace("{city}", city).replace("{country}", country).replace("{temperature}", temperature);
    }

}
