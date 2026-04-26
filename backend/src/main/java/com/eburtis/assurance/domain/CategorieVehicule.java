package com.eburtis.assurance.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Access(AccessType.FIELD)
@Table(name = "categorie_vehicule")
public class CategorieVehicule extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "categorie_vehicule_id_seq", sequenceName = "categorie_vehicule_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorie_vehicule_id_seq")
	private Long id;

	private String code;
	private String libelle;
	private String description;

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public String getDescription() {
		return description;
	}
}
