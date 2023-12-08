package it.fabrick.finalproject.dto.weatherDataClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Daily {

    private List<String> time;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;
}

