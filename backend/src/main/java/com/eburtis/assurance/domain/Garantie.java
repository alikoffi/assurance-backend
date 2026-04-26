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
@Table(name = "garantie")
public class Garantie extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "garantie_id_seq", sequenceName = "garantie_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "garantie_id_seq")
	private Long id;

	private String code;
	private String libelle;
	private BigDecimal taux;
	private Integer ageMaximum;
	private BigDecimal primeMinimum;

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public BigDecimal getTaux() {
		return taux;
	}

	public Integer getAgeMaximum() {
		return ageMaximum;
	}

	public BigDecimal getPrimeMinimum() {
		return primeMinimum;
	}
}
