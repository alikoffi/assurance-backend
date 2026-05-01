package com.eburtis.assurance.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = "simulation")
public class Simulation extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "simulation_id_seq", sequenceName = "simulation_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "simulation_id_seq")
	private Long id;

	private String quoteReference;
	private LocalDate endDate;

	@ManyToOne
	@JoinColumn(name = "produit_assurance_id")
	private ProduitAssurance produitAssurance;

	@ManyToOne
	@JoinColumn(name = "categorie_vehicule_id")
	private CategorieVehicule categorieVehicule;

	@ManyToOne
	@JoinColumn(name = "utilisateur_id")
	private Utilisateur utilisateur;

	private LocalDate datePremiereMiseEnCirculation;
	private Integer puissanceFiscale;
	private BigDecimal valeurNeuve;
	private BigDecimal valeurVenale;
	private BigDecimal price;

	public Simulation() {
	}

	public Simulation(String quoteReference, LocalDate endDate, ProduitAssurance produitAssurance,
			CategorieVehicule categorieVehicule, LocalDate datePremiereMiseEnCirculation,
			Integer puissanceFiscale, BigDecimal valeurNeuve, BigDecimal valeurVenale, BigDecimal price) {
		this.quoteReference = quoteReference;
		this.endDate = endDate;
		this.produitAssurance = produitAssurance;
		this.categorieVehicule = categorieVehicule;
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
		this.puissanceFiscale = puissanceFiscale;
		this.valeurNeuve = valeurNeuve;
		this.valeurVenale = valeurVenale;
		this.price = price;
	}

	public void mettreAJour(ProduitAssurance produitAssurance, CategorieVehicule categorieVehicule,
			LocalDate datePremiereMiseEnCirculation, Integer puissanceFiscale, BigDecimal valeurNeuve,
			BigDecimal valeurVenale, BigDecimal price) {
		this.produitAssurance = produitAssurance;
		this.categorieVehicule = categorieVehicule;
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
		this.puissanceFiscale = puissanceFiscale;
		this.valeurNeuve = valeurNeuve;
		this.valeurVenale = valeurVenale;
		this.price = price;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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

	public ProduitAssurance getProduitAssurance() {
		return produitAssurance;
	}

	public CategorieVehicule getCategorieVehicule() {
		return categorieVehicule;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
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
}
