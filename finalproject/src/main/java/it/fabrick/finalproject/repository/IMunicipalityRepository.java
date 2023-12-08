package it.fabrick.finalproject.repository;

import it.fabrick.finalproject.entity.MunicipalityEntity;
import it.fabrick.finalproject.entity.ProvinceEntity;
import it.fabrick.finalproject.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMunicipalityRepository extends JpaRepository<MunicipalityEntity, String> {

    List<MunicipalityEntity> findByRegionAndNumberOfResidentGreaterThan(RegionEntity region, Integer numberOfResident);

    List<MunicipalityEntity> findByProvinceAndNumberOfResidentGreaterThan(ProvinceEntity province, Integer numberOfResident);

    List<MunicipalityEntity> findByProvince(ProvinceEntity province);


}
