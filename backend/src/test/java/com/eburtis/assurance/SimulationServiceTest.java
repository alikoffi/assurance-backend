package com.eburtis.assurance;

import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.simulation.SimulationResponseDto;
import com.eburtis.assurance.service.SimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SimulationServiceTest {

	private final SimulationService simulationService;

	@Autowired
	SimulationServiceTest(SimulationService simulationService) {
		this.simulationService = simulationService;
	}

	@Test
	void creerSimulationPapillonCalculeLaPrime() {
		SimulationRequestDto request = new SimulationRequestDto();
		request.setProduitCode("PAPILLON");
		request.setCategorieCode("201");
		request.setDatePremiereMiseEnCirculation(LocalDate.now().minusYears(2));
		request.setPuissanceFiscale(3);
		request.setValeurNeuve(new BigDecimal("10000000"));
		request.setValeurVenale(new BigDecimal("6000000"));

		SimulationResponseDto response = simulationService.creer(request);

		// PAPILLON = RC 45 181 + DOMMAGES 2,60% valeur neuve + VOL 0,14% valeur venale.
		assertEquals(new BigDecimal("313581"), response.getPrice());
		assertEquals(3, response.getGaranties().size());
	}
}
