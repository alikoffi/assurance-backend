package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.Assure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssureRepository extends JpaRepository<Assure, Long> {

	@Query("""
			SELECT DISTINCT s.assure
			FROM Souscription s
			WHERE s.utilisateur.id = :utilisateurId
			ORDER BY s.assure.nom, s.assure.prenom
			""")
	List<Assure> rechercherParUtilisateurId(Long utilisateurId);

	@Query("""
			SELECT COUNT(s) > 0
			FROM Souscription s
			WHERE s.assure.id = :assureId
			AND s.utilisateur.id = :utilisateurId
			""")
	boolean existePourUtilisateur(Long assureId, Long utilisateurId);
}
