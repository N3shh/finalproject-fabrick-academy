package it.fabrick.finalproject.repository;

import it.fabrick.finalproject.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<ProvinceEntity, String> {

    ProvinceEntity findByName(String provinceName);
}
