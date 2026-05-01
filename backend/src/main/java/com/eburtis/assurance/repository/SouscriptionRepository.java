package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.Souscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SouscriptionRepository extends JpaRepository<Souscription, Long> {
	Optional<Souscription> findBySubscriptionReference(String subscriptionReference);

	List<Souscription> findByUtilisateurId(Long utilisateurId);

	boolean existsBySimulationId(Long simulationId);
}
