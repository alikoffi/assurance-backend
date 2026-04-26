package com.eburtis.assurance.controller;

import com.eburtis.assurance.presentation.dto.simulation.SimulationRequestDto;
import com.eburtis.assurance.presentation.dto.simulation.SimulationResponseDto;
import com.eburtis.assurance.service.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/simulations")
public class SimulationController {
	private final SimulationService simulationService;

	public SimulationController(SimulationService simulationService) {
		this.simulationService = simulationService;
	}

	@GetMapping
	public List<SimulationResponseDto> lister() {
		return simulationService.lister();
	}

	@PostMapping
	public SimulationResponseDto creer(@RequestBody SimulationRequestDto request) {
		return simulationService.creer(request);
	}

	@GetMapping("/{id}")
	public SimulationResponseDto rechercher(@PathVariable Long id) {
		return simulationService.rechercher(id);
	}
}
