package com.eburtis.assurance.presentation.dto.souscription;

public class AssureResponseDto {
	private Long id;
	private String adresse;
	private String telephone;
	private String nom;
	private String prenom;
	private String numeroCarteIdentite;
	private String ville;

	public AssureResponseDto() {
	}

	public AssureResponseDto(Long id, String adresse, String telephone, String nom, String prenom,
			String numeroCarteIdentite, String ville) {
		this.id = id;
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
