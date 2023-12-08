package it.fabrick.finalproject.dto.weatherDataClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Hourly {

    private List<String> time;

    @JsonProperty("temperature_2m")
    private List<Float> temperature;
}
