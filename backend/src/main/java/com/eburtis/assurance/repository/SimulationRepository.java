package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
	Optional<Simulation> findByQuoteReference(String quoteReference);

	List<Simulation> findByUtilisateurId(Long utilisateurId);
}
