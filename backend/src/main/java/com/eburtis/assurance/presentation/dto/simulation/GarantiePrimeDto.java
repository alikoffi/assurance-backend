package com.eburtis.assurance.presentation.dto.simulation;

import java.math.BigDecimal;

public class GarantiePrimeDto {
	private String code;
	private String libelle;
	private BigDecimal montant;

	public GarantiePrimeDto() {
	}

	public GarantiePrimeDto(String code, String libelle, BigDecimal montant) {
		this.code = code;
		this.libelle = libelle;
		this.montant = montant;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public BigDecimal getMontant() {
		return montant;
	}
}
