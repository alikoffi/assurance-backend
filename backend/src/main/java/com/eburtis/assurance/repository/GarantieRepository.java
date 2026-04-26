package com.eburtis.assurance.repository;

import com.eburtis.assurance.domain.Garantie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarantieRepository extends JpaRepository<Garantie, Long> {
}
