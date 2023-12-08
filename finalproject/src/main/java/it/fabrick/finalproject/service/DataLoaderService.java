package it.fabrick.finalproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fabrick.finalproject.entity.MunicipalityEntity;
import it.fabrick.finalproject.entity.ProvinceEntity;
import it.fabrick.finalproject.entity.RegionEntity;
import it.fabrick.finalproject.enumeration.ErrorCode;
import it.fabrick.finalproject.exception.InternalErrorException;
import it.fabrick.finalproject.mapper.ICityMapper;
import it.fabrick.finalproject.mapper.IProvinceMapper;
import it.fabrick.finalproject.mapper.IRegionMapper;
import it.fabrick.finalproject.model.CityModel;
import it.fabrick.finalproject.model.MunicipalityModel;
import it.fabrick.finalproject.model.ProvinceModel;
import it.fabrick.finalproject.model.RegionModel;
import it.fabrick.finalproject.repository.ICityRepository;
import it.fabrick.finalproject.repository.IMunicipalityRepository;
import it.fabrick.finalproject.repository.IProvinceRepository;
import it.fabrick.finalproject.repository.IRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DataLoaderService {

    private final ICityRepository iCityRepository;
    private final IMunicipalityRepository iMunicipalityRepository;
    private final IProvinceRepository iProvinceRepository;
    private final IRegionRepository iRegionRepository;
    private final ObjectMapper objectMapper;
    private final ICityMapper iCityMapper;
    private final IRegionMapper iRegionMapper;
    private final IProvinceMapper iProvinceMapper;


    @PostConstruct
    public void run() {
        loadCities(); // todo: non e' necessario creare la tabella di citta', i dati sono gia' presenti sulla tabella comuni.
                     // todo: andando ad avere cosi' il DB popolato con dati DOPPI!!

        loadRegions();
        loadProvinces();

        List<ProvinceEntity> provinceEntities = iProvinceRepository.findAll(); // todo: meglio avere una mappa
        List<RegionEntity> regionEntities = iRegionRepository.findAll();

        loadMunicipalities(provinceEntities, regionEntities);
    }

    private void loadMunicipalities(List<ProvinceEntity> provinceEntities, List<RegionEntity> regionEntities) {
        List<MunicipalityModel> municipalityLoad = loadDataFromJson("italy_municipalities.json", MunicipalityModel[].class);
        List<MunicipalityModel> municipalityGeoLoad = loadDataFromJson("italy_geo.json", MunicipalityModel[].class);


        for (MunicipalityModel mload :
                municipalityLoad) {
            MunicipalityEntity municipalityEntity = new MunicipalityEntity();
            municipalityEntity.setName(mload.getName());
            municipalityEntity.setNumberOfResident(mload.getNumberOfResident());
            for (ProvinceEntity p : provinceEntities) {
                if (p.getAcronym().equals(mload.getProvinceInternal())) {
                    municipalityEntity.setProvince(p);
                }
            }
            for (RegionEntity r : regionEntities) {
                if (r.getName().equals(mload.getRegionInternal())) {
                    municipalityEntity.setRegion(r);
                }
            }
            for (MunicipalityModel mLoadGeo :
                    municipalityGeoLoad) {
                if (mload.getIstat().equals(mLoadGeo.getIstat())) {
                    municipalityEntity.setLatitude(mLoadGeo.getLatitude());
                    municipalityEntity.setLongitude(mLoadGeo.getLongitude());
                }
            }
            iMunicipalityRepository.save(municipalityEntity);
        }
    }

    private void loadProvinces() {
        List<ProvinceModel> provinceModels = loadDataFromJson("italy_provinces.json", ProvinceModel[].class);

        iProvinceRepository.saveAll(provinceModels.stream()
                .map(iProvinceMapper::entityFromModel)
                .collect(Collectors.toList()));
    }

    private void loadRegions() {
        List<RegionModel> regionModels = loadDataFromJson("italy_regions.json", RegionModel[].class);

        iRegionRepository.saveAll(regionModels.stream()
                .map(iRegionMapper::entityFromModel)
                .collect(Collectors.toList()));
    }

    private void loadCities() {
        List<CityModel> cityModels = loadDataFromJson("italy_cities.json", CityModel[].class);

        iCityRepository.saveAll(cityModels.stream()
                .map(iCityMapper::entityFromModel)
                .collect(Collectors.toList()));
    }

    private <T> List<T> loadDataFromJson(String filePath, Class<T[]> type) {
        try {
            return List.of(objectMapper.readValue(new File(filePath), type));
        } catch (IOException e) {
            throw new InternalErrorException("Something went wrong", e, ErrorCode.INTERNAL_ERROR);
        }
    }
}
