package it.fabrick.finalproject.mapper;

import it.fabrick.finalproject.dto.MunicipalityResponseDto;
import it.fabrick.finalproject.entity.MunicipalityEntity;
import it.fabrick.finalproject.model.MunicipalityModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IMunicipalityMapper {

    MunicipalityModel modelFromEntity(MunicipalityEntity municipalityEntity);

    MunicipalityResponseDto responseFromModel(MunicipalityModel municipalityModel);
}
