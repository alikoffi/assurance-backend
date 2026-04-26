package com.eburtis.assurance.domain;

import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutUtilisateur;
import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.singleton;


@Entity
@Access(AccessType.FIELD)
@Table(name = Utilisateur.TABLE_NAME)
public class Utilisateur extends AbstractEntity {

	public static final String TABLE_NAME = "utilisateur";
	public static final String TABLE_ID = TABLE_NAME + ID;
	public static final String TABLE_SEQ = TABLE_ID + SEQ;

	@Id
	@SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
	private Long id;

	private String username;
	private String password;
	private String nom;
	private String prenoms;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private StatutUtilisateur statut;

	private boolean mustChangePassword;


	public User buildUser() {
		return new User(getUsername(), getPassword(), singleton(new SimpleGrantedAuthority(getRole().name())));
	}

	public Utilisateur() {

	}

	public Utilisateur(String username, String password, String nom, String prenoms, Role role, StatutUtilisateur statut) {
		this.username = username;
		this.password = password;
		this.nom = nom;
		this.prenoms = prenoms;
		this.role = role;
		this.statut = statut;
	}

	public Utilisateur(String username, String password, String nom, String prenoms, Role role, StatutUtilisateur statut,
			boolean mustChangePassword) {
		this(username, password, nom, prenoms, role, statut);
		this.mustChangePassword = mustChangePassword;
	}

	public void mettreAjour(String nom, String prenoms, Role role, StatutUtilisateur statut) {
		this.nom = nom;
		this.prenoms = prenoms;
		this.role = role;
		this.statut = statut;
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

	public String getPassword() {
		return password;
	}

	public StatutUtilisateur getStatut() {
		return statut;
	}

	public boolean isMustChangePassword() {
		return mustChangePassword;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setStatut(StatutUtilisateur statut) {
		this.statut = statut;
	}

	public void setMustChangePassword(boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}

	public void mettreAJourUtilisateur(String username, String password, String nom, String prenoms, Role role,
			StatutUtilisateur statut, boolean mustChangePassword) {
		this.username = username;
		this.password = password;
		this.nom = nom;
		this.prenoms = prenoms;
		this.role = role;
		this.statut = statut;
		this.mustChangePassword = mustChangePassword;
	}

}

