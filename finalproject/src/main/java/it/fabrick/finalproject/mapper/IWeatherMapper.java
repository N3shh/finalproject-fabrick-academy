package it.fabrick.finalproject.mapper;

import it.fabrick.finalproject.dto.WeatherDailyResponseDto;
import it.fabrick.finalproject.dto.WeatherHourlyResponseDto;
import it.fabrick.finalproject.dto.WeatherResponseClientDto;
import it.fabrick.finalproject.model.WeatherModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IWeatherMapper {

    WeatherModel modelFromResponseExternal(WeatherResponseClientDto weatherResponseClientDto);

    WeatherDailyResponseDto dailyResponseFromModel(WeatherModel cityWeather);

    WeatherHourlyResponseDto hourlyResponseFromModel(WeatherModel cityTemperatureAvg);
}
