package com.eburtis.assurance.service;

import com.eburtis.assurance.presentation.dto.simulation.GarantiePrimeDto;

import java.math.BigDecimal;
import java.util.List;

public class PrimeCalculationResult {
	private final BigDecimal price;
	private final List<GarantiePrimeDto> garanties;

	public PrimeCalculationResult(BigDecimal price, List<GarantiePrimeDto> garanties) {
		this.price = price;
		this.garanties = garanties;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public List<GarantiePrimeDto> getGaranties() {
		return garanties;
	}
}
