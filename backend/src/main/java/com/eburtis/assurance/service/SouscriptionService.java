package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Assure;
import com.eburtis.assurance.domain.CategorieVehicule;
import com.eburtis.assurance.domain.Simulation;
import com.eburtis.assurance.domain.Souscription;
import com.eburtis.assurance.domain.Utilisateur;
import com.eburtis.assurance.domain.Vehicule;
import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutSouscription;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.souscription.AssureDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionResponseDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionStatusDto;
import com.eburtis.assurance.presentation.dto.souscription.VehiculeDto;
import com.eburtis.assurance.repository.AssureRepository;
import com.eburtis.assurance.repository.CategorieVehiculeRepository;
import com.eburtis.assurance.repository.SouscriptionRepository;
import com.eburtis.assurance.repository.UtilisateurRepository;
import com.eburtis.assurance.repository.VehiculeRepository;
import com.eburtis.assurance.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SouscriptionService {
	private final SimulationService simulationService;
	private final CategorieVehiculeRepository categorieVehiculeRepository;
	private final AssureRepository assureRepository;
	private final VehiculeRepository vehiculeRepository;
	private final SouscriptionRepository souscriptionRepository;
	private final UtilisateurRepository utilisateurRepository;

	public SouscriptionService(SimulationService simulationService, CategorieVehiculeRepository categorieVehiculeRepository,
			AssureRepository assureRepository, VehiculeRepository vehiculeRepository,
			SouscriptionRepository souscriptionRepository, UtilisateurRepository utilisateurRepository) {
		this.simulationService = simulationService;
		this.categorieVehiculeRepository = categorieVehiculeRepository;
		this.assureRepository = assureRepository;
		this.vehiculeRepository = vehiculeRepository;
		this.souscriptionRepository = souscriptionRepository;
		this.utilisateurRepository = utilisateurRepository;
	}

	@Transactional
	public SouscriptionResponseDto souscrire(SouscriptionRequestDto request) {
		validerSouscriptionRequest(request);

		Simulation simulation = simulationService.rechercherEntite(request.getSimulationId());
		if (simulation.getEndDate().isBefore(LocalDate.now())) {
			throw AssuranceException.badRequest("DEVIS_EXPIRE", "Le devis n'est plus valide.");
		}

		CategorieVehicule categorie = rechercherCategorieVehicule(request.getVehicule(), simulation);
		validerCoherenceVehicule(request.getVehicule(), simulation, categorie);

		Assure assure = assureRepository.save(creerAssure(request.getAssure()));
		Vehicule vehicule = vehiculeRepository.save(creerVehicule(request.getVehicule(), simulation, categorie));
		Utilisateur utilisateur = utilisateurConnecteObligatoire();

		Souscription souscription = new Souscription(genererReference("SUB"), simulation, assure, vehicule, utilisateur,
				StatutSouscription.VALIDEE, genererReference("AT"), LocalDateTime.now());

		return toDto(souscriptionRepository.save(souscription));
	}

	@Transactional(readOnly = true)
	public List<SouscriptionResponseDto> lister() {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		List<Souscription> souscriptions = utilisateur.getRole() == Role.ADMIN
				? souscriptionRepository.findAll()
				: souscriptionRepository.findByUtilisateurId(utilisateur.getId());

		return souscriptions.stream()
				.map(this::toDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public SouscriptionResponseDto rechercher(Long id) {
		return toDto(rechercherEntite(id));
	}

	@Transactional(readOnly = true)
	public SouscriptionStatusDto statut(Long id) {
		Souscription souscription = rechercherEntite(id);
		return new SouscriptionStatusDto(souscription.getId(), souscription.getSubscriptionReference(), souscription.getStatut());
	}

	@Transactional(readOnly = true)
	public Souscription rechercherEntite(Long id) {
		Souscription souscription = souscriptionRepository.findById(id)
				.orElseThrow(() -> AssuranceException.notFound("SOUSCRIPTION_INTROUVABLE", "Souscription introuvable."));
		verifierAccesSouscription(souscription);
		return souscription;
	}

	private Assure creerAssure(AssureDto dto) {
		return new Assure(dto.getAdresse().trim(), dto.getTelephone().trim(), dto.getNom().trim(),
				dto.getPrenom().trim(), dto.getNumeroCarteIdentite().trim(), dto.getVille().trim());
	}

	private Vehicule creerVehicule(VehiculeDto dto, Simulation simulation, CategorieVehicule categorie) {
		return new Vehicule(valeurOuDefaut(dto.getDatePremiereMiseEnCirculation(), simulation.getDatePremiereMiseEnCirculation()),
				dto.getNumeroImmatriculation().trim(), dto.getCouleur().trim(), dto.getNombreSieges(), dto.getNombrePortes(),
				categorie, valeurOuDefaut(dto.getPuissanceFiscale(), simulation.getPuissanceFiscale()),
				valeurOuDefaut(dto.getValeurNeuve(), simulation.getValeurNeuve()),
				valeurOuDefaut(dto.getValeurVenale(), simulation.getValeurVenale()));
	}

	private CategorieVehicule rechercherCategorieVehicule(VehiculeDto vehicule, Simulation simulation) {
		if (vehicule.getCategorieCode() == null || vehicule.getCategorieCode().trim().isEmpty()) {
			return simulation.getCategorieVehicule();
		}
		return categorieVehiculeRepository.findByCode(vehicule.getCategorieCode().trim())
				.orElseThrow(() -> AssuranceException.notFound("CATEGORIE_INTROUVABLE", "Categorie de vehicule introuvable."));
	}

	private void validerCoherenceVehicule(VehiculeDto vehicule, Simulation simulation, CategorieVehicule categorie) {
		if (!categorie.getId().equals(simulation.getCategorieVehicule().getId())) {
			throw AssuranceException.badRequest("VEHICULE_INCOHERENT",
					"La categorie du vehicule doit correspondre a la categorie du devis.");
		}
		verifierMemeValeur(vehicule.getDatePremiereMiseEnCirculation(), simulation.getDatePremiereMiseEnCirculation(),
				"La date de premiere mise en circulation doit correspondre au devis.");
		verifierMemeValeur(vehicule.getPuissanceFiscale(), simulation.getPuissanceFiscale(),
				"La puissance fiscale doit correspondre au devis.");
		verifierMemeMontant(vehicule.getValeurNeuve(), simulation.getValeurNeuve(),
				"La valeur neuve doit correspondre au devis.");
		verifierMemeMontant(vehicule.getValeurVenale(), simulation.getValeurVenale(),
				"La valeur venale doit correspondre au devis.");
	}

	private void validerSouscriptionRequest(SouscriptionRequestDto request) {
		if (request == null) {
			throw AssuranceException.badRequest("REQUETE_INVALIDE", "La requete de souscription est obligatoire.");
		}
		if (request.getSimulationId() == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", "La simulation est obligatoire.");
		}
		if (request.getAssure() == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", "Les informations de l'assure sont obligatoires.");
		}
		if (request.getVehicule() == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", "Les informations du vehicule sont obligatoires.");
		}

		AssureDto assure = request.getAssure();
		verifierTexte(assure.getAdresse(), "L'adresse de l'assure est obligatoire.");
		verifierTexte(assure.getTelephone(), "Le telephone de l'assure est obligatoire.");
		verifierTexte(assure.getNom(), "Le nom de l'assure est obligatoire.");
		verifierTexte(assure.getPrenom(), "Le prenom de l'assure est obligatoire.");
		verifierTexte(assure.getNumeroCarteIdentite(), "Le numero de carte d'identite est obligatoire.");
		verifierTexte(assure.getVille(), "La ville de l'assure est obligatoire.");

		VehiculeDto vehicule = request.getVehicule();
		verifierTexte(vehicule.getNumeroImmatriculation(), "Le numero d'immatriculation est obligatoire.");
		verifierTexte(vehicule.getCouleur(), "La couleur du vehicule est obligatoire.");
		verifierEntierPositif(vehicule.getNombreSieges(), "Le nombre de sieges doit etre superieur a 0.");
		verifierEntierPositif(vehicule.getNombrePortes(), "Le nombre de portes doit etre superieur a 0.");
	}

	private Utilisateur utilisateurConnecteObligatoire() {
		String username = SecurityUtils.lireLoginUtilisateurConnecte();
		return utilisateurRepository.rechercherParUsername(username)
				.orElseThrow(() -> AssuranceException.badRequest("UTILISATEUR_CONNECTE_INTROUVABLE",
						"Impossible de retrouver l'utilisateur connecte."));
	}

	private void verifierAccesSouscription(Souscription souscription) {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		if (utilisateur.getRole() == Role.ADMIN) {
			return;
		}
		if (souscription.getUtilisateur() != null && souscription.getUtilisateur().getId().equals(utilisateur.getId())) {
			return;
		}
		throw AssuranceException.notFound("SOUSCRIPTION_INTROUVABLE", "Souscription introuvable.");
	}

	private SouscriptionResponseDto toDto(Souscription souscription) {
		return new SouscriptionResponseDto(souscription.getId(), souscription.getSubscriptionReference(),
				souscription.getSimulation().getQuoteReference(), souscription.getNumeroAttestation(),
				souscription.getStatut(), souscription.getDateSouscription(), souscription.getSimulation().getPrice());
	}

	private void verifierTexte(String valeur, String message) {
		if (valeur == null || valeur.trim().isEmpty()) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", message);
		}
	}

	private void verifierEntierPositif(Integer valeur, String message) {
		if (valeur == null || valeur <= 0) {
			throw AssuranceException.badRequest("VALEUR_INVALIDE", message);
		}
	}

	private void verifierMemeValeur(Object valeur, Object attendu, String message) {
		if (valeur != null && !Objects.equals(valeur, attendu)) {
			throw AssuranceException.badRequest("VEHICULE_INCOHERENT", message);
		}
	}

	private void verifierMemeMontant(BigDecimal valeur, BigDecimal attendu, String message) {
		if (valeur != null && valeur.compareTo(attendu) != 0) {
			throw AssuranceException.badRequest("VEHICULE_INCOHERENT", message);
		}
	}

	private <T> T valeurOuDefaut(T valeur, T defaut) {
		return valeur == null ? defaut : valeur;
	}

	private String genererReference(String prefix) {
		return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	}
}
