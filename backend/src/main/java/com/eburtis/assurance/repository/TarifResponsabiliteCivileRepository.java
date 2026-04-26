package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.TarifResponsabiliteCivile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifResponsabiliteCivileRepository extends JpaRepository<TarifResponsabiliteCivile, Long> {

	@Query("""
			SELECT t FROM TarifResponsabiliteCivile t
			WHERE t.puissanceMin <= :puissanceFiscale
			AND (t.puissanceMax IS NULL OR t.puissanceMax >= :puissanceFiscale)
			""")
	Optional<TarifResponsabiliteCivile> rechercherParPuissanceFiscale(Integer puissanceFiscale);
}
