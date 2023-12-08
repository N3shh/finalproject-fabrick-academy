package it.fabrick.finalproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class WeatherDailyResponseDto {

    private List<String> time;

    private List<String> weatherCode;


}
