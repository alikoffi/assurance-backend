package com.eburtis.assurance.presentation.dto.utilisateur;

import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutUtilisateur;

public class UtilisateurRequestDto {
	private String username;
	private String password;
	private String nom;
	private String prenoms;
	private Role role;
	private StatutUtilisateur statut;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenoms() {
		return prenoms;
	}

	public void setPrenoms(String prenoms) {
		this.prenoms = prenoms;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public StatutUtilisateur getStatut() {
		return statut;
	}

	public void setStatut(StatutUtilisateur statut) {
		this.statut = statut;
	}
}
