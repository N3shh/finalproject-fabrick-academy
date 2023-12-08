package it.fabrick.finalproject.repository;

import it.fabrick.finalproject.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICityRepository extends JpaRepository<CityEntity, String> {

    CityEntity findByName(String name);

    List<CityEntity> findByNumberOfPopulationGreaterThan(Integer numberOfPopulation);
}
