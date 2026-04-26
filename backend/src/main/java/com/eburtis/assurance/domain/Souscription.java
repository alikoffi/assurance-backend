package com.eburtis.assurance.domain;

import com.eburtis.assurance.enums.StatutSouscription;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Access(AccessType.FIELD)
@Table(name = "souscription")
public class Souscription extends AbstractEntity {

	@Id
	@SequenceGenerator(name = "souscription_id_seq", sequenceName = "souscription_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "souscription_id_seq")
	private Long id;

	private String subscriptionReference;

	@ManyToOne
	@JoinColumn(name = "simulation_id")
	private Simulation simulation;

	@ManyToOne
	@JoinColumn(name = "assure_id")
	private Assure assure;

	@ManyToOne
	@JoinColumn(name = "vehicule_id")
	private Vehicule vehicule;

	@ManyToOne
	@JoinColumn(name = "utilisateur_id")
	private Utilisateur utilisateur;

	@Enumerated(EnumType.STRING)
	private StatutSouscription statut;

	private String numeroAttestation;
	private LocalDateTime dateSouscription;

	public Souscription() {
	}

	public Souscription(String subscriptionReference, Simulation simulation, Assure assure, Vehicule vehicule,
			Utilisateur utilisateur, StatutSouscription statut, String numeroAttestation, LocalDateTime dateSouscription) {
		this.subscriptionReference = subscriptionReference;
		this.simulation = simulation;
		this.assure = assure;
		this.vehicule = vehicule;
		this.utilisateur = utilisateur;
		this.statut = statut;
		this.numeroAttestation = numeroAttestation;
		this.dateSouscription = dateSouscription;
	}

	public Long getId() {
		return id;
	}

	public String getSubscriptionReference() {
		return subscriptionReference;
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public Assure getAssure() {
		return assure;
	}

	public Vehicule getVehicule() {
		return vehicule;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public StatutSouscription getStatut() {
		return statut;
	}

	public String getNumeroAttestation() {
		return numeroAttestation;
	}

	public LocalDateTime getDateSouscription() {
		return dateSouscription;
	}
}
