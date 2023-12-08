package it.fabrick.finalproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fabrick.finalproject.dto.WeatherResponseClientDto;
import it.fabrick.finalproject.dto.weatherDataClient.Hourly;
import it.fabrick.finalproject.entity.CityEntity;
import it.fabrick.finalproject.entity.MunicipalityEntity;
import it.fabrick.finalproject.entity.ProvinceEntity;
import it.fabrick.finalproject.entity.RegionEntity;
import it.fabrick.finalproject.enumeration.ErrorCode;
import it.fabrick.finalproject.exception.InternalErrorException;
import it.fabrick.finalproject.mapper.ICityMapper;
import it.fabrick.finalproject.mapper.IMunicipalityMapper;
import it.fabrick.finalproject.model.CityModel;
import it.fabrick.finalproject.model.MunicipalityModel;
import it.fabrick.finalproject.model.WeatherModel;
import it.fabrick.finalproject.repository.ICityRepository;
import it.fabrick.finalproject.repository.IMunicipalityRepository;
import it.fabrick.finalproject.repository.IProvinceRepository;
import it.fabrick.finalproject.repository.IRegionRepository;
import it.fabrick.finalproject.service.api.WeatherApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DemographicMeteoService {

    private final static int HOUR_PER_DAY = 24;

    private final IMunicipalityRepository iMunicipalityRepository;
    private final IRegionRepository iRegionRepository;
    private final ICityRepository iCityRepository;
    private final IProvinceRepository iProvinceRepository;
    private final ICityMapper iCityMapper;
    private final IMunicipalityMapper iMunicipalityMapper;
    private final WeatherApi weatherApi;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;

    public List<CityModel> getCitiesByNumberOfPopulationGreaterThan(String numberOfPopulation) {
        validationService.doValidateIntegerAsString(numberOfPopulation);
        List<CityModel> cities = new ArrayList<>();
        try {
            cities = iCityRepository.findByNumberOfPopulationGreaterThan(Integer.parseInt(numberOfPopulation))
                    .stream()
                    .map(iCityMapper::modelFromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return cities;
    }


    public List<MunicipalityModel> getMunicipalitiesByRegionAndNumberOfResidentGreaterThan(String numberOfResident,
                                                                                           String regionName) {
        validationService.doValidateIntegerAsString(numberOfResident);
        List<MunicipalityModel> municipalities = new ArrayList<>();
        try {
            RegionEntity region = iRegionRepository.findByName(regionName.toLowerCase());
            municipalities = iMunicipalityRepository.findByRegionAndNumberOfResidentGreaterThan(region, Integer.parseInt(numberOfResident))
                    .stream()
                    .map(iMunicipalityMapper::modelFromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return municipalities;
    }


    public List<MunicipalityModel> getMunicipalitiesByProvinceAndNumberOfResidentGreaterThan(String numberOfResident,
                                                                                             String provinceName) {
        validationService.doValidateIntegerAsString(numberOfResident);
        List<MunicipalityModel> municipalities = new ArrayList<>();
        try {
            ProvinceEntity province = iProvinceRepository.findByName(provinceName.toLowerCase());
            municipalities = iMunicipalityRepository.findByProvinceAndNumberOfResidentGreaterThan(province, Integer.parseInt(numberOfResident))
                    .stream()
                    .map(iMunicipalityMapper::modelFromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return municipalities;
    }

    public WeatherModel getCityWeather(String cityName) {
        WeatherModel weatherModel = new WeatherModel();
        try {
            CityEntity city = iCityRepository.findByName(cityName.toLowerCase());
            Map<Integer, String> wmoMap = createWmo();
            WeatherResponseClientDto weatherResponseClientDto = weatherApi.getCityWeatherByLatitudeAndLongitude(city.getLatitude(), city.getLongitude());

            List<String> wmoValues =
                    weatherResponseClientDto.getDaily()
                            .getWeatherCode()
                            .stream()
                            .map(wmoMap::get)
                            .collect(Collectors.toList());

            weatherModel = WeatherModel.builder()
                    .time(weatherResponseClientDto.getDaily().getTime())
                    .weatherCode(wmoValues)
                    .build();
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return weatherModel;
    }

    public WeatherModel getCityTemperatureAvg(String cityName, int days) {
        WeatherModel weatherModel = new WeatherModel();
        try {
            CityEntity city = iCityRepository.findByName(cityName.toLowerCase());
            WeatherResponseClientDto weatherResponseClientDto = weatherApi.getCityTemperatureAvgByLatitudeAndLongitude(city.getLatitude(), city.getLongitude(), days);

            List<Float> temperature = weatherResponseClientDto.getHourly().getTemperature();
            Float totalTemperature = temperature.stream().reduce(0f, Float::sum);
            Float avgTemperature = totalTemperature / temperature.size();

            weatherModel = WeatherModel.builder()
                    .temperature(Collections.singletonList(avgTemperature))
                    .build();
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return weatherModel;
    }

    public WeatherModel getMunicipalitiesTemperatureAvg(String provinceName, LocalDate date) {
        WeatherModel weatherModel = new WeatherModel();
        try {
            ProvinceEntity province = iProvinceRepository.findByName(provinceName.toLowerCase());
            List<MunicipalityEntity> municipalityEntities = iMunicipalityRepository.findByProvince(province);

            List<WeatherResponseClientDto> weatherResponseClientDtoList = new ArrayList<>();
            for (MunicipalityEntity m :
                    municipalityEntities) {
                WeatherResponseClientDto weatherResponseClientDto = weatherApi.getMunicipalitiesTemperatureAvgByLatitudeAndLongitudeAndDate(m.getLatitude(), m.getLongitude(), date.toString());
                weatherResponseClientDtoList.add(weatherResponseClientDto);
            }

            List<List<Float>> collect = weatherResponseClientDtoList.stream()
                    .map(WeatherResponseClientDto::getHourly)
                    .map(Hourly::getTemperature)
                    .collect(Collectors.toList());

            List<Float> temperatures = collect.stream()
                    .map(floats -> floats.stream().reduce(0f, Float::sum))
                    .collect(Collectors.toList());


            Float temperatureTotalAmount = temperatures.stream()
                    .reduce(0f, Float::sum);

            int temperatureTotalQty = collect.size() * HOUR_PER_DAY;

            Float temperatureAvg = temperatureTotalAmount / temperatureTotalQty;

            weatherModel = WeatherModel.builder()
                    .temperature(Collections.singletonList(temperatureAvg))
                    .build();
        } catch (Exception e) {
            throw generateGenericInternalError(e);
        }
        return weatherModel;
    }

    private Map<Integer, String> createWmo() {
        try {
            List<WeatherModel> weatherModels = List.of(objectMapper.readValue(new File("WMO.json"), WeatherModel[].class));
            Map<Integer, String> wmoMap = new HashMap<>();
            for (WeatherModel w :
                    weatherModels) {
                wmoMap.putIfAbsent(w.getCode(), w.getDescription());
            }
            return wmoMap;
        } catch (IOException e) {
            throw generateGenericInternalError(e);
        }
    }

    public InternalErrorException generateGenericInternalError(Exception e) {
        return new InternalErrorException("Something went wrong", e, ErrorCode.INTERNAL_ERROR);
    }
}

