package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.ProduitAssurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProduitAssuranceRepository extends JpaRepository<ProduitAssurance, Long> {
	Optional<ProduitAssurance> findByCodeIgnoreCaseAndActifTrue(String code);
}
