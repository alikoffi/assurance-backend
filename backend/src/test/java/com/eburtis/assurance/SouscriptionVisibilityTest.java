package com.eburtis.assurance;

import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.AssureDto;
import com.eburtis.assurance.presentation.dto.souscription.AssureResponseDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionResponseDto;
import com.eburtis.assurance.presentation.dto.souscription.VehiculeDto;
import com.eburtis.assurance.service.AssureService;
import com.eburtis.assurance.service.SimulationService;
import com.eburtis.assurance.service.SouscriptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class SouscriptionVisibilityTest {

	private final SimulationService simulationService;
	private final SouscriptionService souscriptionService;
	private final AssureService assureService;

	@Autowired
	SouscriptionVisibilityTest(SimulationService simulationService, SouscriptionService souscriptionService,
			AssureService assureService) {
		this.simulationService = simulationService;
		this.souscriptionService = souscriptionService;
		this.assureService = assureService;
	}

	@AfterEach
	void nettoyerContexteSecurite() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void amazoneVoitUniquementSesSouscriptionsEtAssuresEtAdminVoitTout() {
		connecter("amazone1");
		SouscriptionResponseDto souscriptionAmazone1 = souscrire("CI-1111-AA", "Awa", "Kouame");

		connecter("amazone2");
		SouscriptionResponseDto souscriptionAmazone2 = souscrire("CI-2222-BB", "Mariam", "Traore");
		Long assureAmazone2Id = assureService.lister().stream()
				.filter(assure -> "Traore".equals(assure.getNom()))
				.findFirst()
				.orElseThrow()
				.getId();

		connecter("amazone1");
		List<Long> idsAmazone1 = souscriptionService.lister().stream()
				.map(SouscriptionResponseDto::getId)
				.toList();
		assertTrue(idsAmazone1.contains(souscriptionAmazone1.getId()));
		assertFalse(idsAmazone1.contains(souscriptionAmazone2.getId()));
		assertThrows(AssuranceException.class, () -> souscriptionService.rechercher(souscriptionAmazone2.getId()));
		List<String> nomsAssuresAmazone1 = assureService.lister().stream()
				.map(AssureResponseDto::getNom)
				.toList();
		assertTrue(nomsAssuresAmazone1.contains("Kouame"));
		assertFalse(nomsAssuresAmazone1.contains("Traore"));
		assertThrows(AssuranceException.class, () -> assureService.rechercher(assureAmazone2Id));

		connecter("admin");
		List<Long> idsAdmin = souscriptionService.lister().stream()
				.map(SouscriptionResponseDto::getId)
				.toList();
		assertTrue(idsAdmin.contains(souscriptionAmazone1.getId()));
		assertTrue(idsAdmin.contains(souscriptionAmazone2.getId()));
		assertDoesNotThrow(() -> souscriptionService.rechercher(souscriptionAmazone2.getId()));
		List<String> nomsAssuresAdmin = assureService.lister().stream()
				.map(AssureResponseDto::getNom)
				.toList();
		assertTrue(nomsAssuresAdmin.contains("Kouame"));
		assertTrue(nomsAssuresAdmin.contains("Traore"));
		assertDoesNotThrow(() -> assureService.rechercher(assureAmazone2Id));
	}

	private SouscriptionResponseDto souscrire(String immatriculation, String prenom, String nom) {
		Long simulationId = simulationService.creer(simulationRequest()).getId();
		SouscriptionRequestDto request = new SouscriptionRequestDto();
		request.setSimulationId(simulationId);
		request.setAssure(assure(prenom, nom));
		request.setVehicule(vehicule(immatriculation));
		return souscriptionService.souscrire(request);
	}

	private SimulationRequestDto simulationRequest() {
		SimulationRequestDto request = new SimulationRequestDto();
		request.setProduitCode("PAPILLON");
		request.setCategorieCode("201");
		request.setDatePremiereMiseEnCirculation(LocalDate.now().minusYears(2));
		request.setPuissanceFiscale(3);
		request.setValeurNeuve(new BigDecimal("10000000"));
		request.setValeurVenale(new BigDecimal("6000000"));
		return request;
	}

	private AssureDto assure(String prenom, String nom) {
		AssureDto assure = new AssureDto();
		assure.setAdresse("Cocody");
		assure.setTelephone("0102030405");
		assure.setNom(nom);
		assure.setPrenom(prenom);
		assure.setNumeroCarteIdentite("CI-" + prenom.toUpperCase());
		assure.setVille("Abidjan");
		return assure;
	}

	private VehiculeDto vehicule(String immatriculation) {
		VehiculeDto vehicule = new VehiculeDto();
		vehicule.setNumeroImmatriculation(immatriculation);
		vehicule.setCouleur("Noir");
		vehicule.setNombreSieges(5);
		vehicule.setNombrePortes(4);
		return vehicule;
	}

	private void connecter(String username) {
		User principal = new User(username, "", List.of());
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
	}
}
