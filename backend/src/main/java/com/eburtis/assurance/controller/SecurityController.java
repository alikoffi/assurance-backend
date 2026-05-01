package com.eburtis.assurance.controller;

import com.eburtis.assurance.service.SecurityService;
import com.eburtis.assurance.configuration.logger.Logged;
import com.eburtis.assurance.presentation.dto.auth.AuthDto;
import com.eburtis.assurance.presentation.dto.auth.TokenDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/securite")
public class SecurityController {

	private final SecurityService securityService;

	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Permet d'authentifier l'utilisateur.
	 *
	 * @param authDto les informations de connexion.
	 * @return le token d'authentification
	 */
	@PostMapping("/auth")
	@Logged
	public TokenDto authentification(@RequestBody AuthDto authDto) {
		return securityService.authentifier(authDto);
	}

}
