package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.CategorieVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieVehiculeRepository extends JpaRepository<CategorieVehicule, Long> {
	Optional<CategorieVehicule> findByCode(String code);
}
