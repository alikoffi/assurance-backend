package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

	@Query("SELECT u FROM Utilisateur u WHERE trim(both from u.username) = :username")
	Optional<Utilisateur> rechercherParUsername(String username);

	@Query("SELECT u FROM Utilisateur u ORDER BY u.nom, u.prenoms, u.username")
	List<Utilisateur> listerOrdonnes();
}
