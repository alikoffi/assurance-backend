package com.eburtis.assurance;

import com.eburtis.assurance.enums.Role;
import com.eburtis.assurance.enums.StatutUtilisateur;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.auth.AuthDto;
import com.eburtis.assurance.presentation.dto.auth.TokenDto;
import com.eburtis.assurance.presentation.dto.utilisateur.ChangementMotDePasseDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurRequestDto;
import com.eburtis.assurance.presentation.dto.utilisateur.UtilisateurResponseDto;
import com.eburtis.assurance.service.SecurityService;
import com.eburtis.assurance.service.UtilisateurService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UtilisateurServiceTest {
	private final UtilisateurService utilisateurService;
	private final SecurityService securityService;

	@Autowired
	UtilisateurServiceTest(UtilisateurService utilisateurService, SecurityService securityService) {
		this.utilisateurService = utilisateurService;
		this.securityService = securityService;
	}

	@AfterEach
	void nettoyerContexteSecurite() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void adminPeutCreerEtModifierUnUtilisateur() {
		connecter("admin");

		UtilisateurResponseDto utilisateur = utilisateurService.creer(request("amazone.test", "Awa", "Kouame",
				Role.AMAZONE, StatutUtilisateur.ACTIF, "password"));

		assertEquals("amazone.test", utilisateur.getUsername());
		assertEquals(Role.AMAZONE, utilisateur.getRole());
		assertTrue(utilisateur.isMustChangePassword());

		UtilisateurResponseDto utilisateurModifie = utilisateurService.modifier(utilisateur.getId(),
				request("amazone.test", "Awa Modifiee", "Kouame", Role.AMAZONE, StatutUtilisateur.INACTIF, null));

		assertEquals("Awa Modifiee", utilisateurModifie.getPrenoms());
		assertEquals(StatutUtilisateur.INACTIF, utilisateurModifie.getStatut());
		assertTrue(utilisateurModifie.isMustChangePassword());
		assertTrue(utilisateurService.lister().stream()
				.map(UtilisateurResponseDto::getId)
				.toList()
				.contains(utilisateur.getId()));
	}

	@Test
	void utilisateurPeutChangerSonMotDePasseTemporaire() {
		connecter("admin");
		UtilisateurResponseDto utilisateur = utilisateurService.creer(request("amazone.change", "Awa", "Kouame",
				Role.AMAZONE, StatutUtilisateur.ACTIF, "password"));

		TokenDto tokenAvantChangement = securityService.autentifier(new AuthDto("amazone.change", "password"));
		assertTrue(tokenAvantChangement.isMustChangePassword());

		connecter("amazone.change");
		UtilisateurResponseDto utilisateurModifie = utilisateurService.changerMotDePasse(
				changementMotDePasse("password", "newPassword", "newPassword"));

		assertEquals(utilisateur.getId(), utilisateurModifie.getId());
		assertFalse(utilisateurModifie.isMustChangePassword());
		assertFalse(securityService.autentifier(new AuthDto("amazone.change", "newPassword")).isMustChangePassword());
		assertThrows(RuntimeException.class, () -> securityService.autentifier(new AuthDto("amazone.change", "password")));
	}

	@Test
	void amazoneNePeutPasGererLesUtilisateurs() {
		connecter("amazone1");

		assertThrows(AssuranceException.class, () -> utilisateurService.lister());
		assertThrows(AssuranceException.class, () -> utilisateurService.creer(request("refuse", "Refuse", "Test",
				Role.AMAZONE, StatutUtilisateur.ACTIF, "password")));
	}

	@Test
	void usernameDoitEtreUnique() {
		connecter("admin");

		utilisateurService.creer(request("doublon.test", "Awa", "Kouame", Role.AMAZONE, StatutUtilisateur.ACTIF,
				"password"));

		assertThrows(AssuranceException.class, () -> utilisateurService.creer(request("doublon.test", "Mariam",
				"Traore", Role.AMAZONE, StatutUtilisateur.ACTIF, "password")));
	}

	private UtilisateurRequestDto request(String username, String prenoms, String nom, Role role,
			StatutUtilisateur statut, String password) {
		UtilisateurRequestDto request = new UtilisateurRequestDto();
		request.setUsername(username);
		request.setPrenoms(prenoms);
		request.setNom(nom);
		request.setRole(role);
		request.setStatut(statut);
		request.setPassword(password);
		return request;
	}

	private ChangementMotDePasseDto changementMotDePasse(String ancienMotDePasse, String nouveauMotDePasse,
			String confirmationMotDePasse) {
		ChangementMotDePasseDto request = new ChangementMotDePasseDto();
		request.setAncienMotDePasse(ancienMotDePasse);
		request.setNouveauMotDePasse(nouveauMotDePasse);
		request.setConfirmationMotDePasse(confirmationMotDePasse);
		return request;
	}

	private void connecter(String username) {
		User principal = new User(username, "", List.of());
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
	}
}
