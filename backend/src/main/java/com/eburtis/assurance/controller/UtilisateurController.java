package com.eburtis.assurance.controller;

import com.eburtis.assurance.presentation.dto.utilisateur.ChangementMotDePasseDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurRequestDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurResponseDto;
import com.eburtis.assurance.service.UtilisateurService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/utilisateurs")
public class UtilisateurController {
	private final UtilisateurService utilisateurService;

	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	@GetMapping
	public List<UtilisateurResponseDto> lister() {
		return utilisateurService.lister();
	}

	@GetMapping("/{id}")
	public UtilisateurResponseDto rechercher(@PathVariable Long id) {
		return utilisateurService.rechercher(id);
	}

	@PostMapping
	public UtilisateurResponseDto creer(@RequestBody UtilisateurRequestDto request) {
		return utilisateurService.creer(request);
	}

	@PutMapping("/{id}")
	public UtilisateurResponseDto modifier(@PathVariable Long id, @RequestBody UtilisateurRequestDto request) {
		return utilisateurService.modifier(id, request);
	}

	@PutMapping("/me/mot-de-passe")
	public UtilisateurResponseDto changerMotDePasse(@RequestBody ChangementMotDePasseDto request) {
		return utilisateurService.changerMotDePasse(request);
	}
}
