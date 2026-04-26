package com.eburtis.assurance.presentation.dto.utilisateur;

import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutUtilisateur;

public class UtilisateurResponseDto {
	private Long id;
	private String username;
	private String nom;
	private String prenoms;
	private Role role;
	private StatutUtilisateur statut;
	private boolean mustChangePassword;

	public UtilisateurResponseDto() {
	}

	public UtilisateurResponseDto(Long id, String username, String nom, String prenoms, Role role,
			StatutUtilisateur statut) {
		this.id = id;
		this.username = username;
		this.nom = nom;
		this.prenoms = prenoms;
		this.role = role;
		this.statut = statut;
	}

	public UtilisateurResponseDto(Long id, String username, String nom, String prenoms, Role role,
			StatutUtilisateur statut, boolean mustChangePassword) {
		this(id, username, nom, prenoms, role, statut);
		this.mustChangePassword = mustChangePassword;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenoms() {
		return prenoms;
	}

	public Role getRole() {
		return role;
	}

	public StatutUtilisateur getStatut() {
		return statut;
	}

	public boolean isMustChangePassword() {
		return mustChangePassword;
	}
}
