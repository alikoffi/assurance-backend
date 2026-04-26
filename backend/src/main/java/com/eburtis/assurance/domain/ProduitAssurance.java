package com.eburtis.assurance.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Access(AccessType.FIELD)
@Table(name = "produit_assurance")
public class ProduitAssurance extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "produit_assurance_id_seq", sequenceName = "produit_assurance_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produit_assurance_id_seq")
	private Long id;

	private String code;
	private String nom;
	private String description;
	private boolean actif;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "produit_categorie_vehicule",
			joinColumns = @JoinColumn(name = "produit_assurance_id"),
			inverseJoinColumns = @JoinColumn(name = "categorie_vehicule_id")
	)
	private Set<CategorieVehicule> categoriesEligibles = new LinkedHashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "produit_garantie",
			joinColumns = @JoinColumn(name = "produit_assurance_id"),
			inverseJoinColumns = @JoinColumn(name = "garantie_id")
	)
	private Set<Garantie> garanties = new LinkedHashSet<>();

	public boolean estEligiblePour(CategorieVehicule categorieVehicule) {
		return categoriesEligibles.stream()
				.anyMatch(categorie -> categorie.getId().equals(categorieVehicule.getId()));
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getNom() {
		return nom;
	}

	public String getDescription() {
		return description;
	}

	public boolean isActif() {
		return actif;
	}

	public Set<CategorieVehicule> getCategoriesEligibles() {
		return categoriesEligibles;
	}

	public Set<Garantie> getGaranties() {
		return garanties;
	}
}
