package com.eburtis.assurance.controller;

import com.eburtis.assurance.presentation.dto.souscription.SouscriptionRequestDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionResponseDto;
import com.eburtis.assurance.presentation.dto.souscription.SouscriptionStatusDto;
import com.eburtis.assurance.service.AttestationService;
import com.eburtis.assurance.service.SouscriptionService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/subscriptions")
public class SouscriptionController {
	private final SouscriptionService souscriptionService;
	private final AttestationService attestationService;

	public SouscriptionController(SouscriptionService souscriptionService, AttestationService attestationService) {
		this.souscriptionService = souscriptionService;
		this.attestationService = attestationService;
	}

	@PostMapping
	public SouscriptionResponseDto souscrire(@RequestBody SouscriptionRequestDto request) {
		return souscriptionService.souscrire(request);
	}

	@GetMapping
	public List<SouscriptionResponseDto> lister() {
		return souscriptionService.lister();
	}

	@GetMapping("/{id}")
	public SouscriptionResponseDto rechercher(@PathVariable Long id) {
		return souscriptionService.rechercher(id);
	}

	@GetMapping("/status/{id}")
	public SouscriptionStatusDto statut(@PathVariable Long id) {
		return souscriptionService.statut(id);
	}

	@GetMapping("/{id}/attestation")
	public ResponseEntity<byte[]> attestation(@PathVariable Long id) {
		byte[] contenu = attestationService.generer(id);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.inline().filename("attestation-" + id + ".pdf").build().toString())
				.body(contenu);
	}
}
