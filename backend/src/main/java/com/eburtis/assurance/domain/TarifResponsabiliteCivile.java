package com.eburtis.assurance.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Access(AccessType.FIELD)
@Table(name = "tarif_responsabilite_civile")
public class TarifResponsabiliteCivile extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "tarif_responsabilite_civile_id_seq", sequenceName = "tarif_responsabilite_civile_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tarif_responsabilite_civile_id_seq")
	private Long id;

	private Integer puissanceMin;
	private Integer puissanceMax;
	private BigDecimal prime;

	public Long getId() {
		return id;
	}

	public Integer getPuissanceMin() {
		return puissanceMin;
	}

	public Integer getPuissanceMax() {
		return puissanceMax;
	}

	public BigDecimal getPrime() {
		return prime;
	}
}
