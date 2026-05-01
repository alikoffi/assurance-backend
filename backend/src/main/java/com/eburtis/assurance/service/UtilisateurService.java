package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Utilisateur;
import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutUtilisateur;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.utilisateur.ChangementMotDePasseDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurRequestDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurResponseDto;
import com.eburtis.assurance.repository.UtilisateurRepository;
import com.eburtis.assurance.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtilisateurService {
	private final UtilisateurRepository utilisateurRepository;

	public UtilisateurService(UtilisateurRepository utilisateurRepository) {
		this.utilisateurRepository = utilisateurRepository;
	}

	@Transactional(readOnly = true)
	public List<UtilisateurResponseDto> lister() {
		verifierAdmin();
		return utilisateurRepository.listerOrdonnes().stream()
				.map(this::toDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public UtilisateurResponseDto rechercher(Long id) {
		verifierAdmin();
		return toDto(rechercherEntite(id));
	}

	@Transactional
	public UtilisateurResponseDto creer(UtilisateurRequestDto request) {
		verifierAdmin();
		validerCreation(request);

		String username = request.getUsername().trim();
		verifierUsernameDisponible(username, null);

		Utilisateur utilisateur = new Utilisateur(username, SecurityService.crypterPassword(request.getPassword().trim()),
				request.getNom().trim(), request.getPrenoms().trim(), request.getRole(), request.getStatut(), true);

		return toDto(utilisateurRepository.save(utilisateur));
	}

	@Transactional
	public UtilisateurResponseDto modifier(Long id, UtilisateurRequestDto request) {
		verifierAdmin();
		validerModification(request);

		Utilisateur utilisateur = rechercherEntite(id);
		String username = request.getUsername().trim();
		verifierUsernameDisponible(username, id);

		String password = utilisateur.getPassword();
		boolean mustChangePassword = utilisateur.isMustChangePassword();
		if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
			password = SecurityService.crypterPassword(request.getPassword().trim());
			mustChangePassword = true;
		}

		utilisateur.mettreAJourUtilisateur(username, password, request.getNom().trim(), request.getPrenoms().trim(),
				request.getRole(), request.getStatut(), mustChangePassword);

		return toDto(utilisateurRepository.save(utilisateur));
	}

	@Transactional
	public UtilisateurResponseDto changerMotDePasse(ChangementMotDePasseDto request) {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		validerChangementMotDePasse(request);

		if (!SecurityService.comparerPassword(request.getAncienMotDePasse().trim(), utilisateur.getPassword())) {
			throw AssuranceException.badRequest("ANCIEN_MOT_DE_PASSE_INCORRECT", "L'ancien mot de passe est incorrect.");
		}

		if (SecurityService.comparerPassword(request.getNouveauMotDePasse().trim(), utilisateur.getPassword())) {
			throw AssuranceException.badRequest("MOT_DE_PASSE_IDENTIQUE",
					"Le nouveau mot de passe doit être différent de l'ancien.");
		}

		utilisateur.setPassword(SecurityService.crypterPassword(request.getNouveauMotDePasse().trim()));
		utilisateur.setMustChangePassword(false);
		return toDto(utilisateurRepository.save(utilisateur));
	}

	private Utilisateur rechercherEntite(Long id) {
		return utilisateurRepository.findById(id)
				.orElseThrow(() -> AssuranceException.notFound("UTILISATEUR_INTROUVABLE", "Utilisateur introuvable."));
	}

	private void verifierAdmin() {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		if (utilisateur.getRole() != Role.ADMIN) {
			throw AssuranceException.forbidden("ACCES_REFUSE", "Seul un administrateur peut gérer les utilisateurs.");
		}
	}

	private Utilisateur utilisateurConnecteObligatoire() {
		String username = SecurityUtils.lireLoginUtilisateurConnecte();
		return utilisateurRepository.rechercherParUsername(username)
				.orElseThrow(() -> AssuranceException.badRequest("UTILISATEUR_CONNECTE_INTROUVABLE",
						"Impossible de retrouver l'utilisateur connecté."));
	}

	private void verifierUsernameDisponible(String username, Long idUtilisateurModifie) {
		utilisateurRepository.rechercherParUsername(username)
				.filter(utilisateur -> idUtilisateurModifie == null || !utilisateur.getId().equals(idUtilisateurModifie))
				.ifPresent(utilisateur -> {
					throw AssuranceException.badRequest("USERNAME_DEJA_UTILISE", "Ce nom d'utilisateur est déjà utilisé.");
				});
	}

	private void validerCreation(UtilisateurRequestDto request) {
		validerCommun(request);
		verifierTexte(request.getPassword(), "Le mot de passe est obligatoire.");
	}

	private void validerModification(UtilisateurRequestDto request) {
		validerCommun(request);
	}

	private void validerChangementMotDePasse(ChangementMotDePasseDto request) {
		if (request == null) {
			throw AssuranceException.badRequest("REQUETE_INVALIDE", "La requête de changement de mot de passe est obligatoire.");
		}
		verifierTexte(request.getAncienMotDePasse(), "L'ancien mot de passe est obligatoire.");
		verifierTexte(request.getNouveauMotDePasse(), "Le nouveau mot de passe est obligatoire.");
		verifierTexte(request.getConfirmationMotDePasse(), "La confirmation du mot de passe est obligatoire.");
		if (!request.getNouveauMotDePasse().trim().equals(request.getConfirmationMotDePasse().trim())) {
			throw AssuranceException.badRequest("MOTS_DE_PASSE_DIFFERENTS",
					"Le nouveau mot de passe et sa confirmation doivent être identiques.");
		}
		if (request.getNouveauMotDePasse().trim().length() < 6) {
			throw AssuranceException.badRequest("MOT_DE_PASSE_TROP_COURT",
					"Le nouveau mot de passe doit contenir au moins 6 caractères.");
		}
	}

	private void validerCommun(UtilisateurRequestDto request) {
		if (request == null) {
			throw AssuranceException.badRequest("REQUETE_INVALIDE", "La requête utilisateur est obligatoire.");
		}
		verifierTexte(request.getUsername(), "Le nom d'utilisateur est obligatoire.");
		verifierTexte(request.getNom(), "Le nom est obligatoire.");
		verifierTexte(request.getPrenoms(), "Les prénoms sont obligatoires.");
		if (request.getRole() == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", "Le rôle est obligatoire.");
		}
		if (request.getStatut() == null) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", "Le statut est obligatoire.");
		}
	}

	private void verifierTexte(String valeur, String message) {
		if (valeur == null || valeur.trim().isEmpty()) {
			throw AssuranceException.badRequest("CHAMP_OBLIGATOIRE", message);
		}
	}

	private UtilisateurResponseDto toDto(Utilisateur utilisateur) {
		return new UtilisateurResponseDto(utilisateur.getId(), utilisateur.getUsername(), utilisateur.getNom(),
				utilisateur.getPrenoms(), utilisateur.getRole(), utilisateur.getStatut(), utilisateur.isMustChangePassword());
	}
}
