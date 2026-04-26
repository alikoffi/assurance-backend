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
@Table(name = "vehicule")
public class Vehicule extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "vehicule_id_seq", sequenceName = "vehicule_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicule_id_seq")
	private Long id;

	private LocalDate datePremiereMiseEnCirculation;
	private String numeroImmatriculation;
	private String couleur;
	private Integer nombreSieges;
	private Integer nombrePortes;

	@ManyToOne
	@JoinColumn(name = "categorie_vehicule_id")
	private CategorieVehicule categorieVehicule;

	private Integer puissanceFiscale;
	private BigDecimal valeurNeuve;
	private BigDecimal valeurVenale;

	public Vehicule() {
	}

	public Vehicule(LocalDate datePremiereMiseEnCirculation, String numeroImmatriculation, String couleur,
			Integer nombreSieges, Integer nombrePortes, CategorieVehicule categorieVehicule,
			Integer puissanceFiscale, BigDecimal valeurNeuve, BigDecimal valeurVenale) {
		this.datePremiereMiseEnCirculation = datePremiereMiseEnCirculation;
		this.numeroImmatriculation = numeroImmatriculation;
		this.couleur = couleur;
		this.nombreSieges = nombreSieges;
		this.nombrePortes = nombrePortes;
		this.categorieVehicule = categorieVehicule;
		this.puissanceFiscale = puissanceFiscale;
		this.valeurNeuve = valeurNeuve;
		this.valeurVenale = valeurVenale;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDatePremiereMiseEnCirculation() {
		return datePremiereMiseEnCirculation;
	}

	public String getNumeroImmatriculation() {
		return numeroImmatriculation;
	}

	public String getCouleur() {
		return couleur;
	}

	public Integer getNombreSieges() {
		return nombreSieges;
	}

	public Integer getNombrePortes() {
		return nombrePortes;
	}

	public CategorieVehicule getCategorieVehicule() {
		return categorieVehicule;
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
}
