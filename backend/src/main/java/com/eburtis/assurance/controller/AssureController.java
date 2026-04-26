package com.eburtis.assurance.controller;

import com.eburtis.assurance.presentation.dto.souscription.AssureResponseDto;
import com.eburtis.assurance.service.AssureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/assures")
public class AssureController {
	private final AssureService assureService;

	public AssureController(AssureService assureService) {
		this.assureService = assureService;
	}

	@GetMapping
	public List<AssureResponseDto> lister() {
		return assureService.lister();
	}

	@GetMapping("/{id}")
	public AssureResponseDto rechercher(@PathVariable Long id) {
		return assureService.rechercher(id);
	}
}
