package com.eburtis.assurance.presentation.dto.erreur;

import java.util.Collection;

public class ErreurApi {
	private String code;
	private String message;
	private Collection<String> details;

	public ErreurApi() {
	}

	public ErreurApi(String code, String message, Collection<String> details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Collection<String> getDetails() {
		return details;
	}
}
