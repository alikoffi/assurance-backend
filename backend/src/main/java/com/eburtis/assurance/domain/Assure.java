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
@Table(name = "assure")
public class Assure extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "assure_id_seq", sequenceName = "assure_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assure_id_seq")
	private Long id;

	private String adresse;
	private String telephone;
	private String nom;
	private String prenom;
	private String numeroCarteIdentite;
	private String ville;

	public Assure() {
	}

	public Assure(String adresse, String telephone, String nom, String prenom, String numeroCarteIdentite, String ville) {
		this.adresse = adresse;
		this.telephone = telephone;
		this.nom = nom;
		this.prenom = prenom;
		this.numeroCarteIdentite = numeroCarteIdentite;
		this.ville = ville;
	}

	public Long getId() {
		return id;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getNumeroCarteIdentite() {
		return numeroCarteIdentite;
	}

	public String getVille() {
		return ville;
	}
}
