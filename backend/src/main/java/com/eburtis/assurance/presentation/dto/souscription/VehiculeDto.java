package com.eburtis.assurance.presentation.dto.souscription;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VehiculeDto {
	private LocalDate datePremiereMiseEnCirculation;
	private String numeroImmatriculation;
	private String couleur;
	private Integer nombreSieges;
	private Integer nombrePortes;
	private String categorieCode;
	private Integer puissanceFiscale;
	private BigDecimal valeurNeuve;
	private BigDecimal valeurVenale;

	public LocalDate getDatePremiereMiseEnCirculation() {
		return datePremiereMiseEnCirculation;
	}

	public void setDatePremiereMiseEnCirculation(LocalDate datePremiereMiseEnCirculation) {
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
	}

	public String getNumeroImmatriculation() {
		return numeroImmatriculation;
	}

	public void setNumeroImmatriculation(String numeroImmatriculation) {
		this.numeroImmatriculation = numeroImmatriculation;
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public Integer getNombreSieges() {
		return nombreSieges;
	}

	public void setNombreSieges(Integer nombreSieges) {
		this.nombreSieges = nombreSieges;
	}

	public Integer getNombrePortes() {
		return nombrePortes;
	}

	public void setNombrePortes(Integer nombrePortes) {
		this.nombrePortes = nombrePortes;
	}

	public String getCategorieCode() {
		return categorieCode;
	}

	public void setCategorieCode(String categorieCode) {
		this.categorieCode = categorieCode;
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
