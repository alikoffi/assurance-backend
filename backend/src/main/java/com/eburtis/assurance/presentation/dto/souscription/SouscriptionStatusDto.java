package com.eburtis.assurance.presentation.dto.souscription;

import com.eburtis.assurance.enums.StatutSouscription;

public class SouscriptionStatusDto {
	private Long id;
	private String subscriptionReference;
	private StatutSouscription statut;

	public SouscriptionStatusDto() {
	}

	public SouscriptionStatusDto(Long id, String subscriptionReference, StatutSouscription statut) {
		this.id = id;
		this.subscriptionReference = subscriptionReference;
		this.statut = statut;
	}

	public Long getId() {
		return id;
	}

	public String getSubscriptionReference() {
		return subscriptionReference;
	}

	public StatutSouscription getStatut() {
		return statut;
	}
}
