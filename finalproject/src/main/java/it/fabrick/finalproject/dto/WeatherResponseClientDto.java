package it.fabrick.finalproject.dto;

import it.fabrick.finalproject.dto.weatherDataClient.Daily;
import it.fabrick.finalproject.dto.weatherDataClient.Hourly;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherResponseClientDto {

    private Daily daily;

    private Hourly hourly;
}
