package com.eburtis.assurance.presentation.dto.souscription;

import com.eburtis.assurance.enums.StatutSouscription;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SouscriptionResponseDto {
	private Long id;
	private String subscriptionReference;
	private String quoteReference;
	private String numeroAttestation;
	private StatutSouscription statut;
	private LocalDateTime dateSouscription;
	private BigDecimal price;

	public SouscriptionResponseDto() {
	}

	public SouscriptionResponseDto(Long id, String subscriptionReference, String quoteReference, String numeroAttestation,
			StatutSouscription statut, LocalDateTime dateSouscription, BigDecimal price) {
		this.id = id;
		this.subscriptionReference = subscriptionReference;
		this.quoteReference = quoteReference;
		this.numeroAttestation = numeroAttestation;
		this.statut = statut;
		this.dateSouscription = dateSouscription;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public String getSubscriptionReference() {
		return subscriptionReference;
	}

	public String getQuoteReference() {
		return quoteReference;
	}

	public String getNumeroAttestation() {
		return numeroAttestation;
	}

	public StatutSouscription getStatut() {
		return statut;
	}

	public LocalDateTime getDateSouscription() {
		return dateSouscription;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
