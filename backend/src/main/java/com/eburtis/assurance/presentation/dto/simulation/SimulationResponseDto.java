package com.eburtis.assurance.presentation.dto.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SimulationResponseDto {
	private Long id;
	private String quoteReference;
	private LocalDate endDate;
	private String produitCode;
	private String produitNom;
	private String categorieCode;
	private LocalDate datePremiereMiseEnCirculation;
	private Integer puissanceFiscale;
	private BigDecimal valeurNeuve;
	private BigDecimal valeurVenale;
	private BigDecimal price;
	private List<GarantiePrimeDto> garanties;
	private Long utilisateurId;
	private String utilisateurNom;
	private boolean souscrite;
	private boolean modifiable;

	public SimulationResponseDto() {
	}

	public SimulationResponseDto(Long id, String quoteReference, LocalDate endDate, String produitCode, String produitNom,
			String categorieCode, LocalDate datePremiereMiseEnCirculation, Integer puissanceFiscale,
			BigDecimal valeurNeuve, BigDecimal valeurVenale, BigDecimal price, List<GarantiePrimeDto> garanties,
			Long utilisateurId, String utilisateurNom, boolean souscrite, boolean modifiable) {
		this.id = id;
		this.quoteReference = quoteReference;
		this.endDate = endDate;
		this.produitCode = produitCode;
		this.produitNom = produitNom;
		this.categorieCode = categorieCode;
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
		this.puissanceFiscale = puissanceFiscale;
		this.valeurNeuve = valeurNeuve;
		this.valeurVenale = valeurVenale;
		this.price = price;
		this.garanties = garanties;
		this.utilisateurId = utilisateurId;
		this.utilisateurNom = utilisateurNom;
		this.souscrite = souscrite;
		this.modifiable = modifiable;
	}

	public Long getId() {
		return id;
	}

	public String getQuoteReference() {
		return quoteReference;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getProduitCode() {
		return produitCode;
	}

	public String getProduitNom() {
		return produitNom;
	}

	public String getCategorieCode() {
		return categorieCode;
	}

	public LocalDate getDatePremiereMiseEnCirculation() {
		return datePremiereMiseEnCirculation;
	}

	public Integer getPuissanceFiscale() {
		return puissanceFiscale;
	}

	public BigDecimal getValeurNeuve() {
		return valeurNeuve;
	}

	public BigDecimal getValeurVenale() {
		return valeurVenale;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public List<GarantiePrimeDto> getGaranties() {
		return garanties;
	}

	public Long getUtilisateurId() {
		return utilisateurId;
	}

	public String getUtilisateurNom() {
		return utilisateurNom;
	}

	public boolean isSouscrite() {
		return souscrite;
	}

	public boolean isModifiable() {
		return modifiable;
	}
}
