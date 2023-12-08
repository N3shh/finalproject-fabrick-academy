package it.fabrick.finalproject.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WeatherModel {

    private List<String> time;

    private List<Float> temperature;

    private List<String> weatherCode;

    @JsonAlias({"Description"})
    private String description;

    @JsonAlias({"Code"})
    private Integer code;

    private Map<Integer, String> wmoMap = new LinkedHashMap<>();
}
