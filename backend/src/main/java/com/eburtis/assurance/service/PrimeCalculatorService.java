package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Garantie;
import com.eburtis.assurance.domain.ProduitAssurance;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.presentation.dto.simulation.GarantiePrimeDto;
import com.eburtis.assurance.repository.TarifResponsabiliteCivileRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Service
public class PrimeCalculatorService {
	private static final BigDecimal VALEUR_ASSUREE_TIERCE_PLAFONNEE = new BigDecimal("0.50");

	private final TarifResponsabiliteCivileRepository tarifResponsabiliteCivileRepository;

	public PrimeCalculatorService(TarifResponsabiliteCivileRepository tarifResponsabiliteCivileRepository) {
		this.tarifResponsabiliteCivileRepository = tarifResponsabiliteCivileRepository;
	}

	public PrimeCalculationResult calculer(ProduitAssurance produitAssurance, LocalDate datePremiereMiseEnCirculation,
			Integer puissanceFiscale, BigDecimal valeurNeuve, BigDecimal valeurVenale) {
		List<GarantiePrimeDto> garanties = produitAssurance.getGaranties().stream()
				.sorted(Comparator.comparing(Garantie::getCode))
				.filter(garantie -> estEligibleSelonAge(garantie, datePremiereMiseEnCirculation))
				.map(garantie -> calculerGarantie(garantie, datePremiereMiseEnCirculation, puissanceFiscale, valeurNeuve, valeurVenale))
				.toList();

		if (garanties.isEmpty()) {
			throw AssuranceException.badRequest("AUCUNE_GARANTIE_ELIGIBLE",
					"Aucune garantie du produit n'est éligible pour ce véhicule.");
		}

		BigDecimal price = garanties.stream()
				.map(GarantiePrimeDto::getMontant)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new PrimeCalculationResult(price, garanties);
	}

	private GarantiePrimeDto calculerGarantie(Garantie garantie, LocalDate datePremiereMiseEnCirculation,
			Integer puissanceFiscale, BigDecimal valeurNeuve, BigDecimal valeurVenale) {
		BigDecimal montant = switch (garantie.getCode()) {
			case "RC" -> calculerResponsabiliteCivile(puissanceFiscale);
			case "DOMMAGES", "TIERCE_COLLISION" -> arrondir(valeurNeuve.multiply(garantie.getTaux()));
			case "TIERCE_PLAFONNEE" -> calculerTiercePlafonnee(garantie, valeurVenale);
			case "VOL", "INCENDIE" -> arrondir(valeurVenale.multiply(garantie.getTaux()));
			default -> throw AssuranceException.internal("GARANTIE_NON_PARAMETREE",
					"La garantie " + garantie.getCode() + " n'est pas paramétrée pour le calcul.");
		};

		return new GarantiePrimeDto(garantie.getCode(), garantie.getLibelle(), montant);
	}

	private BigDecimal calculerResponsabiliteCivile(Integer puissanceFiscale) {
		return tarifResponsabiliteCivileRepository.rechercherParPuissanceFiscale(puissanceFiscale)
				.map(tarif -> arrondir(tarif.getPrime()))
				.orElseThrow(() -> AssuranceException.badRequest("TARIF_RC_INTROUVABLE",
						"Aucun tarif RC trouvé pour la puissance fiscale " + puissanceFiscale + "."));
	}

	private BigDecimal calculerTiercePlafonnee(Garantie garantie, BigDecimal valeurVenale) {
		// Valeur assurée = 50% de la valeur vénale, puis prime = 4,20% de cette valeur.
		BigDecimal valeurAssuree = valeurVenale.multiply(VALEUR_ASSUREE_TIERCE_PLAFONNEE);
		BigDecimal prime = arrondir(valeurAssuree.multiply(garantie.getTaux()));
		return prime.max(garantie.getPrimeMinimum());
	}

	private boolean estEligibleSelonAge(Garantie garantie, LocalDate datePremiereMiseEnCirculation) {
		if (garantie.getAgeMaximum() == null) {
			return true;
		}

		int ageVehicule = Period.between(datePremiereMiseEnCirculation, LocalDate.now()).getYears();
		return ageVehicule <= garantie.getAgeMaximum();
	}

	private BigDecimal arrondir(BigDecimal montant) {
		return montant.setScale(0, RoundingMode.HALF_UP);
	}
}
