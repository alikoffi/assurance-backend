package com.eburtis.assurance.presentation.dto.auth;

public class TokenDto {

	private String token;
	private boolean mustChangePassword;

	public TokenDto() {
	}

	public TokenDto(String token) {
		this.token = token;
	}

	public TokenDto(String token, boolean mustChangePassword) {
		this.token = token;
		this.mustChangePassword = mustChangePassword;
	}

	public String getToken() {
		return token;
	}

	public boolean isMustChangePassword() {
		return mustChangePassword;
	}

}
