package com.eburtis.assurance.exception;

import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class AssuranceException extends RuntimeException {
	private final HttpStatus status;
	private final String code;
	private final Collection<String> details;

	private AssuranceException(HttpStatus status, String code, String message, Collection<String> details) {
		super(message);
		this.status = status;
		this.code = code;
		this.details = details;
	}

	public static AssuranceException badRequest(String code, String message) {
		return new AssuranceException(HttpStatus.BAD_REQUEST, code, message, List.of());
	}

	public static AssuranceException notFound(String code, String message) {
		return new AssuranceException(HttpStatus.NOT_FOUND, code, message, List.of());
	}

	public static AssuranceException forbidden(String code, String message) {
		return new AssuranceException(HttpStatus.FORBIDDEN, code, message, List.of());
	}

	public static AssuranceException internal(String code, String message) {
		return new AssuranceException(HttpStatus.INTERNAL_SERVER_ERROR, code, message, List.of());
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public Collection<String> getDetails() {
		return details;
	}
}
