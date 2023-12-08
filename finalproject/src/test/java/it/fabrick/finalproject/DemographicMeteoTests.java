package it.fabrick.finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.fabrick.finalproject.dto.CityResponseDto;
import it.fabrick.finalproject.dto.MunicipalityResponseDto;
import it.fabrick.finalproject.dto.WeatherDailyResponseDto;
import it.fabrick.finalproject.enumeration.ErrorCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DemographicMeteoTests {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Value("classpath:data/WeatherDailyResponseStub.json")
    Resource weatherDailyResponseStub;

    @Test
    void shouldReadZeroCitiesByNumberOfPopulationGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/cities")
                        .param("population number", "9999999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(0)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<CityResponseDto> cities = objectMapper.readValue(content, new TypeReference<>() {
        });
        cities.forEach(cityResponseDto ->
                Assertions.assertNull(cityResponseDto.getId()));
    }

    @Test
    void shouldReadCitiesByNumberOfPopulationGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/cities")
                        .param("population number", "431656"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(14)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<CityResponseDto> cities = objectMapper.readValue(content, new TypeReference<>() {
        });
        cities.forEach(cityResponseDto ->
                Assertions.assertNotNull(cityResponseDto.getId()));
    }

    @Test
    void shouldReadMunicipalitiesByRegionAndNumberOfResidentGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/municipalities/filter-region-residents")
                        .param("resident number", "2638841")
                        .param("region name", "lazio"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(1)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<MunicipalityResponseDto> municipalities = objectMapper.readValue(content, new TypeReference<>() {
        });
        municipalities.forEach(municipalityResponseDto ->
                Assertions.assertNotNull(municipalityResponseDto.getId()));
    }

    @Test
    void shouldReadZeroMunicipalitiesByRegionAndNumberOfResidentGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/municipalities/filter-region-residents")
                        .param("resident number", "2638842")
                        .param("region name", "lazio"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(0)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<MunicipalityResponseDto> municipalities = objectMapper.readValue(content, new TypeReference<>() {
        });
        municipalities.forEach(municipalityResponseDto ->
                Assertions.assertNull(municipalityResponseDto.getId()));
    }

    @Test
    void shouldReadMunicipalitiesByProvinceAndNumberOfResidentGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/municipalities/filter-province-residents")
                        .param("resident number", "41234")
                        .param("province name", "milano"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(7)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<MunicipalityResponseDto> municipalities = objectMapper.readValue(content, new TypeReference<>() {
        });
        municipalities.forEach(municipalityResponseDto ->
                Assertions.assertNotNull(municipalityResponseDto.getId()));
    }

    @Test
    void shouldReadZeroMunicipalitiesByProvinceAndNumberOfResidentGreaterThan() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/municipalities/filter-province-residents")
                        .param("resident number", "9999999")
                        .param("province name", "milano"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(0)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<MunicipalityResponseDto> municipalities = objectMapper.readValue(content, new TypeReference<>() {
        });
        municipalities.forEach(municipalityResponseDto ->
                Assertions.assertNull(municipalityResponseDto.getId()));
    }

    @Test
    void shouldGenerateBadRequestForValidateIntegerParsing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/demographic-meteo/municipalities/filter-province-residents")
                        .param("resident number", "NOTANUMBER")
                        .param("province name", "milano"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode", Matchers.is(ErrorCode.DATA_NOT_VALID.toString())));
    }


    void shouldReadCityWeather() throws Exception {

        WeatherDailyResponseDto stub = weatherDailyResponseDto();


    }

    private WeatherDailyResponseDto weatherDailyResponseDto() throws Exception {
        String content = StreamUtils.copyToString(weatherDailyResponseStub.getInputStream(), Charset.defaultCharset());
        return objectMapper.readValue(content, WeatherDailyResponseDto.class);
    }
}
