package com.eburtis.assurance.presentation.dto.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimulationRequestDto {
	private String produitCode;
	private String categorieCode;
	private LocalDate datePremiereMiseEnCirculation;
	private Integer puissanceFiscale;
	private BigDecimal valeurNeuve;
	private BigDecimal valeurVenale;

	public String getProduitCode() {
		return produitCode;
	}

	public void setProduitCode(String produitCode) {
		this.produitCode = produitCode;
	}

	public String getCategorieCode() {
		return categorieCode;
	}

	public void setCategorieCode(String categorieCode) {
		this.categorieCode = categorieCode;
	}

	public LocalDate getDatePremiereMiseEnCirculation() {
		return datePremiereMiseEnCirculation;
	}

	public void setDatePremiereMiseEnCirculation(LocalDate datePremiereMiseEnCirculation) {
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
	}

	public Integer getPuissanceFiscale() {
		return puissanceFiscale;
	}

	public void setPuissanceFiscale(Integer puissanceFiscale) {
		this.puissanceFiscale = puissanceFiscale;
	}

	public BigDecimal getValeurNeuve() {
		return valeurNeuve;
	}

	public void setValeurNeuve(BigDecimal valeurNeuve) {
		this.valeurNeuve = valeurNeuve;
	}

	public BigDecimal getValeurVenale() {
		return valeurVenale;
	}

	public void setValeurVenale(BigDecimal valeurVenale) {
		this.valeurVenale = valeurVenale;
	}
}
