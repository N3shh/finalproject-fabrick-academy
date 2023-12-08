package it.fabrick.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.fabrick.finalproject.dto.*;
import it.fabrick.finalproject.mapper.ICityMapper;
import it.fabrick.finalproject.mapper.IMunicipalityMapper;
import it.fabrick.finalproject.mapper.IWeatherMapper;
import it.fabrick.finalproject.service.DemographicMeteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1.0/demographic-meteo")
public class DemographicMeteoController {

    private final DemographicMeteoService demographicMeteoService;
    private final IMunicipalityMapper iMunicipalityMapper;
    private final ICityMapper iCityMapper;
    private final IWeatherMapper iWeatherMapper;

    @Operation(description = "Read average temperature of all municipalities of a province from a given day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/municipalities/filter-province/temperature-avg")
    public WeatherHourlyResponseDto readMunicipalitiesTemperatureAvg(@RequestParam(value = "province name")
                                                                     @Schema(description = "Set this filter to search for municipalities with the given province name")
                                                                             String provinceName,
                                                                     @RequestParam(value = "date")
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                     @Schema(description = "Set this filter to get the average temperature of the given date, ex. 2023-12-31")
                                                                             LocalDate date) {

        return iWeatherMapper.hourlyResponseFromModel(demographicMeteoService.getMunicipalitiesTemperatureAvg(provinceName, date));
    }

    @Operation(description = "Read average temperature of a city from a given forecast days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/temperature-avg/filter-city")
    public WeatherHourlyResponseDto readCityTemperatureAvg(@RequestParam("city name")
                                                           @Schema(description = "the name of the city")
                                                                   String cityName,
                                                           @RequestParam(value = "forecast days number")
                                                           @Min(value = 0, message = "filter number has to be higher than 0")
                                                           @Max(value = 17, message = "filter number has to be lower than 17")
                                                           @Schema(description = "set this filter to get temperatures on the given request days")
                                                                   Integer days) {

        return iWeatherMapper.hourlyResponseFromModel(demographicMeteoService.getCityTemperatureAvg(cityName, days));
    }

    @Operation(description = "Read city weathers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/weather/filter-city")
    public WeatherDailyResponseDto readCityWeather(@RequestParam("city name")
                                                   @Schema(description = "the name of the city")
                                                           String cityName) {

        return iWeatherMapper.dailyResponseFromModel(demographicMeteoService.getCityWeather(cityName));
    }

    @Operation(description = "Read all cities with a number of population greater than the given request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/cities")
    public ResponseEntity<List<CityResponseDto>> readCitiesByNumberOfPopulationGreaterThan(@RequestParam(value = "population number")
                                                                                           @Min(value = 431656, message = "filter number has to be higher than 431656")
                                                                                           @Max(value = 4340473, message = "filter number has to be lower or equal than 4340473")
                                                                                           @Schema(description = "Set this filter to search for cities with population higher than the given number")
                                                                                           @NotNull
                                                                                                   String numberOfPopulation) {

        return ResponseEntity.ok(demographicMeteoService.getCitiesByNumberOfPopulationGreaterThan(numberOfPopulation)
                .stream()
                .map(iCityMapper::responseFromModel)
                .collect(Collectors.toList()));
    }

    @Operation(description = "Read all municipalities of a region and with a number of population greater than the given request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/municipalities/filter-region-residents")
    public ResponseEntity<List<MunicipalityResponseDto>> readMunicipalitiesByRegionAndNumberOfResidentGreaterThan(@RequestParam(value = "resident number")
                                                                                                                  @Min(value = 7, message = "filter number has to be higher than 7")
                                                                                                                  @Max(value = 2638841, message = "filter number has to be lower than 2638841")
                                                                                                                  @Schema(description = "Set this filter to search for municipalities with resident higher than the given number")
                                                                                                                          String numberOfResident,
                                                                                                                  @RequestParam(value = "region name")
                                                                                                                  @Schema(description = "Set this filter to search for municipalities with the given region name")
                                                                                                                          String regionName) {

        return ResponseEntity.ok(demographicMeteoService.getMunicipalitiesByRegionAndNumberOfResidentGreaterThan(numberOfResident, regionName)
                .stream()
                .map(iMunicipalityMapper::responseFromModel)
                .collect(Collectors.toList()));
    }

    @Operation(description = "Read all municipalities of a province and with a number of population greater than the given request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    @GetMapping("/municipalities/filter-province-residents")
    public ResponseEntity<List<MunicipalityResponseDto>> readMunicipalitiesByProvinceAndNumberOfResidentGreaterThan(@RequestParam(value = "resident number")
                                                                                                                    @Min(value = 7, message = "filter number has to be higher than 7")
                                                                                                                    @Max(value = 2638841, message = "filter number has to be lower than 2638841")
                                                                                                                    @Schema(description = "Set this filter to search for municipalities with resident higher than the given number")
                                                                                                                            String numberOfResident,
                                                                                                                    @RequestParam(value = "province name")
                                                                                                                    @Schema(description = "Set this filter to search for municipalities with the given province name")
                                                                                                                            String provinceName) {

        return ResponseEntity.ok(demographicMeteoService.getMunicipalitiesByProvinceAndNumberOfResidentGreaterThan(numberOfResident, provinceName)
                .stream()
                .map(iMunicipalityMapper::responseFromModel)
                .collect(Collectors.toList()));
    }

}
