package it.fabrick.finalproject.mapper;

import it.fabrick.finalproject.entity.RegionEntity;
import it.fabrick.finalproject.model.RegionModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IRegionMapper {

    RegionEntity entityFromModel(RegionModel regionModel);
}
