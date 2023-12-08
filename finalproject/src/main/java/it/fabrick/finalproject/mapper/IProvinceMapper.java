package it.fabrick.finalproject.mapper;

import it.fabrick.finalproject.entity.ProvinceEntity;
import it.fabrick.finalproject.model.ProvinceModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IProvinceMapper {

    ProvinceEntity entityFromModel(ProvinceModel provinceModel);
}
