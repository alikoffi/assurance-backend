package com.eburtis.assurance.presentation.dto.souscription;

public class SouscriptionRequestDto {
	private Long simulationId;
	private AssureDto assure;
	private VehiculeDto vehicule;

	public Long getSimulationId() {
		return simulationId;
	}

	public void setSimulationId(Long simulationId) {
		this.simulationId = simulationId;
	}

	public AssureDto getAssure() {
		return assure;
	}

	public void setAssure(AssureDto assure) {
		this.assure = assure;
	}

	public VehiculeDto getVehicule() {
		return vehicule;
	}

	public void setVehicule(VehiculeDto vehicule) {
		this.vehicule = vehicule;
	}
}
