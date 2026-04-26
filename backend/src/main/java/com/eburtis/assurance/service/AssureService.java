package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Assure;
import com.eburtis.assurance.domain.Utilisateur;
import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.souscription.AssureResponseDto;
import com.eburtis.assurance.repository.AssureRepository;
import com.eburtis.assurance.repository.UtilisateurRepository;
import com.eburtis.assurance.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssureService {
	private final AssureRepository assureRepository;
	private final UtilisateurRepository utilisateurRepository;

	public AssureService(AssureRepository assureRepository, UtilisateurRepository utilisateurRepository) {
		this.assureRepository = assureRepository;
		this.utilisateurRepository = utilisateurRepository;
	}

	@Transactional(readOnly = true)
	public List<AssureResponseDto> lister() {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		List<Assure> assures = utilisateur.getRole() == Role.ADMIN
				? assureRepository.findAll()
				: assureRepository.rechercherParUtilisateurId(utilisateur.getId());

		return assures.stream()
				.map(this::toDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public AssureResponseDto rechercher(Long id) {
		Utilisateur utilisateur = utilisateurConnecteObligatoire();
		Assure assure = assureRepository.findById(id)
				.orElseThrow(() -> AssuranceException.notFound("ASSURE_INTROUVABLE", "Assure introuvable."));

		if (utilisateur.getRole() != Role.ADMIN && !assureRepository.existePourUtilisateur(id, utilisateur.getId())) {
			throw AssuranceException.notFound("ASSURE_INTROUVABLE", "Assure introuvable.");
		}

		return toDto(assure);
	}

	private Utilisateur utilisateurConnecteObligatoire() {
		String username = SecurityUtils.lireLoginUtilisateurConnecte();
		return utilisateurRepository.rechercherParUsername(username)
				.orElseThrow(() -> AssuranceException.badRequest("UTILISATEUR_CONNECTE_INTROUVABLE",
						"Impossible de retrouver l'utilisateur connecte."));
	}

	private AssureResponseDto toDto(Assure assure) {
		return new AssureResponseDto(assure.getId(), assure.getAdresse(), assure.getTelephone(), assure.getNom(),
				assure.getPrenom(), assure.getNumeroCarteIdentite(), assure.getVille());
	}
}
