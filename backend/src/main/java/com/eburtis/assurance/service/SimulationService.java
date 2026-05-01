package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.CategorieVehicule;
import com.eburtis.assurance.domain.ProduitAssurance;
import com.eburtis.assurance.domain.Simulation;
import com.eburtis.assurance.domain.Utilisateur;
import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.simulation.SimulationResponseDto;
import com.eburtis.assurance.repository.CategorieVehiculeRepository;
import com.eburtis.assurance.repository.ProduitAssuranceRepository;
import com.eburtis.assurance.repository.SimulationRepository;
import com.eburtis.assurance.repository.SouscriptionRepository;
import com.eburtis.assurance.repository.UtilisateurRepository;
import com.eburtis.assurance.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SimulationService {
	private final SimulationRepository simulationRepository;
	private final ProduitAssuranceRepository produitAssuranceRepository;
	private final CategorieVehiculeRepository categorieVehiculeRepository;
	private final PrimeCalculatorService primeCalculatorService;
	private final SouscriptionRepository souscriptionRepository;
	private final UtilisateurRepository utilisateurRepository;

	public SimulationService(SimulationRepository simulationRepository, ProduitAssuranceRepository produitAssuranceRepository,
			CategorieVehiculeRepository categorieVehiculeRepository, PrimeCalculatorService primeCalculatorService,
			SouscriptionRepository souscriptionRepository, UtilisateurRepository utilisateurRepository) {
		this.simulationRepository = simulationRepository;
		this.produitAssuranceRepository = produitAssuranceRepository;
		this.categorieVehiculeRepository = categorieVehiculeRepository;
		this.primeCalculatorService = primeCalculatorService;
		this.souscriptionRepository = souscriptionRepository;
		this.utilisateurRepository = utilisateurRepository;
	}

	@Transactional(readOnly = true)
	public List<SimulationResponseDto> lister() {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		List<Simulation> simulations = utilisateur.getRole() == Role.ADMIN
				? simulationRepository.findAll()
				: simulationRepository.findByUtilisateurId(utilisateur.getId());

		return simulations.stream()
				.map(this::toDto)
				.toList();
	}

	@Transactional
	public SimulationResponseDto creer(SimulationRequestDto request) {
		validerSimulationRequest(request);

		ProduitAssurance produit = produitAssuranceRepository.findByCodeIgnoreCaseAndActifTrue(normaliser(request.getProduitCode()))
				.orElseThrow(() -> AssuranceException.notFound("PRODUIT_INTROUVABLE", "Produit d'assurance introuvable."));
		CategorieVehicule categorie = categorieVehiculeRepository.findByCode(normaliser(request.getCategorieCode()))
				.orElseThrow(() -> AssuranceException.notFound("CATEGORIE_INTROUVABLE", "Catégorie de véhicule introuvable."));

		if (!produit.estEligiblePour(categorie)) {
			throw AssuranceException.badRequest("PRODUIT_NON_ELIGIBLE",
					"Le produit " + produit.getCode() + " n'est pas disponible pour la catégorie " + categorie.getCode() + ".");
		}

		PrimeCalculationResult calcul = primeCalculatorService.calculer(produit, request.getDatePremiereMiseEnCirculation(),
				request.getPuissanceFiscale(), request.getValeurNeuve(), request.getValeurVenale());

		Simulation simulation = new Simulation(genererReference("QT"), LocalDate.now().plusWeeks(2), produit, categorie,
				request.getDatePremiereMiseEnCirculation(), request.getPuissanceFiscale(),
				request.getValeurNeuve(), request.getValeurVenale(), calcul.getPrice());
		utilisateurConnecteOptionnel().ifPresent(simulation::setUtilisateur);

		return toDto(simulationRepository.save(simulation), calcul);
	}

	@Transactional(readOnly = true)
	public SimulationResponseDto rechercher(Long id) {
		return toDto(rechercherEntite(id));
	}

	@Transactional
	public SimulationResponseDto modifier(Long id, SimulationRequestDto request) {
		validerSimulationRequest(request);
		Simulation simulation = rechercherEntite(id);
		if (souscriptionRepository.existsBySimulationId(id)) {
			throw AssuranceException.badRequest("SIMULATION_DEJA_SOUSCRITE",
					"Une simulation déjà utilisée pour une souscription ne peut plus être modifiée.");
		}

		ProduitAssurance produit = produitAssuranceRepository.findByCodeIgnoreCaseAndActifTrue(normaliser(request.getProduitCode()))
				.orElseThrow(() -> AssuranceException.notFound("PRODUIT_INTROUVABLE", "Produit d'assurance introuvable."));
		CategorieVehicule categorie = categorieVehiculeRepository.findByCode(normaliser(request.getCategorieCode()))
				.orElseThrow(() -> AssuranceException.notFound("CATEGORIE_INTROUVABLE", "Catégorie de véhicule introuvable."));

		if (!produit.estEligiblePour(categorie)) {
			throw AssuranceException.badRequest("PRODUIT_NON_ELIGIBLE",
					"Le produit " + produit.getCode() + " n'est pas disponible pour la catégorie " + categorie.getCode() + ".");
		}

		PrimeCalculationResult calcul = primeCalculatorService.calculer(produit, request.getDatePremiereMiseEnCirculation(),
				request.getPuissanceFiscale(), request.getValeurNeuve(), request.getValeurVenale());
		simulation.mettreAJour(produit, categorie, request.getDatePremiereMiseEnCirculation(), request.getPuissanceFiscale(),
				request.getValeurNeuve(), request.getValeurVenale(), calcul.getPrice());
		return toDto(simulationRepository.save(simulation), calcul);
	}

	@Transactional(readOnly = true)
	public Simulation rechercherEntite(Long id) {
		Simulation simulation = simulationRepository.findById(id)
				.orElseThrow(() -> AssuranceException.notFound("SIMULATION_INTROUVABLE", "Simulation introuvable."));
		verifierAccesSimulation(simulation);
		return simulation;
	}

	private SimulationResponseDto toDto(Simulation simulation) {
		PrimeCalculationResult calcul = primeCalculatorService.calculer(simulation.getProduitAssurance(),
				simulation.getDatePremiereMiseEnCirculation(), simulation.getPuissanceFiscale(),
				simulation.getValeurNeuve(), simulation.getValeurVenale());
		return toDto(simulation, calcul);
	}

	private SimulationResponseDto toDto(Simulation simulation, PrimeCalculationResult calcul) {
		Utilisateur utilisateur = simulation.getUtilisateur();
		boolean souscrite = souscriptionRepository.existsBySimulationId(simulation.getId());
		return new SimulationResponseDto(simulation.getId(), simulation.getQuoteReference(), simulation.getEndDate(),
				simulation.getProduitAssurance().getCode(), simulation.getProduitAssurance().getNom(),
				simulation.getCategorieVehicule().getCode(), simulation.getDatePremiereMiseEnCirculation(),
				simulation.getPuissanceFiscale(), simulation.getValeurNeuve(), simulation.getValeurVenale(),
				simulation.getPrice(), calcul.getGaranties(), utilisateur == null ? null : utilisateur.getId(),
				utilisateur == null ? "-" : nomComplet(utilisateur), souscrite, !souscrite);
	}

	private void validerSimulationRequest(SimulationRequestDto request) {
		if (request == null) {
			throw AssuranceException.badRequest("REQUETE_INVALIDE", "La requête de simulation est obligatoire.");
		}
		verifierTexte(request.getProduitCode(), "Le code produit est obligatoire.");
		verifierTexte(request.getCategorieCode(), "Le code catégorie est obligatoire.");
		verifierDate(request.getDatePremiereMiseEnCirculation(), "La date de première mise en circulation est obligatoire.");
		verifierEntierPositif(request.getPuissanceFiscale(), "La puissance fiscale doit être supérieure à 0.");
		verifierMontantPositif(request.getValeurNeuve(), "La valeur neuve doit être supérieure à 0.");
		verifierMontantPositif(request.getValeurVenale(), "La valeur vénale doit être supérieure à 0.");
	}

	private void verifierTexte(String valeur, String message) {
		if (valeur == null || valeur.trim().isEmpty()) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", message);
		}
	}

	private void verifierDate(LocalDate valeur, String message) {
		if (valeur == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", message);
		}
		if (valeur.isAfter(LocalDate.now())) {
			throw AssuranceException.badRequest("DATE_INVALIDE", "La date de première mise en circulation ne peut pas être future.");
		}
	}

	private void verifierEntierPositif(Integer valeur, String message) {
		if (valeur == null || valeur <= 0) {
			throw AssuranceException.badRequest("VALEUR_INVALIDE", message);
		}
	}

	private void verifierMontantPositif(BigDecimal valeur, String message) {
		if (valeur == null || valeur.compareTo(BigDecimal.ZERO) <= 0) {
			throw AssuranceException.badRequest("VALEUR_INVALIDE", message);
		}
	}

	private String normaliser(String valeur) {
		return valeur.trim().toUpperCase();
	}

	private java.util.Optional<Utilisateur> utilisateurConnecteOptionnel() {
		String username = SecurityUtils.lireLoginUtilisateurConnecte();
		if (SecurityUtils.DEFAULT_LOGIN.equals(username)) {
			return java.util.Optional.empty();
		}
		return utilisateurRepository.rechercherParUsername(username);
	}

	private Utilisateur utilisateurConnecteObligatoire() {
		String username = SecurityUtils.lireLoginUtilisateurConnecte();
		return utilisateurRepository.rechercherParUsername(username)
				.orElseThrow(() -> AssuranceException.badRequest("UTILISATEUR_CONNECTE_INTROUVABLE",
						"Impossible de retrouver l'utilisateur connecté."));
	}

	private void verifierAccesSimulation(Simulation simulation) {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		if (utilisateur.getRole() == Role.ADMIN) {
			return;
		}
		if (simulation.getUtilisateur() != null && simulation.getUtilisateur().getId().equals(utilisateur.getId())) {
			return;
		}
		throw AssuranceException.notFound("SIMULATION_INTROUVABLE", "Simulation introuvable.");
	}

	private String nomComplet(Utilisateur utilisateur) {
		String prenoms = valeurOuVide(utilisateur.getPrenoms());
		String nom = valeurOuVide(utilisateur.getNom());
		String nomComplet = (prenoms + " " + nom).trim();
		return nomComplet.isEmpty() ? utilisateur.getUsername() : nomComplet;
	}

	private String valeurOuVide(String valeur) {
		return valeur == null ? "" : valeur.trim();
	}

	private String genererReference(String prefix) {
		return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	}
}
