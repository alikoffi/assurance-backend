package com.eburtis.assurance;

import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.AssureDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionResponseDto;
import com.eburtis.assurance.presentation.dto.souscription.VehiculeDto;
import com.eburtis.assurance.service.AttestationService;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AttestationServiceTest {
	private final SimulationService simulationService;
	private final SouscriptionService souscriptionService;
	private final AttestationService attestationService;

	@Autowired
	AttestationServiceTest(SimulationService simulationService, SouscriptionService souscriptionService,
			AttestationService attestationService) {
		this.simulationService = simulationService;
		this.souscriptionService = souscriptionService;
		this.attestationService = attestationService;
	}

	@AfterEach
	void nettoyerContexteSecurite() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void genererRetourneUnPdfAvecContenu() {
		connecter("amazone1");
		SouscriptionResponseDto souscription = souscrire();

		byte[] contenu = attestationService.generer(souscription.getId());

		assertTrue(contenu.length > 1000);
		assertTrue(new String(contenu, 0, 4, StandardCharsets.US_ASCII).startsWith("%PDF"));
	}

	private SouscriptionResponseDto souscrire() {
		Long simulationId = simulationService.creer(simulationRequest()).getId();
		SouscriptionRequestDto request = new SouscriptionRequestDto();
		request.setSimulationId(simulationId);
		request.setAssure(assure());
		request.setVehicule(vehicule());
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

	private AssureDto assure() {
		AssureDto assure = new AssureDto();
		assure.setAdresse("Cocody");
		assure.setTelephone("0102030405");
		assure.setNom("Kouame");
		assure.setPrenom("Awa");
		assure.setNumeroCarteIdentite("CI-AWA");
		assure.setVille("Abidjan");
		return assure;
	}

	private VehiculeDto vehicule() {
		VehiculeDto vehicule = new VehiculeDto();
		vehicule.setNumeroImmatriculation("CI-1234-AA");
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
