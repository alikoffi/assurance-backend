package com.eburtis.assurance.presentation.dto.souscription;

public class AssureDto {
	private String adresse;
	private String telephone;
	private String nom;
	private String prenom;
	private String numeroCarteIdentite;
	private String ville;

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNumeroCarteIdentite() {
		return numeroCarteIdentite;
	}

	public void setNumeroCarteIdentite(String numeroCarteIdentite) {
		this.numeroCarteIdentite = numeroCarteIdentite;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
}
