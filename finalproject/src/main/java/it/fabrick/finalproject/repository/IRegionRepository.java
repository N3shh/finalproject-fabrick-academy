package it.fabrick.finalproject.repository;

import it.fabrick.finalproject.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionRepository extends JpaRepository<RegionEntity, String> {

    RegionEntity findByName(String regionName);
}
