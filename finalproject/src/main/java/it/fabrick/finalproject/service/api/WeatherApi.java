package it.fabrick.finalproject.service.api;


import it.fabrick.finalproject.dto.WeatherResponseClientDto;
import it.fabrick.finalproject.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class WeatherApi {

    private final static String WMO = "weather_code";
    private final static String TEMP = "temperature_2m";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String DAILY = "daily";
    private final static String HOURLY = "hourly";
    private final static String FORECAST_DAYS = "forecast_days";
    private final static String TIME_ZONE = "timezone"; // per daily request
    private final static String AUTO = "auto";
    private final static String CEST = "Europe/Berlin";
    private final static String START_DATE = "start_date";
    private final static String END_DATE = "end_date";


    private final RestTemplate restTemplate;
    private final ValidationService validationService;

    private static DefaultUriBuilderFactory defaultUri() {
        return new DefaultUriBuilderFactory("http://api.open-meteo.com/v1/forecast");
    }

    public WeatherResponseClientDto getCityWeatherByLatitudeAndLongitude(Float latitude, Float longitude) {
        URI uri = defaultUri()
                .builder()
                .queryParam(LATITUDE, latitude)
                .queryParam(LONGITUDE, longitude)
                .queryParam(DAILY, WMO)
                .queryParam(TIME_ZONE, CEST)
                .build();

        WeatherResponseClientDto weatherResponseClientDto = restTemplate.getForObject(uri, WeatherResponseClientDto.class);
        validationService.doValidate(weatherResponseClientDto);
        return weatherResponseClientDto;

//        String json = restTemplate.getForObject(uri, String.class);
//        SimpleModule module = new SimpleModule("CustomWeatherDeserializer",
//                new Version(1, 0, 0, null, null, null));
//        module.addDeserializer(WeatherResponseClientDto.class, new CustomWeatherDeserializer());
//        objectMapper.registerModule(module);
//        try {
//            WeatherResponseClientDto weatherResponseClientDto = objectMapper.readValue(json, WeatherResponseClientDto.class);
//            return weatherResponseClientDto;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    public WeatherResponseClientDto getCityTemperatureAvgByLatitudeAndLongitude(Float latitude, Float longitude, int days) {
        URI uri = defaultUri()
                .builder()
                .queryParam(LATITUDE, latitude)
                .queryParam(LONGITUDE, longitude)
                .queryParam(HOURLY, TEMP)
                .queryParam(FORECAST_DAYS, days)
                .build();

        WeatherResponseClientDto weatherResponseClientDto = restTemplate.getForObject(uri, WeatherResponseClientDto.class);
        validationService.doValidate(weatherResponseClientDto);
        return weatherResponseClientDto;
    }

    public WeatherResponseClientDto getMunicipalitiesTemperatureAvgByLatitudeAndLongitudeAndDate(Float latitude, Float longitude, String date) {
        URI uri = defaultUri()
                .builder()
                .queryParam(LATITUDE, latitude)
                .queryParam(LONGITUDE, longitude)
                .queryParam(HOURLY, TEMP)
                .queryParam(START_DATE, date)
                .queryParam(END_DATE, date)
                .build();

        WeatherResponseClientDto weatherResponseClientDto = restTemplate.getForObject(uri, WeatherResponseClientDto.class);
        validationService.doValidate(weatherResponseClientDto);
        return weatherResponseClientDto;
    }
}

