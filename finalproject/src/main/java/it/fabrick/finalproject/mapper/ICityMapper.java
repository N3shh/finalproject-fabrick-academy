package it.fabrick.finalproject.mapper;

import it.fabrick.finalproject.dto.CityResponseDto;
import it.fabrick.finalproject.entity.CityEntity;
import it.fabrick.finalproject.model.CityModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ICityMapper {

    CityResponseDto responseFromModel(CityModel cityModel);

    CityModel modelFromEntity(CityEntity cityEntity);

    CityEntity entityFromModel(CityModel cityModel);
}
