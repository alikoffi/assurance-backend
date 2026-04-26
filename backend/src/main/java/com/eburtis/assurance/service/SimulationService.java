package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.CategorieVehicule;
import com.eburtis.assurance.domain.ProduitAssurance;
import com.eburtis.assurance.domain.Simulation;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.simulation.SimulationResponseDto;
import com.eburtis.assurance.repository.CategorieVehiculeRepository;
import com.eburtis.assurance.repository.ProduitAssuranceRepository;
import com.eburtis.assurance.repository.SimulationRepository;
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

	public SimulationService(SimulationRepository simulationRepository, ProduitAssuranceRepository produitAssuranceRepository,
			CategorieVehiculeRepository categorieVehiculeRepository, PrimeCalculatorService primeCalculatorService) {
		this.simulationRepository = simulationRepository;
		this.produitAssuranceRepository = produitAssuranceRepository;
		this.categorieVehiculeRepository = categorieVehiculeRepository;
		this.primeCalculatorService = primeCalculatorService;
	}

	@Transactional(readOnly = true)
	public List<SimulationResponseDto> lister() {
		return simulationRepository.findAll().stream()
				.map(this::toDto)
				.toList();
	}

	@Transactional
	public SimulationResponseDto creer(SimulationRequestDto request) {
		validerSimulationRequest(request);

		ProduitAssurance produit = produitAssuranceRepository.findByCodeIgnoreCaseAndActifTrue(normaliser(request.getProduitCode()))
				.orElseThrow(() -> AssuranceException.notFound("PRODUIT_INTROUVABLE", "Produit d'assurance introuvable."));
		CategorieVehicule categorie = categorieVehiculeRepository.findByCode(normaliser(request.getCategorieCode()))
				.orElseThrow(() -> AssuranceException.notFound("CATEGORIE_INTROUVABLE", "Categorie de vehicule introuvable."));

		if (!produit.estEligiblePour(categorie)) {
			throw AssuranceException.badRequest("PRODUIT_NON_ELIGIBLE",
					"Le produit " + produit.getCode() + " n'est pas disponible pour la categorie " + categorie.getCode() + ".");
		}

		PrimeCalculationResult calcul = primeCalculatorService.calculer(produit, request.getDatePremiereMiseEnCirculation(),
				request.getPuissanceFiscale(), request.getValeurNeuve(), request.getValeurVenale());

		Simulation simulation = new Simulation(genererReference("QT"), LocalDate.now().plusWeeks(2), produit, categorie,
				request.getDatePremiereMiseEnCirculation(), request.getPuissanceFiscale(),
				request.getValeurNeuve(), request.getValeurVenale(), calcul.getPrice());

		return toDto(simulationRepository.save(simulation), calcul);
	}

	@Transactional(readOnly = true)
	public SimulationResponseDto rechercher(Long id) {
		return toDto(rechercherEntite(id));
	}

	@Transactional(readOnly = true)
	public Simulation rechercherEntite(Long id) {
		return simulationRepository.findById(id)
				.orElseThrow(() -> AssuranceException.notFound("SIMULATION_INTROUVABLE", "Simulation introuvable."));
	}

	private SimulationResponseDto toDto(Simulation simulation) {
		PrimeCalculationResult calcul = primeCalculatorService.calculer(simulation.getProduitAssurance(),
				simulation.getDatePremiereMiseEnCirculation(), simulation.getPuissanceFiscale(),
				simulation.getValeurNeuve(), simulation.getValeurVenale());
		return toDto(simulation, calcul);
	}

	private SimulationResponseDto toDto(Simulation simulation, PrimeCalculationResult calcul) {
		return new SimulationResponseDto(simulation.getId(), simulation.getQuoteReference(), simulation.getEndDate(),
				simulation.getProduitAssurance().getCode(), simulation.getProduitAssurance().getNom(),
				simulation.getCategorieVehicule().getCode(), simulation.getDatePremiereMiseEnCirculation(),
				simulation.getPuissanceFiscale(), simulation.getValeurNeuve(), simulation.getValeurVenale(),
				simulation.getPrice(), calcul.getGaranties());
	}

	private void validerSimulationRequest(SimulationRequestDto request) {
		if (request == null) {
			throw AssuranceException.badRequest("REQUETE_INVALIDE", "La requete de simulation est obligatoire.");
		}
		verifierTexte(request.getProduitCode(), "Le code produit est obligatoire.");
		verifierTexte(request.getCategorieCode(), "Le code categorie est obligatoire.");
		verifierDate(request.getDatePremiereMiseEnCirculation(), "La date de premiere mise en circulation est obligatoire.");
		verifierEntierPositif(request.getPuissanceFiscale(), "La puissance fiscale doit etre superieure a 0.");
		verifierMontantPositif(request.getValeurNeuve(), "La valeur neuve doit etre superieure a 0.");
		verifierMontantPositif(request.getValeurVenale(), "La valeur venale doit etre superieure a 0.");
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
			throw AssuranceException.badRequest("DATE_INVALIDE", "La date de premiere mise en circulation ne peut pas etre future.");
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

	private String genererReference(String prefix) {
		return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	}
}
